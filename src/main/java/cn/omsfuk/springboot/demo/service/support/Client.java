package cn.omsfuk.springboot.demo.service.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Talk is cheap. Show me the code
 * -------  by omsfuk  2017/7/25
 */
public class Client {

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    private String tomcatHost = "localhost";

    private int tomcatPort = 8080;

    private String serverHost = "knife037.cn";

    private int serverPort = 65521;

    private String schedulerHost = "knife037.cn";

    private int schedulerPort = 65522;

    private ClientEndScheduler scheduler;


    public Client(String tomcatHost, int tomcatPort, String serverHost, int serverPort, String schedulerHost, int schedulerPort) {
        this.tomcatHost = tomcatHost;
        this.tomcatPort = tomcatPort;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.schedulerHost = schedulerHost;
        this.schedulerPort = schedulerPort;

        prepare();
        start();
    }

    public void start() {
        scheduler.runSync();
    }

    public void stop() {

    }

    public void prepare() {
        initScheduler();
    }

    public void initScheduler() {
        scheduler = new ClientEndScheduler(serverHost, schedulerPort,
                SocketFactory.SocketFactoryBuilder.buildClientSocketFactory(
                        tomcatHost, tomcatPort, serverHost, serverPort, schedulerHost, schedulerPort));
    }

    public static void main(String[] args) {
        if (args.length % 2 == 1) {
            throw new IllegalArgumentException("Wrong parameter format");
        }
        CmdArg[] cmdArgs = new CmdArg[args.length / 2];
        for (int i = 0; i < args.length; i+= 2) {
            cmdArgs[i / 2] = new CmdArg(args[i], args[i + 1]);
        }
        String tomcatHost = "localhost";

        int tomcatPort = 8080;

        String serverHost = "knife037.cn";

        int serverPort = 65521;

        String schedulerHost = "knife037.cn";

        int schedulerPort = 65522;

        for (CmdArg cmdArg : cmdArgs) {
            if (cmdArg.cmd.equals("--port-tomcat")) {
                tomcatPort = Integer.parseInt(cmdArg.argument);
            } else if (cmdArg.cmd.equals("--port-server")) {
                serverPort = Integer.parseInt(cmdArg.argument);
            } else if (cmdArg.cmd.equals("--port-scheduler")) {
                schedulerPort = Integer.parseInt(cmdArg.argument);
            } else if (cmdArg.cmd.equals("--host-tomcat")) {
                tomcatHost = cmdArg.argument;
            } else if (cmdArg.cmd.equals("--host-server")) {
                serverHost = cmdArg.argument;
            } else if (cmdArg.cmd.equals("--host-scheduler")) {
                schedulerHost = cmdArg.argument;
            } else {
                System.out.println("参数格式错误");
                return ;
            }
        }
        new Client(tomcatHost, tomcatPort, serverHost, serverPort, schedulerHost, schedulerPort);
    }

}
