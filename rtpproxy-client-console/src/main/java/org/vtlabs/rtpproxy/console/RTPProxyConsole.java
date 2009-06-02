package org.vtlabs.rtpproxy.console;

import java.io.Console;
import org.apache.log4j.Logger;
import org.vtlabs.rtpproxy.client.UDPService;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxyConsole {
    private static Logger logger = Logger.getLogger(RTPProxyConsole.class);

    public static void main(String[] args) {
        try {
            Thread.currentThread().setName("UDPServerConsole");
            
            UDPService udpServer = new UDPService();
            udpServer.start();

            Console console = System.console();
            String command = "";
            while (!command.equals("exit")) {
                console.printf("udpserver> ");
                command = console.readLine();

                if (!(command.equals("") || command.equals("exit"))) {
                    logger.debug("Command read: " + command);
                    udpServer.send(command);
                }
            }

        } catch (Exception e) {
            logger.error("Unknow error running UDPServer", e);
        }
    }
}
