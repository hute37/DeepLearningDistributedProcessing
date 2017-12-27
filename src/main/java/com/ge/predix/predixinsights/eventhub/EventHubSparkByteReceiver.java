package com.ge.predix.predixinsights.eventhub;
import static java.lang.System.currentTimeMillis;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;

import com.ge.predix.eventhub.EventHubClientException;
import com.ge.predix.eventhub.Message;
import com.ge.predix.eventhub.client.Client;
import com.ge.predix.eventhub.configuration.EventHubConfiguration;
import com.ge.predix.eventhub.configuration.SubscribeConfiguration;

/**
 * @author Steven Ho 502677522
 * http://spark.apache.org/docs/latest/streaming-custom-receivers.html
 */
public class EventHubSparkByteReceiver extends Receiver<byte[]> {
  private static final Logger LOGGER = LogManager.getLogger(EventHubSparkByteReceiver.class);

  private EventHubConfiguration eventHubConfiguration;
  Properties eventHubProps;

  public EventHubSparkByteReceiver(StorageLevel storageLevel, Properties eventHubProps) {
    super(storageLevel);
    LOGGER.debug("creating a new eventhub receiver");
    this.eventHubProps = eventHubProps;
  }

  //create the event hub client and subscribe data
  public void receive() {
    LOGGER.info("calling create at " + currentTimeMillis());
    convertPropertiesToEHConfiguration(this.eventHubProps);
    Client client = new Client(eventHubConfiguration);    
    client.subscribe(new Client.SubscribeCallback() {
    	long msgCount = 1;
        public void onMessage(Message message) {	            
            LOGGER.debug("<" + msgCount + ">");
            msgCount++;
            //Seiralize object through exception: 
//            File "/opt/spark/python/lib/pyspark.zip/pyspark/serializers.py", line 144, in load_stream
//            yield self._read_with_length(stream)
//          File "/opt/spark/python/lib/pyspark.zip/pyspark/serializers.py", line 169, in _read_with_length
//            return self.loads(obj)
//          File "/opt/spark/python/lib/pyspark.zip/pyspark/serializers.py", line 451, in loads
//            return pickle.loads(obj, encoding=encoding)
//        _pickle.UnpicklingError: invalid load key, '\xac'.
//            store(Serializer.serialize("ABC"));
            //pickle work
//            store(Serializer.pickle(new SerializeObject("data value test"))); 
            store(Serializer.pickle(new SerializeMessage(message))); 
            
        }

        public void onFailure(Throwable throwable) {

        	LOGGER.error("Error when receive message from EventHub: "+throwable);
        }
    });

        
    LOGGER.info("exiting create at " + currentTimeMillis());
  }

  private void convertPropertiesToEHConfiguration(Properties props) {
    EventHubConfiguration.Builder configBuilder = new EventHubConfiguration.Builder()
        .host(props.getProperty("eventhub.host"))
        .zoneID(props.getProperty("eventhub.zoneid"))
        .clientID(props.getProperty("eventhub.client.id"))
        .clientSecret(props.getProperty("eventhub.client.secret"))
        .authURL(props.getProperty("eventhub.auth.url"))
        .port(Integer.parseInt(props.getProperty("eventhub.port")));
    SubscribeConfiguration subscribeConfig = new SubscribeConfiguration.Builder()
        .subscriberName("name" + currentTimeMillis())
        .subscriberInstance("subscriber-instance").build();

    try {
      eventHubConfiguration = configBuilder.build();
    } catch (EventHubClientException.InvalidConfigurationException ex) {
      LOGGER.error("INVALID EVENTHUB CONFIGURATION");
      LOGGER.error(ex);
    }
  }

  @Override
  public void onStart() {
    LOGGER.info("CALLING ONSTART() at " + currentTimeMillis());
    new Thread(this::receive).start();    
  }

  @Override
  public void onStop() {
    //nothing
    LOGGER.info("CALLING ONSTOP() at " + currentTimeMillis());
  }  
  
}
