package com.ge.predix.predixinsights.eventhub;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.razorvine.pickle.PickleException;
import net.razorvine.pickle.Pickler;

public class Serializer {
	private static final Logger logger = LogManager.getLogger(Serializer.class);
    public static byte[] serialize(Object obj){
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }catch (Exception e){
        	logger.error("Exception as serialize object: " + e.getMessage());
        	return null;
        }
    }
    
    public static byte[] pickle(Object obj){
    	Pickler pickler = new Pickler();
    	try {
			return pickler.dumps(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("exception pickle: "+ e.getMessage());
			return null;
		}
    	
    
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }

}