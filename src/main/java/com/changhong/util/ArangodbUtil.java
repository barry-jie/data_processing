package com.changhong.util;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.util.MapBuilder;

/**
 * Created by tangjuan on 2017/6/12.
 */
public class ArangodbUtil {

    private static final String urlDouban = "10.9.201.197";
    private static final Integer portDouban = 8529;
    private static final String dbNameDouban = "knowledge-graph-test";
    private static final String userNameDouban = "";
    private static final String passwordDouban = "";

    private static ArangoDB arangoDBDouban;

    private static ArangoDB arangoDBMtime;

    private static ArangoDB arangoDBZujian;


    public static ArangoDatabase getDbDouban() {
        arangoDBDouban = new ArangoDB.Builder().host(urlDouban, portDouban).user(userNameDouban).password(passwordDouban).build();

        //判断database是否已经存在，不存在就新创建
        if (!arangoDBDouban.getDatabases().contains(dbNameDouban)) {
            arangoDBDouban.createDatabase(dbNameDouban);
        }
        return arangoDBDouban.db(dbNameDouban);
    }

    public static void shutDownDouban() {
        if (arangoDBDouban != null) {
            arangoDBDouban.shutdown();
        }
    }

    public static void shutDownTime() {

        if (arangoDBMtime != null) {
            arangoDBMtime.shutdown();
        }
    }

    public static void shutDownZujian() {
        if (arangoDBZujian != null) {
            arangoDBZujian.shutdown();
        }
    }
  
}
