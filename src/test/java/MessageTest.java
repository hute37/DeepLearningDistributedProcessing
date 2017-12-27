import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ge.predix.eventhub.Message;
import com.ge.predix.predixinsights.eventhub.SerializeMessage;
import com.ge.predix.predixinsights.eventhub.SerializeObject;
import com.ge.predix.predixinsights.eventhub.Serializer;
import com.google.protobuf.ByteString;

import java.util.Iterator;


public class MessageTest {
	@Test
	public void testMessageString(){
		
		System.out.println("testing String");
		List<Message> messages = new ArrayList<Message>(); 
		Message.Builder builder = Message.newBuilder().setId("1").setTopic("topic 1").setBody(ByteString.copyFromUtf8("Data"));
		Message message1 = builder.build(); 
		Message message2 = builder.build();
		messages.add(message1);
		messages.add(message2);
		Iterator<Message> iter =  messages.iterator();
		while(iter.hasNext()){
			System.out.println("data: " + iter.next());
		}
	}
	
	
	@Test
	public void testPickleStringObject(){
		SerializeObject object = new SerializeObject("data");
		byte[] bytes = Serializer.pickle(object);
		System.out.println(bytes);
	}
	
	@Test
	public void testPickleObject(){
		Message.Builder builder = Message.newBuilder().setId("1").setTopic("topic 1").setBody(ByteString.copyFromUtf8("Data"));
		Message message1 = builder.build(); 
		byte[] bytes = Serializer.pickle(new SerializeMessage(message1));
		System.out.println(bytes);
	}
	
}
