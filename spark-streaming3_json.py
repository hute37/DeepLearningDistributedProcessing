import sys
from pyspark.sql import SparkSession
from pyspark import SparkContext
from pyspark.sql.types import StructType, StructField, StringType

if __name__ == "__main__":
    sc = SparkContext(appName="PythonStreamingEventHubWordCount")
    spark = SparkSession(sc)
    schema = StructType().add("OUTAGE_ID", "string").add("START_DATETIME", "string")
    csv_sdf = spark.readStream.json('/Users/502677522/test', schema).groupBy("OUTAGE_ID").count()
    query = csv_sdf.writeStream.outputMode("complete").format("console")\
        .start()
    query.awaitTermination()
