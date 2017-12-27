


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;
import org.junit.Test;
import com.ge.predix.eventhub.client.Client;
import com.ge.predix.insights.connector.eh.impl.EventHubConnector;
import com.ge.predix.insights.connector.eh.service.PublisherService;

public class PublisherTest {
	private static final Logger logger = Logger.getLogger(PublisherTest.class.getName());
	Properties props;
	Client eventHubClient = null;
	String fileName = "testing-data-throughput.csv";
	
	public void loadProperties() {
		props = new Properties();
		logger.info("** create EH stream **");
		try (InputStream input = PublisherTest.class.getClassLoader().getResourceAsStream("eventhub.properties")) {
			props.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}		
	}

	@Test
	public void readCSV() {

		String fileName = "testing-data-throughput.csv";
		String line = "";        

		try (BufferedReader bReader = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream("/" + fileName)))) {
			while ((line = bReader.readLine()) != null) {
				System.out.println("line: " + line);								
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void publishMessage() throws Exception {
		logger.info("start publish message: ");		
		EventHubConnector eventHubConnector = EventHubConnector.newBuilder().setReceiverConfigFileName("eventhub.properties").setPublisherConfigFileName("eventhubPublisher.properties").build(); 
		eventHubConnector.publish("payload 1");		
		logger.info("finish publish message: ");
	}

	@Test
	public void publishStreamMessagesFromCSV() throws Exception {
		logger.info("start publish message: ");		
		EventHubConnector eventHubConnector = EventHubConnector.newBuilder()
				.setReceiverConfigFileName("eventhub.properties")
				.setPublisherConfigFileName("eventhub.properties").build();
		

		String line = "";
		try (BufferedReader bReader = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream("/" + fileName)))) {
			while ((line = bReader.readLine()) != null) {				
				System.out.println("sending data: " + line);
				eventHubConnector.publish(line);
				Thread.sleep(1000);	
			}
			eventHubConnector.getPublisherService().shutDownEventHubClient();	
		} catch (Exception e) {
			e.printStackTrace();
		}				
		logger.info("finish publish message: ");
	}
	
	

}
