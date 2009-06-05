package org.vtlabs.rtpproxy.callback;

import java.net.InetSocketAddress;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.command.Command;
import org.vtlabs.rtpproxy.command.CommandListener;
import org.vtlabs.rtpproxy.command.CommandTimeoutManager;
import org.vtlabs.rtpproxy.command.UpdateCommand;
import org.vtlabs.rtpproxy.udp.DatagramListener;

/**
 * Handler of RTPProxy responses and notify RTPProxyClientListener.
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class CallbackHandler implements DatagramListener, CommandListener {

    private CommandTimeoutManager timeoutManager;

    public CallbackHandler(CommandTimeoutManager commandManager) {
        this.timeoutManager = commandManager;
    }

    /**
     * Callback method for received datagram messages (DatagramListener).
     *
     * @param Command cookie
     * @param Response message
     * @param Datagram source address
     */
    public void processResponse(String cookie, String message,
            InetSocketAddress srcAddr) {
        Command command = timeoutManager.removePendingCommand(cookie);

        if (command instanceof UpdateCommand) {
            UpdateCommand updateCommand = (UpdateCommand)command;
            
            if (updateCommand.getSession() == null) {
                processSessionCreated(command, message);
                
            } else {
                processSessionUpdated(command, message);
            }
        }
    }

    /**
     * 
     * @param command
     * @param msg
     */
    protected void processSessionCreated(Command command, String msg) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *
     * @param command
     * @param message
     */
    private void processSessionUpdated(Command command, String message) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    /**
     * Command timeout callback method (CommandListener).
     *
     * @param Command that reached the timeout period.
     */
    public void commandTimeout(Command command) {
        RTPProxyClientListener listener = command.getCallbackListener();
        listener.createSessionTimeout(command.getSessionID(),
                command.getAppData());
    }
}
