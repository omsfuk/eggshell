package cn.omsfuk.springboot.demo.service.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Talk is cheap. Show me the code
 * 多说无益，代码上见真章
 * -------  by omsfuk  2017/7/27
 */
public class ClientEndScheduler implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ClientEndScheduler.class);

    private Socket scheduler;

    private SocketFactory factory;

    private ExecutorService executors;

    private String serverHost;


    private int schedulerPort = 65522;

    public ClientEndScheduler(String serverHost, int schedulerPort, SocketFactory factory) {
        this.serverHost = serverHost;
        this.schedulerPort = schedulerPort;
        this.factory = factory;
        executors = Executors.newFixedThreadPool(100);
        try {
            logger.info("init scheduler");
            System.out.println("serverHost" + serverHost);
            scheduler = new Socket(serverHost, schedulerPort);
            logger.info("init scheduler successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        logger.info("run client");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(scheduler.getInputStream()));
            String msg = null;
            while ((msg = reader.readLine()) != null) {
                if (Message.NEW_SOCKET.equals(msg)) {
                    Socket tomcat = factory.buildRequestSocket();
                    Socket server = new Socket(serverHost, 65521);
                    executors.execute(new Pipeline(server, tomcat, "server", "tomcat"));
                    executors.execute(new Pipeline(tomcat, server, "tomcat", "server"));
                }
            }
            executors.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scheduler != null) {
                try {
                    scheduler.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void runSync() {
        new Thread(this).start();
    }

}
