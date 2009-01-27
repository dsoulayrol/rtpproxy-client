package udpserver;

import java.io.Console;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import org.apache.log4j.Logger;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class UDPServer implements Runnable {

    private static final int DEFAULT_BIND_PORT = 9999;
    private static Logger logger = Logger.getLogger(UDPServer.class);
    private InetAddress bindAddress;
    private int bindPort;
    private InetAddress remoteAddress;
    private int remotePort;
    private Boolean isRunning;
    private DatagramChannel channel;
    private Thread udpServerThread;

    public UDPServer() throws IOException {
        bindAddress = InetAddress.getLocalHost();
        bindPort = DEFAULT_BIND_PORT;
        isRunning = new Boolean(true);

        channel = DatagramChannel.open();
        SocketAddress address = new InetSocketAddress(bindAddress, bindPort);
        channel.socket().bind(address);
    }

    public void start() {
        logger.info("Starting UDPServer " + bindAddress + ":" + bindPort);

        Thread thread = new Thread(this);
        thread.setName("UDPServerThread");
        thread.start();

        logger.info("UDPServer started");
    }

    public void stop() {
        logger.info("Stopping UDPServer");

        synchronized(udpServerThread) {
            udpServerThread.interrupt();
        }

        logger.info("UDPServer stopped");
    }


    public void send(String command) throws IOException {
        logger.debug("Sending command \"" + command + "\"");
        SocketAddress server = new InetSocketAddress("127.0.0.1", 22222);

        // Create ByteBuffer output buffer with the command content
        ByteBuffer out = ByteBuffer.allocate(512);
        out.order(ByteOrder.nativeOrder());
        out.put(command.concat("\r\n").getBytes());

        out.flip();
        channel.send(out, server);
    }

    
    public void run() {
        try {
            ByteBuffer inBuffer = ByteBuffer.allocateDirect(1024);

            while (isRunning) {
                inBuffer.clear();
                logger.debug("Waiting for packet input");
                SocketAddress client = channel.receive(inBuffer);

                // flip and convert ByteBuffer to String
                inBuffer.flip();
                byte[] byteArray = new byte[inBuffer.remaining()];
                inBuffer.get(byteArray);
                String s = new String(byteArray).replaceAll("\n", ".");
                logger.debug(client + " said: " + s);
            }
            
        } catch (ClosedByInterruptException interruptEx) {
            logger.debug(Thread.currentThread().getName() + " interruped.");

        } catch (Exception e) {
            logger.error("Error running UDPServer", e);

        } finally {
            logger.debug("UDPServer loop terminated");
        }
    }
}
