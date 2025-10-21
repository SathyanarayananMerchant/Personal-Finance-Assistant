import requests
import json

url="https://ocr.asprise.com/api/v1/receipt"

image="therma.jpg"

res=requests.post(url,data = {'api_key':'TEST','recognizer':'auto','ref_no':'my_ref_123'},
                  files={'file':open(image,'rb')})

with open("response1.json","w") as f:
    json.dump(json.loads(res.text),f)






