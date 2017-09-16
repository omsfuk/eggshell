package cn.omsfuk.springboot.demo.service.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/25
 */
public class Server {

    private Logger logger = LoggerFactory.getLogger(Server.class);

    private int browserPort = 65520;

    private int schedulerPort = 65522;

    private Thread serverThread;

    /**
     * 调度器
     */
    private ServerEndScheduler scheduler;

    /**
     * 接受浏览器的连接
     */
    private ServerSocket browserAcceptor;

    private ServerSocket schedulerAcceptor;

    private ExecutorService executors;

    private boolean stop;

    public boolean terminated = true;

    public Server(int browserPort, int schedulerPort) {
        this.browserPort = browserPort;
        this.schedulerPort = schedulerPort;
        prepare();
        start();
    }

    public void run() {
        initScheduler();
        Socket client = null;
        try {
            browserAcceptor = new ServerSocket(browserPort);
            while (!stop && (client = browserAcceptor.accept()) != null) {
                logger.debug("accept a new socket");
                new Channel(client);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            terminated = true;
        }
    }

    public void start() {
        logger.info("bind port {}", browserPort);
        terminated = false;
        serverThread = new Thread(this::run);
        serverThread.start();
    }

    public void stop() {
        terminated = true;
        serverThread.stop();
        try {
            if (browserAcceptor != null) {
                browserAcceptor.close();
            }
            if (schedulerAcceptor != null) {
                schedulerAcceptor.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void prepare() {
        this.executors = Executors.newFixedThreadPool(20);
    }

    public void initScheduler() {
        logger.info("init scheduler");
        try {
            schedulerAcceptor = new ServerSocket(schedulerPort);
            scheduler = new ServerEndScheduler(schedulerAcceptor.accept());
            logger.info("init scheduler success");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class Channel {

        private Socket client;

        public Channel(Socket browserClient) {
            logger.debug("trying to pull a socket {}", browserClient);
            this.client = scheduler.pullSocket();
            logger.debug("pull a socket successfully {}", browserClient);

            try {
                executors.execute(new Pipeline(browserClient, client, "browser", "client"));
                executors.execute(new Pipeline(client, browserClient, "client", "browser"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length % 2 == 1) {
            throw new IllegalArgumentException("Wrong parameter format");
        }
        CmdArg[] cmdArgs = new CmdArg[args.length / 2];
        for (int i = 0; i < args.length; i+= 2) {
            cmdArgs[i / 2] = new CmdArg(args[i], args[i + 1]);
        }
        int browserPort = 65520;
        int schedulerPort = 65522;
        for (CmdArg cmdArg : cmdArgs) {
            if (cmdArg.cmd.equals("--port-browser") || cmdArg.cmd.equals("-p1")) {
                browserPort = Integer.parseInt(cmdArg.argument);
            } else if (cmdArg.cmd.equals("--port-scheduler") || cmdArg.cmd.equals("-p2")) {
                schedulerPort = Integer.parseInt(cmdArg.argument);
            }
        }
        Server server = new Server(browserPort, schedulerPort);
    }
}
