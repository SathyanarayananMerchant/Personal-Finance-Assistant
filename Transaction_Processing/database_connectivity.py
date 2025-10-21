import pandas as pd
from pymongo import MongoClient


client = MongoClient("mongodb://localhost:27017/")
db=client["Transactions"]
collection=db["Transactions"]

def load_transactions(filepath):
    return pd.read_csv(filepath)

class DB:

    def initialize_db(self,file):
        data=load_transactions(file)
        transactions=data.to_dict(orient="records")
        #Creating collections
        collection.insert_many(transactions)
        print(f"Inserted {len(transactions)} transactions in the database")
        client.close()
        

    def fetch_specific(self):
        user_input=input("Enter the transaction id/description/amount to search:- ")

        query={
            "$or":[
                {"id":user_input},
                {"description":user_input},
                {"debit":float(user_input)},
            ]
        }

        transaction=collection.find_one(query)

        if transaction:
            print('\nTransaction found:\n')
            print(transaction)
        else:
            print("\nTransaction not found with the given input")
        client.close()


        

    def fetch(self):
        user_inp=input('Do you want to fetch a specific transaction or all of them(s/a):-')
        if(user_inp=='specific'or user_inp=='Specific' or user_inp=='s'or user_inp=='S'):
            self.fetch_specific()
        else:
            all_transactions=list(collection.find())
            print('All transactions:-\n')
            for transaction in all_transactions:
                print(transaction)
        
        client.close()

            





filepath=input("Insert file path for the bank statement:-")
print(type(filepath))
db=DB()
# db.initialize_db(filepath)

db.fetch()
db.fetch_specific()








