package org.vtlabs.rtpproxy.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.vtlabs.rtpproxy.command.Command;
import org.vtlabs.rtpproxy.command.CommandManager;
import org.vtlabs.rtpproxy.command.CreateSessionCommand;
import org.vtlabs.rtpproxy.udp.DatagramListener;
import org.vtlabs.rtpproxy.udp.DatagramService;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxyClient implements DatagramListener {

    /**
     * Default bind port used by UDP service if it isn't specified.
     */
    public static final int DEFAULT_BIND_PORT = 9876;

    private CommandManager commandManager;
    private DatagramService udpService;

    public RTPProxyClient() throws IOException {
        this(DEFAULT_BIND_PORT);
    }

    public RTPProxyClient(int bindPort) throws IOException {
        commandManager = createCommandManager();
        udpService = createDatagraService(bindPort, this);
    }

    /**
     * Create a new RTPProxy session.
     *
     * @param Application data object, will be passed as argument to callback
     *        methods.
     * @param Listener object to receive callback events.
     * @see RTPProxyClientListener
     */
    public void createSession(Object appData, RTPProxyClientListener listener) {
    }

    /**
     * Callback method of datagram responses.
     *
     * @param Command cookie
     * @param Response message
     * @param Datagram source address
     */
    public void processResponse(String cookie, String message,
            InetSocketAddress srcAddr) {
        Command command = commandManager.removePendingCommand(cookie);

        if (command instanceof CreateSessionCommand) {
            processCreateSessionResponse(command, message);
        }
    }

    protected void processCreateSessionResponse(Command command, String msg) {
    }

    /**
     * Factory method to create CommandManager.
     *
     * @return
     */
    protected CommandManager createCommandManager() {
        return new CommandManager();
    }

    /**
     * Factory method to create DatagramService.
     *
     * @return
     */
    protected DatagramService createDatagraService(int bindPort,
            DatagramListener listener) throws IOException {
        return new DatagramService(bindPort, listener);
    }
}
