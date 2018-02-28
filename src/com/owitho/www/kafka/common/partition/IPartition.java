package com.owitho.www.kafka.common.partition;


/**
 * @DESC INTERFACE
 * @author saic_xinshiyou
 */
public interface IPartition {

	public int targetPartition(String topic, int totalPartitions, Object input) throws Exception;

	public void setObject(Object obj);

	public Object getObject(Object obj);

}