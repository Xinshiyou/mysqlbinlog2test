package com.owitho.www.enums;

/**
 * @DESC KAFKA partition strategy
 * @author xinshiyou
 */
public enum KafkaPartitionSelectionStrategy {

	RANDOM("random"), MINIMUM("minimum"), ORDERED("ordered");

	private String name;

	private KafkaPartitionSelectionStrategy(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
