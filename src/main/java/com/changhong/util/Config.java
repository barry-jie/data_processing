package com.changhong.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Config {

	private static Logger logger = Logger.getLogger(Config.class);
	
	public static String redisHost = "";
	public static int redisPort = 6379;
	public static String redisPassword = "";
	public static int redisSelect = 0;
		
	public static String mongoHost = "";
	public static int mongoPort = 27017;
	public static String mongoUser = "";
	public static String mongoPassword = "";
	
	static {
		InputStream is = null;
		try {
			Properties prop = new Properties();
			prop.load(Config.class.getResourceAsStream("/config.ini"));
			
			redisSelect = Integer.parseInt(prop.getProperty("redis_select").toString());
			
			redisHost = prop.getProperty("redis_host", "127.0.0.1").toString();
			redisPort = Integer.parseInt(prop.getProperty("redis_port", "6379").toString());
			redisPassword = prop.getProperty("redis_password");
			mongoHost = prop.getProperty("mongo_host", "127.0.0.1").toString();
			mongoPort = Integer.parseInt(prop.getProperty("mongo_port", "27017").toString());
			mongoUser = prop.getProperty("mongo_user");
			mongoPassword = prop.getProperty("mongo_password");
		} catch (IOException e) {
			logger.info("加载配置文件出错",e);
		} finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static void main(String[] args) {
	}

}
