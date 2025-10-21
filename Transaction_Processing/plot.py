# import pandas as pd
# import re
# import matplotlib.pyplot as plt

# # Load cleaned CSV file
# file_path = "BS.csv"  # Update path if needed
# df = pd.read_csv(file_path)

# # Define categories based on keywords
# category_mapping = {
#     "shopping": ["amazon", "flipkart", "myntra"],
#     "food": ["zomato", "swiggy", "restaurant", "cafe"],
#     "utilities": ["electricity", "water bill", "gas"],
#     "entertainment": ["netflix", "hotstar", "spotify"],
#     "travel": ["uber", "ola", "flight", "train", "bus"],
#     "salary": ["salary", "payroll", "company"],
#     "investment": ["mutual fund", "stocks", "trading"]
# }

# # Function to categorize transactions
# def categorize_transaction(desc):
#     desc_lower = desc.lower()
#     for category, keywords in category_mapping.items():
#         if any(keyword in desc_lower for keyword in keywords):
#             return category
#     return "others"

# # Apply categorization
# df["Category"] = df["Description"].fillna("").apply(categorize_transaction)

# # Plot transfer amounts
# def plot_transfers(df, account_number):
#     # Filter transactions related to the account
#     sent = df[df["Ref No./ChequeNo."].str.contains(f"{account_number}", na=False) & df["Debit"].notna()]
#     received = df[df["Ref No./ChequeNo."].str.contains(f"{account_number}", na=False) & df["Credit"].notna()]

#     plt.figure(figsize=(10, 5))
#     plt.bar(sent["Txn Date"], sent["Debit"], color='red', label="Sent", alpha=0.7)
#     plt.bar(received["Txn Date"], received["Credit"], color='green', label="Received", alpha=0.7)

#     plt.xticks(rotation=45)
#     plt.xlabel("Transaction Date")
#     plt.ylabel("Amount")
#     plt.title(f"Transfers To and From Account {account_number}")
#     plt.legend()
#     plt.show()

# # Example: Call plot function for a specific account number
# account_number = "4897690162095"  # Change this to any account in your data
# # plot_transfers(df, account_number)

# # Save categorized file
# df.to_csv("BS_categorized.csv", index=False)

# def fetch_transaction_description(df, ref_no, txn_date):
#     # Filter based on Ref No./Cheque No. and Transaction Date
#     result = df[(df["Ref No./ChequeNo."] == ref_no) & (df["Txn Date"] == txn_date)]
    
#     if not result.empty:
#         return result[["Txn Date", "Ref No./ChequeNo.", "Description"]]
#     else:
#         return "No transaction found for the given reference number and date."

# # Example usage:
# ref_no_input = "4897690162095"  # Change as needed
# txn_date_input = "1 Dec 2024"  # Change as needed

# print(fetch_transaction_description(df, ref_no_input, txn_date_input))


import pandas as pd
import matplotlib.pyplot as plt

# Define categories and associated keywords
categories = {
    'Groceries': ['supermarket', 'grocery', 'food basics', 'whole foods'],
    'Utilities': ['electric', 'water', 'gas', 'utility'],
    'Transportation': ['gas station', 'public transport', 'uber', 'lyft'],
    'Entertainment': ['cinema', 'netflix', 'spotify', 'concert'],
    'Dining': ['restaurant', 'cafe', 'fast food', 'coffee'],
    'Shopping': ['mall', 'clothing', 'electronics', 'amazon'],
    'Healthcare': ['pharmacy', 'hospital', 'clinic', 'doctor'],
    'Other': []  # Default category for uncategorized transactions
}

# Function to categorize transactions
def categorize_transaction(description):
    for category, keywords in categories.items():
        for keyword in keywords:
            if keyword in description.lower():
                return category
    return 'Other'  # If no keyword matches, categorize as 'Other'

# Sample data from the PDF (manually extracted for demonstration)
data = {
    'Txn Date': ['1 Dec 2024', '1 Dec 2024', '1 Dec 2024', '2 Dec 2024', '5 Dec 2024', '5 Dec 2024', '5 Dec 2024', '6 Dec 2024', '7 Dec 2024', '8 Dec 2024', '8 Dec 2024', '9 Dec 2024', '9 Dec 2024', '10 Dec 2024', '11 Dec 2024', '12 Dec 2024', '12 Dec 2024', '15 Dec 2024', '15 Dec 2024', '16 Dec 2024', '16 Dec 2024', '17 Dec 2024', '17 Dec 2024', '18 Dec 2024', '18 Dec 2024', '19 Dec 2024', '25 Dec 2024', '26 Dec 2024', '27 Dec 2024', '28 Dec 2024', '29 Dec 2024', '31 Dec 2024'],
    'Description': [
        'TO TRANSFER-UP/DR/470231704327/PANN DIA/HDFC/panindiaed/UPI-',
        'TO TRANSFER-UP/DR/470251107748/PANN DIA/HDFC/panindiaed/UPI-',
        'TO TRANSFER-UP/DR/433637629343/ADAMA S U/MDB/pos.112923/UPI-',
        'TO TRANSFER-UP/DR/433766570243/247038 /YESB/paytmqr281/UPI-',
        'TO TRANSFER-UP/DR/434037882804/ADAMA S U/MDB/pos.112923/UPI-',
        'TO TRANSFER-UP/DR/434065080547/QUICK 247/YESB/paytmqr2vx/UPI-',
        'TO TRANSFER-UP/DR/434029496716/PANN DIA/HDFC/panindiaed/UPI-',
        'TO TRANSFER-UP/DR/437743756431/QUICK 247/YESB/paytmqr2vx/UPI-',
        'TO TRANSFER-UP/DR/470855152040/adityakr /UTKS/adityakr24/UPI-',
        'TO TRANSFER-UP/DR/434328503058/POS FOOD/HDFC/postoodcou/UPI-',
        'TO TRANSFER-UP/DR/470907004699/PANIN DIA/HDFC/panindiaed/UPI-',
        'TO TRANSFER-UP/DR/434480881068/PANIN DIA/HDFC/panindiaed/UPI-',
        'TO TRANSFER-UP/DR/434481406619/IRCTC UTS/VESB/paytm-8796/UPI-',
        'TO TRANSFER-UP/DR/434552344888/QUICK 247/VESB/paytmq2vx/UPI-',
        'TO TRANSFER-UP/DR/47224409815/ADAMA S.UMIDB/pos 112923/UPI-',
        'TO TRANSFER-UP/DR/471391183339/AMAZO N PAY/UTIB/amazonpay@/You -',
        'TO TRANSFER-UP/DR/471361498824/QUICK 247/VESB/paytmq2vx/UPI-',
        'TO TRANSFER-UP/DR/434978266968/adityakr /UTKS/adityakr24/UPI-',
        'TO TRANSFER-UP/DR/471681384640/POS SPA/HDFC/pospsa.640/UPI-',
        'TO TRANSFER-UP/DR/471706058813/QUICK 247/VESB/paytmq2vx/UPI-',
        'TO TRANSFER-UP/DR/471741752620/QUICK 247/VESB/paytmq2vx/UPI-',
        'TO TRANSFER-UP/DR/471872143135/AYESHA K/VESB/paytm.s165/UPI-',
        'TO TRANSFER-UP/DR/225057901419/Rahul R /SBIN/7077346439/Payme-',
        'TO TRANSFER-UP/DR/435266942262/KANHA IYA/VESB/paytmq563/UPI-',
        'TO TRANSFER-UP/DR/471974352499/Mr RAGHA/VESB/tq427451709/UP -',
        'TO TRANSFER-UP/DR/658373314720/CHIRA G D/BARB/7091266605/Payme -',
        'TO TRANSFER-UP/DR/436463928398/TSHER NG/SBN/Herobratshe/UPI-',
        'CREDIT INTEREST--',
        'TO TRANSFER-UP/DR/436294948595/PANIN DIA/HDFC/panindiaed/UPI-',
        'TO TRANSFER-UP/DR/436350753242/QUICK 247/YESB/paytmgr2xx/UPI-',
        'TO TRANSFER-UP/DR/473000576098/PANIN DIA/HDFC/panindiaed/UPI-',
        'TO TRANSFER-UP/DR/436610556669/247038 /YESB/paytmgr281/UPI-'
    ],
    'Debit': [150.00, 32.00, 38.00, 20.00, 22.00, 20.00, 189.00, 40.00, 1400.00, 25.00, 45.00, 116.00, 60.00, 40.00, 142.00, 1218.00, 60.00, 12500.00, 125.00, 30.00, 30.00, 10.00, 50.00, 30.00, 50.00, 240.00, 40.00, 0.00, 252.00, 40.00, 126.00, 60.00]
}


df = pd.DataFrame(data)

df['Category'] = df['Description'].apply(categorize_transaction)


df = df[df['Debit'] > 0]


monthly_spending = df.groupby('Category')['Debit'].sum().reset_index()

plt.figure(figsize=(10, 6))
plt.bar(monthly_spending['Category'], monthly_spending['Debit'], color='skyblue')
plt.xlabel('Category')
plt.ylabel('Total Spending (INR)')
plt.title('Monthly Spending by Category (Dec 2024)')
plt.xticks(rotation=45)
plt.tight_layout()
plt.show()

