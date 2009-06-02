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
public class CommandManager {
    private HashMap<String, Command> pendingCommandMap;
    private List<CommandListener> listeners;
    
    public CommandManager() {
        pendingCommandMap = new HashMap<String, Command>();
    }

    public void addPendingCommand(Command c) {
        // TODO add command timeout

        synchronized (pendingCommandMap) {
            pendingCommandMap.put(c.getCookie(), c);
        }
    }

    public Command removePendingCommand(String cookie) {
        // TODO remove command timeout

        synchronized (pendingCommandMap) {
            return pendingCommandMap.remove(cookie);
        }
    }

    public void addListener(CommandListener l) {
        synchronized(listeners) {
            listeners.add(l);
        }
    }

    public void removeListener(CommandListener l) {
        synchronized(listeners) {
            listeners.remove(l);
        }
    }
}
