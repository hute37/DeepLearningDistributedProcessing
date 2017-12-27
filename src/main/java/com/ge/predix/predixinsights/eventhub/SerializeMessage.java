package com.ge.predix.predixinsights.eventhub;

import java.io.Serializable;

import com.ge.predix.eventhub.Message;

public class SerializeMessage implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

	public SerializeMessage(Message message){
		super();
		this.id_ = message.getId();
		this.body_ = message.getBody().toByteArray();
		this.zoneId_ = message.getZoneId();
		this.key_ = message.getKey().toByteArray();
		this.topic_ = message.getTopic();
		this.partition_ = message.getPartition();
		this.offset_ = message.getOffset();
	}
	private volatile java.lang.Object id_;
	private byte[] body_;	
	private volatile java.lang.Object zoneId_;
	private byte[] key_;
	private volatile java.lang.Object topic_;
	private int partition_;
	private long offset_;

	public java.lang.Object getId_() {
		return id_;
	}
	public void setId_(java.lang.Object id_) {
		this.id_ = id_;
	}
	
	public byte[] getBody_() {
		return body_;
	}
	public void setBody_(byte[] body_) {
		this.body_ = body_;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public java.lang.Object getZoneId_() {
		return zoneId_;
	}
	public void setZoneId_(java.lang.Object zoneId_) {
		this.zoneId_ = zoneId_;
	}
	public byte[] getKey_() {
		return key_;
	}
	public void setKey_(byte[] key_) {
		this.key_ = key_;
	}
	public java.lang.Object getTopic_() {
		return topic_;
	}
	public void setTopic_(java.lang.Object topic_) {
		this.topic_ = topic_;
	}
	public int getPartition_() {
		return partition_;
	}
	public void setPartition_(int partition_) {
		this.partition_ = partition_;
	}
	public long getOffset_() {
		return offset_;
	}
	public void setOffset_(long offset_) {
		this.offset_ = offset_;
	}
	
	  
	

}