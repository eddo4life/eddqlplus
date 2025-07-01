package model;

import java.io.Serializable;

public class ConnectingToolsModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String dbName = "", port = "", password = "", portNme = "", host = "";
//password was initiaized with eddo


    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public String toString() {
        return "ConnectingToolsModel [dbName=" + dbName + ", port=" + port + ", password=" + password + ", portNme="
                + portNme + ", host=" + host + "]";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPortNme() {
        return portNme;

    }

    public void setPortNme(String portNme) {
        this.portNme = portNme;
    }

    public ConnectingToolsModel(String host, String portNme, String port, String password, String dbName) {
        this.port = port;
        this.password = password;
        this.portNme = portNme;
        this.host = host;
        this.dbName = dbName;
    }

    public ConnectingToolsModel() {
    }

}
