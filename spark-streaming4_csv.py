import sys
from pyspark.sql import SparkSession
from pyspark import SparkContext
from pyspark.sql.types import StructType, StructField, StringType

if __name__ == "__main__":
    sc = SparkContext(appName="PythonStreamingEventHubWordCount")
    spark = SparkSession(sc)
    spark.conf.set("spark.sql.shuffle.partitions", "5")
    schema = StructType().add("OUTAGE_ID", "string").add("START_DATETIME", "string")\
        .add("RESTORE_DATETIME", "string")
    csv_sdf = spark.readStream.option("maxFilesPerTrigger", 1).csv('/Users/502677522/test_csv', schema, header=True).groupBy("OUTAGE_ID").count()
    query = csv_sdf.writeStream.outputMode("complete").format("console")\
        .start()
    query.awaitTermination()
