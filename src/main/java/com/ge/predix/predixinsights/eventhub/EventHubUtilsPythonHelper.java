package com.ge.predix.predixinsights.eventhub;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.ge.predix.eventhub.Message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EventHubUtilsPythonHelper {

  private static final Logger LOGGER = LogManager.getLogger(EventHubUtilsPythonHelper.class);

  public JavaDStream<byte[]> createStream(JavaStreamingContext jssc, StorageLevel storageLevel) {

	    Properties eventHubProps = new Properties();
	    LOGGER.info("** create EH stream **");
	    try (InputStream input = EventHubUtilsPythonHelper.class
	        .getClassLoader()
	        .getResourceAsStream("eventhub.properties")) {
	      eventHubProps.load(input);
	    } catch (IOException ex) {
	      ex.printStackTrace();
	    }

	    JavaDStream<byte[]> customReceiverStream = jssc.receiverStream(
	        new EventHubSparkByteReceiver(StorageLevel.MEMORY_AND_DISK_2(), eventHubProps)
	    );
	    
	   	return customReceiverStream;    	     
	  }
      
}
