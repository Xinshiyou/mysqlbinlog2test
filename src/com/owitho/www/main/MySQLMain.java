package com.owitho.www.main;

import java.io.IOException;
import java.util.BitSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.BinaryLogClient.EventListener;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.QueryEventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.owitho.www.entity.DataEntity;
import com.owitho.www.enums.OperationType;
import com.owitho.www.utils.ConstantUtils;
import com.owitho.www.utils.PropertiesUtil;

/**
 * @DESC 主程序入口：解析响应MySQL Binlog，并封装为JSON，并发送到Kafka
 * @author xinshiyou
 */
public class MySQLMain extends AbstractMain {

	private static Timer scheduler = new Timer();
	static {
		try {
			delayTime = Integer.valueOf(PropertiesUtil.getCommonProperty(ConstantUtils.COMMON_TIMER_DELAY));
		} catch (IOException e) {
			logger.error("Initalized delay time parameter failed!", e);
			delayTime = 30 * 1000;
		}
	}

	public static void main(String[] args) throws IOException {

		/** initialize BinaryClient */
		final BinaryLogClient client = getClient();
		if (null == client) {
			logger.error("Initialize BinaryLogClient failed!");
			return;
		}

		client.registerEventListener(new EventListener() {

			private DataEntity entity = null;
			private boolean flag = false;

			@Override
			public void onEvent(Event event) {

				String type = event.getHeader().getEventType().name();
				logger.debug("Get event message :" + event);

				if (null != type && !flag) {

					if (null == entity && type.equals(ConstantUtils.QUERY)) {

						entity = new DataEntity();
						if (null != event.getData()) {
							QueryEventData qed = event.getData();
							String sql = qed.getSql();
							if (!"BEGIN".equals(sql)) {
								entity.dbName = qed.getDatabase();
								entity.sql = sql;
								entity.type = OperationType.SQL;
								flag = true;
							}
						}

					} else if (null != entity && type.equals(ConstantUtils.TABLE_MAP)) {

						TableMapEventData tme = event.getData();
						entity.dbName = tme.getDatabase();
						entity.tabName = tme.getTable();

					} else if (null != entity && type.startsWith(ConstantUtils.EXT)) {

						flag = true;
						BitSet bs = null;
						List<?> rows = null;

						EventData dt = event.getData();

						if (dt instanceof WriteRowsEventData) {

							WriteRowsEventData wre = event.getData();
							bs = wre.getIncludedColumns();
							rows = wre.getRows();
							entity.type = OperationType.INSERT;

						} else if (dt instanceof DeleteRowsEventData) {

							DeleteRowsEventData wre = event.getData();
							bs = wre.getIncludedColumns();
							rows = wre.getRows();
							entity.type = OperationType.DELETE;

						} else if (dt instanceof UpdateRowsEventData) {

							UpdateRowsEventData wre = event.getData();
							bs = wre.getIncludedColumns();
							rows = wre.getRows();
							entity.type = OperationType.UPDATE;

						}

						if (null != bs) {
							entity.cols = bs.toString();
							entity.rows = rows;
						}
					}
				}

				if (flag) {

					logger.debug("发送数据到Kafka-->" + entity.toString());
					try {
						send2Kafka(entity.toString(), null);
					} catch (IOException e1) {
						logger.error("send data to Kafka failed!", e1);
					}

					entity = null;
					flag = false;
				}
			}
		});

		/** Begin to Listen */
		client.connect();

		/** update mysql binlog offsets */
		scheduler.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					updateMySQLProperty(client);
				} catch (IOException e) {
					logger.error("Update data to property files failed!", e);
				}
			}
		}, delayTime);

		/** add shutdown hook to our thread */
		addHookListener(client, scheduler);
	}

}
