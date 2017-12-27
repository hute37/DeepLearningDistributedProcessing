# pyspark --packages databricks:spark-deep-learning:0.1.0-spark2.1-s_2.11
# spark-submit --packages databricks:spark-deep-learning:0.1.0-spark2.1-s_2.11 spark-deep-learning.py
# https://github.com/databricks/spark-deep-learning/issues/18
import sparkdl
from keras.preprocessing import image
from keras.applications.inception_v3 import preprocess_input
from keras.preprocessing.image import img_to_array, load_img
import numpy as np
import os
from sparkdl import KerasImageFileTransformer
from pyspark.sql.types import *
# http://spark.apache.org/docs/2.1.0/api/python/pyspark.sql.html#pyspark.sql.SparkSession
# https://community.cloud.databricks.com/?o=4116656499064282#notebook/802030457004261/command/802030457004265
# def loadAndPreprocessKerasInceptionV3_github-spark(uri):
#     # this is a typical way to load and prep images in keras
#     image = img_to_array(load_img(uri, target_size=(299, 299)))
#     image = np.expand_dims(image, axis=0)
#     return preprocess_input(image)
# image_df = sparkdl.readImages("/Users/502677522/Class/DataWeekend/2017_12_02/data/test/1.jpg")
# image_df.show()

# def loadAndPreprocessKerasInceptionV3(uri):
#     # this is a typical way to load and prep images in keras
#     image = read_image(uri)
#     image = np.expand_dims(image, axis=0)
#     return preprocess_input(image)

from pyspark.sql import SparkSession
spark = SparkSession.builder.master("local").appName("Word Count").getOrCreate()


def read_image(file_path):
    img = image.load_img(file_path, grayscale=False, target_size=(150,150))
    return image.img_to_array(img)/255

def loadAndPreprocessKerasInceptionV3(uri):
    in_data = np.ndarray((1, 150, 150, 3), dtype=np.float32)
    img = read_image(uri)
    in_data[0] = img
    return in_data

transformer = KerasImageFileTransformer(inputCol="uri", outputCol="predictions",
                                        modelFile="/Users/502677522/Class/DataWeekend/2017_12_02/labs/vgg16_dogcat_transfer.h5",
                                        imageLoader=loadAndPreprocessKerasInceptionV3,
                                        outputMode="vector")
dirpath = "/Users/502677522/Class/DataWeekend/2017_12_02/data/test"

# files = [os.path.abspath(os.path.join(dirpath, f)) for f in os.listdir("/Users/502677522/Class/DataWeekend/2017_12_02/data/test") if f.endswith('11.jpg')]
# >>> spark.createDataFrame(rdd, "a: string, b: int").collect()
# [Row(a=u'Alice', b=1)]
# >>> rdd = rdd.map(lambda row: row[1])
# >>> spark.createDataFrame(rdd, "int").collect()
# [Row(value=1)]
# >>> spark.createDataFrame(rdd, "boolean").collect()
# Traceback (most recent call last):
# uri_df = sqlContext.createDataFrame(files, StringType()).toDF("uri")
files = [os.path.abspath(os.path.join(dirpath, f)) for f in os.listdir("/Users/502677522/Class/DataWeekend/2017_12_02/data/test_temp") if f.endswith('.jpg')]

# files = [os.path.abspath(os.path.join(dirpath, f)) for f in os.listdir("/Users/502677522/Class/DataWeekend/2017_12_02/data/test") if f == '3.jpg']
uri_df = spark.createDataFrame(files, "string").toDF("uri")
final_df = transformer.transform(uri_df)
final_df.show()
print final_df.collect()
