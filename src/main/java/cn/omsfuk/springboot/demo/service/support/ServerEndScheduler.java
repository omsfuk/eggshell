package cn.omsfuk.springboot.demo.service.support;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Talk is cheap. Show me the code
 * 多说无益，代码上见真章
 * -------  by omsfuk  2017/7/26
 */
public class ServerEndScheduler {

    private Socket scheduler;

    private BufferedWriter writer;

    private ServerSocket clientAcceptor;

    public ServerEndScheduler(Socket scheduler) {
        this.scheduler = scheduler;
        try {
            this.writer = new BufferedWriter(new OutputStreamWriter(scheduler.getOutputStream()));
            clientAcceptor = new ServerSocket(65521);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket pullSocket() {
        try {
            writer.write(Message.NEW_SOCKET + "\r\n");
            writer.flush();
            return clientAcceptor.accept();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
