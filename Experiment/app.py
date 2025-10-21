from flask import Flask,request, jsonify, render_template
from flask_cors import CORS
import mysql.connector

app=Flask(__name__)
CORS(app)

db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="Pro@2026",
    database="University"
)

@app.route("/")
def home():
    return render_template("form.html")

@app.route('/search',methods=['POST'])
def search_students():
    data=request.get_json()
    department=data.get("department")

    cursor=db.cursor(dictionary=True)
    query = "select s.name, s.enrollment_year, d.name as department_name from students s join departments d on s.department_id=d.department_id where d.name=%s;"
    cursor.execute(query, (department,))
    results=cursor.fetchall()
    
    cursor.close()
    return jsonify(results)





app.run(debug=True, port=5500)

