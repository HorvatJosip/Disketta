package com.lib.dal.entities;

public class DBConfig {

    //{0} - server name
    //{1} - instance name
    //{2} - port number
    //user
    //password
    //databaseName
    //integratedSecurity
    private String serverName;
    private String databaseName;
    private String instanceName;
    private String userName;
    private String password;
    private int port = 1433;
    private boolean integratedSecurity = true;

    public DBConfig(String serverName, String instanceName, String databaseName, String userName, String password) {
        this.serverName = serverName;
        this.instanceName = instanceName;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
    }

    public DBConfig(String serverName, String instanceName, String databaseName, String userName, String password, int port, boolean integratedSecurity) {
        this.serverName = serverName;
        this.instanceName = instanceName;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
        this.port = port;
        this.integratedSecurity = integratedSecurity;
    }

    public int getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getPassword() {
        return password;
    }

    public String getServerName() {
        return serverName;
    }

    public String getUserName() {
        return userName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public boolean getIntegratedSecurity(){
        return integratedSecurity;
    }
}
