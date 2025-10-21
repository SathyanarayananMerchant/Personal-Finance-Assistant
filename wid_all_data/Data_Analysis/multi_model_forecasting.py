import pandas as pd
from statsmodels.tsa.api import Holt
from statsmodels.tsa.arima.model import ARIMA
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error, mean_squared_error
import numpy as np
import warnings

# Suppress warnings for cleaner output
warnings.filterwarnings("ignore")

# --- 1. Load and Prepare Data ---
try:
    df = pd.read_csv('WID_data_IN.csv', delimiter=';')
except FileNotFoundError:
    print("Error: WID_data_IN.csv not found.")
    exit()

df['year'] = pd.to_datetime(df['year'], format='%Y')
variable = 'ehfcari999' # You can change this to another variable if needed

# --- 2. Get All Unique Percentiles and Initialize Results ---
all_percentiles = df[df['variable'] == variable]['percentile'].unique()
all_results = []

print(f"Starting evaluation for {len(all_percentiles)} percentiles. This may take some time...")

# --- 3. Loop Through Each Percentile ---
for i, percentile in enumerate(all_percentiles):
    print(f"Processing {percentile} ({i+1}/{len(all_percentiles)})...")
    
    try:
        ts_df = df[(df['variable'] == variable) & (df['percentile'] == percentile)]
        ts = ts_df.set_index('year')['value'].asfreq('AS').dropna()

        # Ensure there is enough data to split
        if len(ts) < 10:
            print(f"  -> Skipping {percentile} due to insufficient data.")
            continue

        # Split data
        train = ts[ts.index.year <= 2015]
        test = ts[ts.index.year > 2015]
        
        if test.empty or train.empty:
            print(f"  -> Skipping {percentile} due to empty train/test set after split.")
            continue

        # --- Model 1: Naive Forecast ---
        naive_predictions = test.shift(1).bfill()
        all_results.append({
            'Percentile': percentile, 'Model': 'Naive Forecast',
            'RMSE': np.sqrt(mean_squared_error(test, naive_predictions)),
            'MAE': mean_absolute_error(test, naive_predictions)
        })

        # --- Model 2: Holt's Linear Trend ---
        holt_model = Holt(train, initialization_method="estimated").fit()
        holt_predictions = holt_model.forecast(steps=len(test))
        all_results.append({
            'Percentile': percentile, 'Model': "Holt's Linear",
            'RMSE': np.sqrt(mean_squared_error(test, holt_predictions)),
            'MAE': mean_absolute_error(test, holt_predictions)
        })

        # --- Model 3: ARIMA ---
        arima_model = ARIMA(train, order=(5, 1, 0)).fit()
        arima_predictions = arima_model.forecast(steps=len(test))
        all_results.append({
            'Percentile': percentile, 'Model': 'ARIMA',
            'RMSE': np.sqrt(mean_squared_error(test, arima_predictions)),
            'MAE': mean_absolute_error(test, arima_predictions)
        })
        
        # --- Model 4: Random Forest ---
        def create_lag_features(data, n_lags=3):
            df_lags = pd.DataFrame(data)
            for j in range(1, n_lags + 1):
                df_lags[f'lag_{j}'] = df_lags['value'].shift(j)
            df_lags.dropna(inplace=True)
            return df_lags

        lag_data = create_lag_features(ts)
        X = lag_data.drop('value', axis=1)
        y = lag_data['value']
        
        X_train, X_test = X[X.index.year <= 2015], X[X.index.year > 2015]
        y_train, y_test = y[y.index.year <= 2015], y[y.index.year > 2015]

        if not y_test.empty:
            rf = RandomForestRegressor(n_estimators=100, random_state=42).fit(X_train, y_train)
            rf_predictions = rf.predict(X_test)
            all_results.append({
                'Percentile': percentile, 'Model': 'Random Forest',
                'RMSE': np.sqrt(mean_squared_error(y_test, rf_predictions)),
                'MAE': mean_absolute_error(y_test, rf_predictions)
            })

    except Exception as e:
        print(f"  -> An error occurred for percentile {percentile}: {e}")

# --- 4. Consolidate and Display Final Results ---
results_df = pd.DataFrame(all_results)
# Set a multi-index and sort for clear comparison
results_df.set_index(['Percentile', 'Model'], inplace=True)
results_df.sort_index(inplace=True)

print("\n--- Comprehensive Model Evaluation Summary ---")
# To display all rows in the output
pd.set_option('display.max_rows', None)
print(results_df)
pd.reset_option('display.max_rows') # Reset display option

# Save results to a CSV file for further analysis
results_df.to_csv('all_percentiles_model_evaluation.csv')
print("\nFull evaluation summary saved to 'all_percentiles_model_evaluation.csv'")