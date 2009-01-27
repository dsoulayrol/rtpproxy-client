package udpserver;

import java.io.Console;
import org.apache.log4j.Logger;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class UDPServerConsole {
    private static Logger logger = Logger.getLogger(UDPServerConsole.class);

    public static void main(String[] args) {
        try {
            Thread.currentThread().setName("UDPServerConsole");
            
            UDPServer udpServer = new UDPServer();
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
