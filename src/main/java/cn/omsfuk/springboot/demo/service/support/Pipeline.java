package cn.omsfuk.springboot.demo.service.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * 定向流动管道
 *
 * Talk is cheap. Show me the code
 * 多说无益，代码上见真章
 * -------  by omsfuk  2017/7/26
 */

public class Pipeline implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(Pipeline.class);

    private Socket source;

    private Socket target;

    private String sourceName;

    private String targetName;

    public Pipeline(Socket source, Socket target, String sourceName, String targetName) {
        this.source = source;
        this.target = target;
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = source.getInputStream();
            OutputStream outputStream = target.getOutputStream();
            byte[] bytes = new byte[1024 * 16];
            int t = -1;

            while ((t = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, t);
                outputStream.flush();
            }

            source.shutdownInput();
            target.shutdownOutput();
            if (source.isOutputShutdown()) {
                source.close();
                logger.debug("close socket [{}]", sourceName);
            }

            if (target.isInputShutdown()) {
                target.close();
                logger.debug("close socket [{}]", targetName);
            }

            logger.debug("finish pipeline from [{}] to [{}]", sourceName, targetName);
        } catch (IOException e) {
            logger.error("crashed, nested exception is ", e);
        }
    }
}