package com.owitho.www.utils;

public class ConstantUtils {

	// environment configure
	public static final String CONFIG_PATH = "cfgPath";// configure file's path
	public static final String ENV = "env";// concrete environment
	public static final String CFG_KAFKA = "kafka.properties";// KAFKA configure
	public static final String CFG_MYSQL = "mysql.properties";// MySQL configure
	public static final String CFG_COMMON = "common.properties";// MySQL configure

	// parser MySQL BINLOG
	public static final String QUERY = "QUERY";// query operation
	public static final String TABLE_MAP = "TABLE_MAP";// table's message
	public static final String EXT = "EXT";// validate concrete operation

	// read MySQL property -- host INFOS
	public static final String MYSQL_HOST_NAME = "mysql.host.name";// host
	public static final String MYSQL_HOST_PORT = "mysql.host.port";// port
	public static final String MYSQL_HOST_USER = "mysql.host.username";// user name
	public static final String MYSQL_HOST_PASSWD = "mysql.host.password";// user password

	// read MySQL property -- log INFOS
	public static final String MYSQL_LOG_FILENAME = "mysql.log.filename";// log filename
	public static final String MYSQL_LOG_POSITION = "mysql.log.position";// log file's offset

	// read KAFKA property
	public static final String KAFKA_TOPIC_NAME = "kafka.topic.name";// KAFKA topic
	public static final String KAFKA_PRODUCER_SERVICE = "kafka.producer.service";// KAFKA producer services
	public static final String KAFKA_PRODUCER_RETRIES = "kafka.producer.retries";// KAFKA producer retries
	public static final String KAFKA_PRODUCER_ACKS = "kafka.producer.acks";// KAFKA ACK
	public static final String KAFKA_PRODUCER_BATCH_SIZE="kafka.producer.batch.size";// batch size
	public static final String KAFKA_PRODUCER_BUFFER_MEMEORY="kafka.producer.buffer.memory";
	
	// read common property
	public static final String COMMON_IS_DEBUG="is.debug";
	public static final String COMMON_TIMER_DELAY="timer.delay.time";
}
