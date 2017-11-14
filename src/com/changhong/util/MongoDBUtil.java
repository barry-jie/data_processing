package com.changhong.util;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtil {
	private static MongoClientOptions options;
	private static MongoClient mongoClient = null;
	private MongoDBUtil() {}

	static {
		synchronized (MongoDBUtil.class) {
			initDBPrompties();
		}
	}

	public static MongoCollection<Document> getCollection(String dbName,String collName) {
		MongoDatabase db = mongoClient.getDatabase(dbName);
		return db.getCollection(collName);
	}
	
	public static MongoCollection<Document> getDBCollection(String dbName,String collName) {
		return mongoClient.getDatabase(dbName).getCollection(collName);
	}
	
	 /** 
	 * 初始化连接池 
	 */  
	private static void initDBPrompties() {  
		try { 
			if(StringUtils.isBlank(Config.mongoUser) || StringUtils.isBlank(Config.mongoPassword)){
				mongoClient = new MongoClient(Config.mongoHost, Config.mongoPort);
			}
		} catch (MongoException e) {  
			e.printStackTrace();
		}
    }
	
	public static void main(String[] args) {
		
	}
	
}