from pyspark.sql import SparkSession
from pyspark.sql.functions import col

spark = SparkSession.builder.appName("SQL_Functions") \
    .config("spark.jars", "/content/sqlite-jdbc-3.42.0.0.jar") \
    .getOrCreate()

df=spark.read.csv