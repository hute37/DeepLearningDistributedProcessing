
from py4j.protocol import Py4JJavaError

from pyspark.storagelevel import StorageLevel
from pyspark.serializers import UTF8Deserializer,PickleSerializer
from pyspark.streaming import DStream

__all__ = ['EventHubUtils']


class EHUtils(object):

    @staticmethod
    def createStream(ssc, storageLevel=StorageLevel.MEMORY_AND_DISK_SER_2):
        """
        Create an input stream that pulls messages from a Event Hub.

        :param ssc:  StreamingContext object        
        :param storageLevel:  RDD storage level.
        :return: A DStream object
        """
        jlevel = ssc._sc._getJavaStorageLevel(storageLevel)

        try:
            helperClass = ssc._jvm.java.lang.Thread.currentThread().getContextClassLoader() \
                .loadClass("com.ge.predix.predixinsights.eventhub.EventHubUtilsPythonHelper")
            helper = helperClass.newInstance()
            jstream = helper.createStream(ssc._jssc, jlevel)
        except Py4JJavaError as e:
            if 'ClassNotFoundException' in str(e.java_exception):
                EHUtils._printErrorMsg(ssc.sparkContext)
            raise e

        return DStream(jstream, ssc, PickleSerializer())

    @staticmethod
    def _printErrorMsg(sc):
        print("""
________________________________________________________________________________________________

  Spark Streaming's EventHub libraries not found in class path. Try one of the following.

  1. Include the EventHub library and its dependencies with in the
     spark-submit command as

     $ bin/spark-submit --packages org.apache.spark:spark-streaming-EventHub:%s ...

  2. Download the JAR of the artifact from Maven http://ge.maven.org/,
     Group Id = org.ge.predix, Artifact Id = spark-streaming-EventHub-assembly, Version = %s.
     Then, include the jar in the spark-submit command as

     $ bin/spark-submit --jars <spark-streaming-EventHub-assembly.jar> ...
________________________________________________________________________________________________
""" % (sc.version, sc.version))

import sys
from pyspark.sql import SparkSession
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.ml import PipelineModel
from pyspark.sql import SQLContext

if __name__ == "__main__":

    # spark = SparkSession.builder.master("local").appName("PythonStreamingEventHub").getOrCreate()
    # sc = spark.sparkContext

    sc = SparkContext(appName="PythonStreamingEventHubWordCount")
    sqlContext = SQLContext(sc)
    spark = SparkSession(sc)


    def printRddElement(x):
        print ("rdd element: ")
        print(x)

    def process(time, rdd):
        print("time========= %s =========time" % str(time))
        if rdd.isEmpty() != True:
            print ("proccess rdd: ")
            print (rdd.take(5))
            # rdd.forEach(lambda o: print(type(o))
        else:
            print("no data")

    ssc = StreamingContext(sc, 5)
    lines = EHUtils.createStream(ssc)
    lines.foreachRDD(process)
    ssc.start()
    ssc.awaitTermination()