package com.owitho.www.kafka.common.partition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import com.owitho.www.kafka.common.GenericMessage;

/**
 * @DESC use MIN ruler to distribute partitions
 * @author saic_xinshiyou
 */
public class MiniumPartition implements IPartition {

	private KafkaConsumer<String, GenericMessage> consumer = null;

	@Override
	public int targetPartition(String topic, int totalPartitions, Object input) {

		Map<TopicPartition, Long> ends = null;
		Map<TopicPartition, Long> commt = new HashMap<>();
		List<TopicPartition> list = new ArrayList<>();

		synchronized (consumer) {// sync

			// just subscribe and do not poll jobs
			// consumer.subscribe(Arrays.asList(new String[] { topic }));
			List<PartitionInfo> lst = consumer.partitionsFor(topic);

			// Generate TopicPartition collection
			for (int i = 0; i < lst.size(); i++)
				list.add(new TopicPartition(lst.get(i).topic(), lst.get(i).partition()));
			// Get end offsets by API
			ends = consumer.endOffsets(list);
			for (TopicPartition tp : list) {
				Long result = consumer.committed(tp).offset();
				commt.put(tp, result);
			}
		}

		// Find the MIN task partition
		int result = Integer.MAX_VALUE;
		int partition = 0;
		for (int i = 0; i < list.size(); i++) {
			TopicPartition key = list.get(i);
			if (Math.abs(ends.get(key) - commt.get(key)) < result) {
				result = (int) Math.abs(ends.get(key) - commt.get(key));
				partition = key.partition();
			}
		}
		// System.out.println("Local log: " + partition);

		return Math.abs(partition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setObject(Object obj) {
		// TODO Auto-generated method stub
		consumer = (KafkaConsumer<String, GenericMessage>) obj;
	}

	@Override
	public Object getObject(Object obj) {
		// TODO Auto-generated method stub
		return consumer;
	}

}
