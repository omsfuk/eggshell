package cn.omsfuk.springboot.demo.service;

import cn.omsfuk.springboot.demo.service.support.Client;
import cn.omsfuk.springboot.demo.service.support.Server;
import org.springframework.stereotype.Service;

/**
 * Talk is cheap. Show me the code
 * 多说无益，代码上见真章
 * -------  by omsfuk  2017/8/1
 */

@Service
public class ServerService {

    private Server server;

    private Client client;

    public boolean getServerStatus() {
        if (server != null) {
            return server.terminated;
        }
        return true;
    }

    public void startServer(int bPort, int sPort) {
        server = new Server(bPort, sPort);
    }

    public void stopServer() {
        if (server != null) {
            server.stop();
        }
    }

    public void startClient(String tomcatHost, int tomcatPort,
                            String serverHost, int serverPort,
                            String schedulerHost, int schedulerPort) {
        client = new Client(tomcatHost, tomcatPort, serverHost, serverPort, schedulerHost, schedulerPort);
    }
}
