from keras.applications.inception_v3 import preprocess_input
from keras.preprocessing.image import img_to_array, load_img
import numpy as np
import os
from sparkdl import KerasImageFileTransformer
from pyspark.sql.types import StringType
from pyspark.sql import SQLContext

def loadAndPreprocessKerasInceptionV3_save(uri):
    # this is a typical way to load and prep images in keras
    image = img_to_array(load_img(uri, target_size=(299, 299)))
    image = np.expand_dims(image, axis=0)
    return preprocess_input(image)


def read_image(file_path):
    img = image.load_img(file_path, grayscale=False, target_size=(150,150))
    return image.img_to_array(img)/255

def prep_data(images):
    count = len(images)
    data = np.ndarray((count, ROWS, COLS, CHANNELS), dtype=np.float32)
    for i, image_file in enumerate(images):
        image = read_image(image_file)
        data[i] = image
        if i%2500 == 0:
            print('Processed {} of {}'.format(i, count))
            #print image_file
    return data

def loadAndPreprocessKerasInceptionV3(uri):
    # this is a typical way to load and prep images in keras
    image = read_image(uri)
    # img_to_array(load_img(uri, target_size=(299, 299)))
    image = np.expand_dims(image, axis=0)
    return preprocess_input(image)


transformer = KerasImageFileTransformer(inputCol="uri", outputCol="predictions",
                                        modelFile="/tmp/model-full.h5",
                                        imageLoader=loadAndPreprocessKerasInceptionV3,
                                        outputMode="vector")
sc = SparkContext(appName="PythonStreamingEventHubWordCount")
sqlContext = SQLContext(sc)

files = [os.path.abspath(os.path.join(dirpath, f)) for f in os.listdir("/data/test") if f.endswith('.jpg')]
uri_df = sqlContext.createDataFrame(files, StringType()).toDF("uri")

final_df = transformer.transform(uri_df)