package com.lib.dal.entities;

import com.lib.xml.XmlReader;
import com.lib.xml.XmlTag;

import java.util.List;
import java.util.Map;

public class DBConfig {
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
        init(serverName, instanceName, databaseName, userName, password, port, integratedSecurity);
    }

    public DBConfig(String xmlFilePath) throws Exception {
        XmlReader reader = new XmlReader(xmlFilePath);

        Map<String, List<String>> data = reader.getXMLData(
                new XmlTag("ConnectionString"),
                new XmlTag("ServerName"),
                new XmlTag("InstanceName"),
                new XmlTag("DatabaseName"),
                new XmlTag("Username"),
                new XmlTag("Password"),
                new XmlTag("Port"),
                new XmlTag("IntegratedSecurity")
        );

        List<String> values = data.values().stream().findFirst().get();

        init(
            values.get(0),
            values.get(1),
            values.get(2),
            values.get(3),
            values.get(4),
            Integer.parseInt(values.get(5)),
            Boolean.parseBoolean(values.get(6))
        );
    }

    private void init(String serverName, String instanceName, String databaseName, String userName, String password, int port, boolean integratedSecurity)
    {
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

    public boolean getIntegratedSecurity() {
        return integratedSecurity;
    }
}
