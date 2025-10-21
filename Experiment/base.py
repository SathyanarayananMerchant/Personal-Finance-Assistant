import mysql.connector
from dotenv import load_dotenv
import os

load_dotenv()

db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="Pro@2026",
    database="University"
)

cursor=db.cursor()

query="""
select * from professors;
"""

cursor.execute(query)

for (p_id,name,email,phone,department_id) in cursor.fetchall():
    print(f"{p_id} {name} is enrolled in {email}")

cursor.close()
db.close()

# from flask import Flask,render_template

# app=Flask(__name__)

# @app.route("/")
# def hello(name=None):
#     return render_template('hello_flask.html', person=name)

# app.run()