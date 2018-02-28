package com.owitho.www.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * @DESC read properties
 * @author xinshiyou
 */
public class PropertiesUtil {

	private static String PATH = null;

	// load system property and configure
	static {
		// configure path
		String path = getSystemProperty(ConstantUtils.CONFIG_PATH);
		if (null == path)
			path = "./config";

		// environment path
		String env = getSystemProperty(ConstantUtils.ENV);
		if (null == env)
			env = "local";

		// format path
		path = path + "/" + env;
		PATH = path;

		/** configure log4j */
		PropertyConfigurator.configure(path+"/log4j.property");

	}

	/**
	 * @DESC get KAFKA property
	 * @param key
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String getKafkaProperty(String key) throws FileNotFoundException, IOException {
		return getProperty(ConstantUtils.CFG_KAFKA, key);
	}

	/**
	 * @DESC get MySQL property
	 * @param key
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String getMySQLProperty(String key) throws FileNotFoundException, IOException {
		return getProperty(ConstantUtils.CFG_MYSQL, key);
	}

	/**
	 * @DESC get Common property
	 * @param key
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String getCommonProperty(String key) throws FileNotFoundException, IOException {
		return getProperty(ConstantUtils.CFG_COMMON, key);
	}

	/**
	 * @DESC MySQL property
	 * @param key
	 * @param value
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void update2MySQLProperty(String key, String value) throws FileNotFoundException, IOException {

		String path = PATH + "/" + ConstantUtils.CFG_MYSQL;
		Properties props = readProperty(path);
		props.put(key, value);
		writeProperty(path, props);
	}

	/**
	 * @DESC read system property
	 * @param name
	 */
	private static String getSystemProperty(String key) {
		String value = System.getProperty(key);
		if (value != null && value != "") {
			return value;
		}

		return null;
	}

	/**
	 * @DESC read property
	 * @param name
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static String getProperty(String path, String key) throws FileNotFoundException, IOException {

		// load properties every time
		Properties props = readProperty(PATH + "/" + path);
		return props.getProperty(key);
	}

	/**
	 * @DESC read property from input file
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static Properties readProperty(String path) throws FileNotFoundException, IOException {

		// load properties every time
		Properties props = new Properties();
		props.load(new FileInputStream(new File(path)));

		return props;
	}

	/**
	 * @DESC write property to file
	 * @param path
	 * @param props
	 * @throws IOException
	 */
	private static void writeProperty(String path, Properties props) throws IOException {

		OutputStream fos = new FileOutputStream(new File(path));
		props.store(fos,
				"update propertys" + new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(System.currentTimeMillis()));
		fos.flush();
		fos.close();
	}

}
