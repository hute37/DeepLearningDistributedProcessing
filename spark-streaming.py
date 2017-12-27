
# struct1 = StructType([StructField("OUTAGE_ID", StringType(), True)])
# userSchema = StructType().add("name", "string").add("age", "integer")

from pyspark.sql.types import StructType, StructField, StringType
#
json  = "{'OUTAGE_ID':'1676444','START_DATETIME':'1/10/14 10:33'}"
jsonRdd = sc.parallelize([json])
df1 = spark.read.json(jsonRdd)

# df1 = spark.read.json('/Users/502677522/test/power_data.json')
df1.createOrReplaceTempView('df1');

schema = StructType().add("OUTAGE_ID", "string").add("START_DATETIME", "string")
csv_sdf = spark.readStream.json('/Users/502677522/test', schema)
csv_sdf.createOrReplaceTempView('df2');

# sql = "SELECT count(OUTAGE_ID) as count FROM temp_table";
sql = "SELECT df1.OUTAGE_ID, df1.START_DATETIME, df2.START_DATETIME FROM df1 JOIN df2 ON df1.OUTAGE_ID = df2.OUTAGE_ID"
# df2 = spark.sql(sql)
csv_sdf.join(df1, "OUTAGE_ID")

query = df2 \
    .writeStream \
    .outputMode("complete") \
    .format("console") \
    .start()
query.awaitTermination()
