package cn.omsfuk.springboot.demo.service.support;

import java.io.IOException;
import java.net.Socket;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/26
 */
public class SocketFactory {

    private int type = 0;

    private String tomcatHost;

    private int tomcatPort;

    private String serverHost;

    private int serverPort;

    private String schedulerHost;

    private int schedulerPort;

    private SocketFactory(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTomcatHost() {
        return tomcatHost;
    }

    public void setTomcatHost(String tomcatHost) {
        this.tomcatHost = tomcatHost;
    }

    public int getTomcatPort() {
        return tomcatPort;
    }

    public void setTomcatPort(int tomcatPort) {
        this.tomcatPort = tomcatPort;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getSchedulerHost() {
        return schedulerHost;
    }

    public void setSchedulerHost(String schedulerHost) {
        this.schedulerHost = schedulerHost;
    }

    public int getSchedulerPort() {
        return schedulerPort;
    }

    public void setSchedulerPort(int schedulerPort) {
        this.schedulerPort = schedulerPort;
    }

    public Socket buildTransitSocket() {
        try {
            return new Socket(serverHost, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Socket buildRequestSocket() {
        try {
            return new Socket(tomcatHost, tomcatPort);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class SocketFactoryBuilder {

        public static SocketFactory buildClientSocketFactory(String tomcatHost, int tomcatPort, String serverHost, int serverPort,
                                                             String schedulerHost, int schedulerPort) {
            SocketFactory factory = new SocketFactory(0);
            factory.setTomcatHost(tomcatHost);
            factory.setTomcatPort(tomcatPort);
            factory.setServerHost(serverHost);
            factory.setServerPort(serverPort);
            factory.setSchedulerHost(schedulerHost);
            factory.setSchedulerPort(schedulerPort);
            return factory;
        }
    }

}
