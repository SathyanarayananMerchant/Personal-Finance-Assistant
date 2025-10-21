import pandas as pd
import matplotlib.pyplot as plt
from statsmodels.tsa.arima.model import ARIMA
import warnings
import os

# Suppress warnings from statsmodels
warnings.filterwarnings("ignore")

# --- 1. Load and Prepare the Data ---
try:
    df = pd.read_csv('WID_data_IN.csv', delimiter=';')
except FileNotFoundError:
    print("Error: WID_data_IN.csv not found. Please place it in the same directory as this script.")
    exit()

# Set 'year' as a proper datetime object (start of the year)
df['year'] = pd.to_datetime(df['year'], format='%Y')

# Get unique variables starting with 'wgw' or 'ehf'
variables_to_forecast = [
    var for var in df['variable'].unique() 
    if var.startswith('wgw') or var.startswith('ehf')
]

# Create a directory to save the forecast plots
if not os.path.exists('forecast_plots'):
    os.makedirs('forecast_plots')

print(f"Found {len(variables_to_forecast)} variables to forecast...")

# --- 2. Loop, Forecast, and Plot ---
for variable in variables_to_forecast:
    print(f"Forecasting for {variable}...")
    
    # Filter for the specific variable and the p0p100 percentile
    ts_df = df[(df['variable'] == variable) & (df['percentile'] == 'p0p100')]
    
    # Check if data exists for this combination
    if ts_df.empty:
        print(f"  -> No data for {variable} at p0p100 percentile. Skipping.")
        continue

    # Create the time series, using 'year' as the index
    ts = ts_df.set_index('year')['value'].asfreq('AS') # 'AS' for Annual, Start of year

    # --- 3. Fit the ARIMA Model ---
    # The (p,d,q) parameters are standard for ARIMA. (5,1,0) is a common starting point.
    # p: The number of past observations to include (lag order).
    # d: The number of times the data is differenced to make it stationary.
    # q: The size of the moving average window.
    model = ARIMA(ts, order=(5, 1, 0))
    fitted_model = model.fit()

    # --- 4. Generate Forecast ---
    # Forecast the next 10 years
    forecast_result = fitted_model.get_forecast(steps=10)
    
    # Get the forecast and confidence intervals
    forecast_mean = forecast_result.predicted_mean
    confidence_intervals = forecast_result.conf_int()

    # --- 5. Plot the Results ---
    plt.figure(figsize=(12, 7))
    plt.plot(ts.index, ts, label='Historical Data')
    plt.plot(forecast_mean.index, forecast_mean, color='red', label='Forecast')

    # Shade the confidence interval
    plt.fill_between(confidence_intervals.index,
                     confidence_intervals.iloc[:, 0],
                     confidence_intervals.iloc[:, 1], color='pink', alpha=0.7)

    plt.title(f'Forecast for {variable} (p0p100)')
    plt.xlabel('Year')
    plt.ylabel('Value')
    plt.legend()
    plt.grid(True)
    
    # Save the plot
    plt.savefig(f'forecast_plots/{variable}_p0p100_forecast.png')
    plt.close()

print("\nAll forecasts complete. Plots are saved in the 'forecast_plots' directory.")