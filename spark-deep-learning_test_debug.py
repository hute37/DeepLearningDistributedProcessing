# pyspark --packages databricks:spark-deep-learning:0.1.0-spark2.1-s_2.11
# https://github.com/databricks/spark-deep-learning/issues/18
# from sparkdl import readImages
import sparkdl
image_df = sparkdl.readImages("/Users/502677522/Class/DataWeekend/2017_12_02/data/test/1.jpg")
image_df.show()

from keras.applications.inception_v3 import preprocess_input
from keras.preprocessing.image import img_to_array, load_img
import numpy as np
import os
from sparkdl import KerasImageFileTransformer

def loadAndPreprocessKerasInceptionV3(uri):
    # this is a typical way to load and prep images in keras
    image = img_to_array(load_img(uri, target_size=(299, 299)))
    image = np.expand_dims(image, axis=0)
    return preprocess_input(image)

uri = "/Users/502677522/Class/DataWeekend/2017_12_02/data/test/1.jpg"
dat = loadAndPreprocessKerasInceptionV3(uri)

# >> > dat.shape
# (1, 299, 299, 3)