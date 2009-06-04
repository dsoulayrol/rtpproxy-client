/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.callback;

import java.net.InetSocketAddress;
import org.vtlabs.rtpproxy.command.Command;
import org.vtlabs.rtpproxy.command.CommandListener;
import org.vtlabs.rtpproxy.command.CommandTimeoutManager;
import org.vtlabs.rtpproxy.command.CreateSessionCommand;
import org.vtlabs.rtpproxy.udp.DatagramListener;

/**
 *
 * @author mhack
 */
public class CallbackHandler implements DatagramListener, CommandListener {

    private CommandTimeoutManager commandManager;

    public CallbackHandler(CommandTimeoutManager commandManager) {
        this.commandManager = commandManager;
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
     * Command timeout callback method.
     *
     * @param Command that reached the timeout period.
     */
    public void commandTimeout(Command c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
