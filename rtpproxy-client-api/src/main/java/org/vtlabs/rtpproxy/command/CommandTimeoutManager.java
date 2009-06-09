/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.command;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mhack
 */
public class CommandTimeoutManager {

    public static final long DEFAULT_COMMAND_TIMEOUT = 2000;
    private Logger log = LoggerFactory.getLogger(CommandTimeoutManager.class);
    private HashMap<String, Command> pendingCommandMap;
    private HashMap<Command, CommandTimeoutTask> pendingTimeoutTaskMap;
    private List<CommandListener> listeners;
    private ScheduledThreadPoolExecutor executor;
    private long commandTimeout;

    public CommandTimeoutManager(ScheduledThreadPoolExecutor executor) {
        this(executor, DEFAULT_COMMAND_TIMEOUT);
    }

    public CommandTimeoutManager(ScheduledThreadPoolExecutor executor,
            long commandTimeout) {
        this.executor = executor;
        this.commandTimeout = commandTimeout;
        pendingCommandMap = new HashMap<String, Command>();
        pendingTimeoutTaskMap = new HashMap<Command, CommandTimeoutTask>();
    }

    public void addPendingCommand(Command command) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Adding timeout for command ");
            sb.append(command);
            log.debug(sb.toString());
        }

        CommandTimeoutTask timeoutTask = new CommandTimeoutTask(command, this);

        synchronized (pendingCommandMap) {
            pendingCommandMap.put(command.getCookie(), command);
        }

        synchronized (pendingTimeoutTaskMap) {
            pendingTimeoutTaskMap.put(command, timeoutTask);
            executor.schedule(timeoutTask, commandTimeout,
                    TimeUnit.MILLISECONDS);
        }
    }

    public Command removePendingCommand(String cookie) {
        Command command;

        synchronized (pendingCommandMap) {
            command = pendingCommandMap.remove(cookie);
        }

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Removing timeout for ");
            sb.append(command);
            log.debug(sb.toString());
        }

        synchronized (pendingTimeoutTaskMap) {
            CommandTimeoutTask task = pendingTimeoutTaskMap.remove(command);
            executor.remove(task);
        }

        return command;
    }

    public void addListener(CommandListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeListener(CommandListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
}
