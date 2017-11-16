package com.changhong.db.arango;

import java.util.List;

/**
 * Created by redred on 2017/9/7.
 * email:zhyx2014@yeah.net
 */
public class ArangoConfig {
    private String host;
    private int port;
    private String userName;
    private String password;
    private List<String> dbNames;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getDbNames() {
        return dbNames;
    }

    public void setDbNames(List<String> dbNames) {
        this.dbNames = dbNames;
    }
}
