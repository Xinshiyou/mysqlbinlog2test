package com.owitho.www.kafka.common;

import com.owitho.www.enums.KafkaPartitionSelectionStrategy;
import com.owitho.www.kafka.common.partition.IPartition;
import com.owitho.www.kafka.common.partition.MiniumPartition;
import com.owitho.www.kafka.common.partition.OrderedPartition;
import com.owitho.www.kafka.common.partition.RandomPartition;

/**
 * @DESC 选择分配partition的策略
 * @author saic_xinshiyou
 */
public class PartitionFactory {

	private static PartitionFactory instance = null;

	private PartitionFactory() {
	}

	public static PartitionFactory getInstance() {
		if (null == instance)
			instance = new PartitionFactory();
		return instance;
	}

	public IPartition getStrategy(KafkaPartitionSelectionStrategy strategy) {

		IPartition result = null;
		switch (strategy) {
		case RANDOM:
			result = new RandomPartition();
			break;
		case MINIMUM:
			result = new MiniumPartition();
			break;
		case ORDERED:
			result = new OrderedPartition();
			break;
		default:// 外部处理时间
			break;
		}

		return result;
	}

}
