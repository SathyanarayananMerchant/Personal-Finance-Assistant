import json
import csv


with open("response1.json","r") as f:
    myjson=json.load(f)

ourdata=[]
csvheader=['Category','Description','Qty','Amount']

for x in myjson['receipts']:
    for j in x['items']:
        listing=[j['category'],j['description'],j['qty'],j['amount']]
        ourdata.append(listing)
    

with open('receipt.csv','w') as f:
    writer=csv.writer(f)
    
    writer.writerow(csvheader)
    
    writer.writerows(ourdata)
            
            

    

print('done')