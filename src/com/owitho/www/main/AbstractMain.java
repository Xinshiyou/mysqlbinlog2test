package com.owitho.www.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.owitho.www.kafka.producer.KafkaProducerFactory;
import com.owitho.www.kafka.producer.OwithoKafkaProducer;
import com.owitho.www.utils.ConstantUtils;
import com.owitho.www.utils.PropertiesUtil;

public class AbstractMain {

	/**
	 * 总结:<br>
	 * 1、create or drop table:只有一个事件相应【 QueryEventData 】，里面可以看到SQL语句
	 * 2、insert,update,delete:多个事件组成，包括Query/Table_Map/EXT_XXX等事件组成
	 */

	// LOG
	public static final Logger logger = Logger.getLogger(MySQLMain.class);

	// basic parameter
	static int delayTime = 30 * 1000;

	/**
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @DESC generate BinaryLogClient
	 */
	public static BinaryLogClient getClient() throws FileNotFoundException, IOException {

		String mysqlHost = PropertiesUtil.getMySQLProperty(ConstantUtils.MYSQL_HOST_NAME);
		String mysqlHostPort = PropertiesUtil.getMySQLProperty(ConstantUtils.MYSQL_HOST_PORT);
		String username = PropertiesUtil.getMySQLProperty(ConstantUtils.MYSQL_HOST_USER);
		String password = PropertiesUtil.getMySQLProperty(ConstantUtils.MYSQL_HOST_PASSWD);

		BinaryLogClient client = new BinaryLogClient(mysqlHost, Integer.parseInt(mysqlHostPort), username, password);
		return setLogs(client);
	}

	/**
	 * @DESC log filename and log offset position
	 * @param client
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static BinaryLogClient setLogs(BinaryLogClient client) throws FileNotFoundException, IOException {

		String logFile = PropertiesUtil.getMySQLProperty(ConstantUtils.MYSQL_LOG_FILENAME);
		String logPosition = PropertiesUtil.getMySQLProperty(ConstantUtils.MYSQL_LOG_POSITION);

		logger.debug("LogFile:" + logFile);
		logger.debug("LogPos:" + logPosition);

		if (null != logFile && logFile.trim().length() > 0 && null != logPosition) {
			// set log file and log position
			client.setBinlogFilename(logFile);
			client.setBinlogPosition(Integer.parseInt(logPosition));
		}

		return client;
	}

	/**
	 * @DESC send message to KAFKA
	 * @param msg
	 * @param object
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void send2Kafka(String msg, Object object) throws FileNotFoundException, IOException {

		OwithoKafkaProducer producer = KafkaProducerFactory.getInstance();
		ProducerRecord<Integer, String> record = new ProducerRecord<Integer, String>(
				PropertiesUtil.getKafkaProperty(ConstantUtils.KAFKA_TOPIC_NAME),
				OwithoKafkaProducer.getIncrementAndGetKey(), msg);
		producer.send(record);

	}

	/**
	 * @DESC update log filename and log offset position
	 * @param client
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void updateMySQLProperty(BinaryLogClient client) throws FileNotFoundException, IOException {

		String filename = client.getBinlogFilename();
		String position = client.getBinlogPosition() + "";

		PropertiesUtil.update2MySQLProperty(ConstantUtils.MYSQL_LOG_FILENAME, filename);
		PropertiesUtil.update2MySQLProperty(ConstantUtils.MYSQL_LOG_POSITION, position);
	}

	/**
	 * @DESC add graceful shutdown listener
	 * @param client
	 */
	public static void addHookListener(final BinaryLogClient client, final Timer scheduler) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {

				try {
					scheduler.cancel();
				} catch (Exception e) {
					logger.error("Showdown Timer graceful failed!", e);
				}

				try {
					updateMySQLProperty(client);// TODO shutdown update or every
				} catch (IOException e) {
					logger.error("Showdown updateMySQLProperty graceful failed!", e);
				}

				try {
					KafkaProducerFactory.getInstance().close();
				} catch (Exception e) {
					logger.error("Showdown KafkaProducerFactory graceful failed!", e);
				}

			}
		});
	}

}
