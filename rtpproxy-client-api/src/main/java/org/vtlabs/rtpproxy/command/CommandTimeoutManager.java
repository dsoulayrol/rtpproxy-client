/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.command;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author mhack
 */
public class CommandTimeoutManager {
    private HashMap<String, Command> pendingCommandMap;
    private List<CommandListener> listeners;
    
    public CommandTimeoutManager() {
        pendingCommandMap = new HashMap<String, Command>();
    }

    public void addPendingCommand(Command command) {
        // TODO add command timeout using java.util.concurrent.Executor.

        synchronized (pendingCommandMap) {
            pendingCommandMap.put(command.getCookie(), command);
        }
    }

    public Command removePendingCommand(String cookie) {
        // TODO remove command timeout

        synchronized (pendingCommandMap) {
            return pendingCommandMap.remove(cookie);
        }
    }

    public void addListener(CommandListener listener) {
        synchronized(listeners) {
            listeners.add(listener);
        }
    }

    public void removeListener(CommandListener listener) {
        synchronized(listeners) {
            listeners.remove(listener);
        }
    }
}
