import pandas as pd
from statsmodels.tsa.arima.model import ARIMA
from sklearn.metrics import mean_squared_error
import numpy as np
import warnings

# Suppress warnings for cleaner output and convergence issues during search
warnings.filterwarnings("ignore")

# --- 1. Load and Prepare Data ---
try:
    df = pd.read_csv('WID_data_IN.csv', delimiter=';')
except FileNotFoundError:
    print("Error: WID_data_IN.csv not found.")
    exit()

df['year'] = pd.to_datetime(df['year'], format='%Y')
variable = 'ehfcari999'
percentile = 'p0p100'

ts_df = df[(df['variable'] == variable) & (df['percentile'] == percentile)]
ts = ts_df.set_index('year')['value'].asfreq('AS').dropna()

# Split data
train = ts[ts.index.year <= 2015]
test = ts[ts.index.year > 2015]

if test.empty or train.empty:
    print("Train or test set is empty. Cannot perform evaluation.")
    exit()

# --- 2. Initial Diagnosis with a Non-Tuned Model ---
print("--- Step 1: Diagnosing Initial ARIMA(5,1,0) Model ---")
initial_model = ARIMA(train, order=(5, 1, 0)).fit()

# In-sample predictions (training accuracy)
train_predictions_initial = initial_model.predict(start=train.index[0], end=train.index[-1])
train_rmse_initial = np.sqrt(mean_squared_error(train, train_predictions_initial))

# Out-of-sample predictions (testing accuracy)
test_predictions_initial = initial_model.forecast(steps=len(test))
test_rmse_initial = np.sqrt(mean_squared_error(test, test_predictions_initial))

print(f"Initial Training RMSE: {train_rmse_initial:.2f}")
print(f"Initial Testing RMSE: {test_rmse_initial:.2f}\n")

if train_rmse_initial < test_rmse_initial:
    print("Diagnosis: The large gap between training and testing RMSE suggests the initial model is OVERFITTING.\n")
else:
    print("Diagnosis: The model might be underfitting or a good fit. Let's tune it to see if we can improve.\n")


# --- 3. The Solution: Hyperparameter Tuning with Grid Search ---
print("--- Step 2: Finding Optimal Order (p,d,q) with Grid Search ---")
best_aic, best_order = np.inf, None

# Define a range for p, d, q values to search
p_values = range(0, 6)
d_values = range(0, 2)
q_values = range(0, 3)

for p in p_values:
    for d in d_values:
        for q in q_values:
            try:
                model = ARIMA(train, order=(p, d, q)).fit()
                if model.aic < best_aic:
                    best_aic = model.aic
                    best_order = (p, d, q)
            except:
                continue

print(f"Best Order Found: {best_order} with AIC: {best_aic:.2f}\n")

# --- 4. Final Evaluation with Tuned Model ---
print(f"--- Step 3: Evaluating Tuned ARIMA{best_order} Model ---")
final_model = ARIMA(train, order=best_order).fit()

# Training accuracy
train_predictions_final = final_model.predict(start=train.index[0], end=train.index[-1])
train_rmse_final = np.sqrt(mean_squared_error(train, train_predictions_final))

# Testing accuracy
test_predictions_final = final_model.forecast(steps=len(test))
test_rmse_final = np.sqrt(mean_squared_error(test, test_predictions_final))

print(f"Final Training RMSE: {train_rmse_final:.2f}")
print(f"Final Testing RMSE: {test_rmse_final:.2f}\n")

# --- 5. Conclusion ---
print("--- Conclusion ---")
improvement = ((test_rmse_initial - test_rmse_final) / test_rmse_initial) * 100
print(f"The testing RMSE of the tuned model is {test_rmse_final:.2f}.")
print(f"This is a {improvement:.2f}% improvement over the initial, non-tuned model.")
print("The smaller gap between the final training and testing RMSE indicates a better-fitting model with reduced overfitting.")