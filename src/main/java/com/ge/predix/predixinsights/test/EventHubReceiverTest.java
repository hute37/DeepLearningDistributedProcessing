package com.ge.predix.predixinsights.test;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.ge.predix.eventhub.Message;
import com.ge.predix.predixinsights.eventhub.EventHubUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EventHubReceiverTest {

  private static final Logger LOGGER = LogManager.getLogger(EventHubReceiverTest.class);

  public static void main(String[] args) {
 
	 
	SparkConf sparkConf = new SparkConf().setAppName(EventHubReceiverTest.class.getName());    
    JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, new Duration(5000));


    JavaDStream<String> customReceiverStream = EventHubUtils.createStream(streamingContext,null);

    System.out.println("print the dstream with size: " + customReceiverStream.count());
    customReceiverStream.print();    
    
    LOGGER.info("** starting streaming context **");

    streamingContext.start();

    try {
      LOGGER.info("** awaiting termination **");
      streamingContext.awaitTermination();
    } catch (InterruptedException ex) {
      LOGGER.error("interrupted: " + ex.toString());
    }
  }
}
