package org.vtlabs.rtpproxy.timeout;

import org.vtlabs.rtpproxy.command.*;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mhack
 */
public class TimeoutManager {

    private Logger log = LoggerFactory.getLogger(TimeoutManager.class);
    /**
     * Map to hold 'cookie' -> 'command' maps.
     */
    private HashMap<String, Command> pendingCommandMap;
    /**
     * Map to hold 'command' -> 'timeout task future object' maps.
     */
    private HashMap<Command, ScheduledFuture<TimeoutTask>> timeoutFutureMap;
    private ScheduledThreadPoolExecutor executor;
    private long commandTimeout;

    public TimeoutManager(ScheduledThreadPoolExecutor executor,
            long commandTimeout) {
        this.executor = executor;
        this.commandTimeout = commandTimeout;
        pendingCommandMap = new HashMap<String, Command>();
        timeoutFutureMap = new HashMap<Command, ScheduledFuture<TimeoutTask>>();
    }

    public void addCommand(Command command) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Adding timeout for command ");
            sb.append(command);
            log.debug(sb.toString());
        }

        addPendingCommand(command);


        TimeoutTask timeoutTask = new TimeoutTask(command, this);
        synchronized (timeoutFutureMap) {
            ScheduledFuture<TimeoutTask> future =
                    (ScheduledFuture<TimeoutTask>) executor.schedule(
                    timeoutTask,
                    commandTimeout,
                    TimeUnit.MILLISECONDS);

            timeoutFutureMap.put(command, future);
        }
    }

    public Command removeCommand(String cookie) {
        Command command = removePendingCommand(cookie);

        if (command != null) {
            cancelTimeout(command);

        } else {
            StringBuilder sb = new StringBuilder("Command not found for ");
            sb.append("cookie \'").append(cookie).append("\'");
            log.warn(sb.toString());
        }

        return command;
    }

    private void addPendingCommand(Command command) {
        synchronized (pendingCommandMap) {
            pendingCommandMap.put(command.getCookie(), command);
        }
    }

    private Command removePendingCommand(String cookie) {
        Command command;
        synchronized (pendingCommandMap) {
            command = pendingCommandMap.remove(cookie);
        }
        return command;
    }

    private void cancelTimeout(Command command) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Canceling timeout for ");
            sb.append(command);
            log.debug(sb.toString());
        }

        ScheduledFuture<TimeoutTask> future;
        synchronized (timeoutFutureMap) {
            future = timeoutFutureMap.remove(command);
        }

        if (future.cancel(false)) {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Timeout sucessful ");
                sb.append("canceled for command ").append(command);
                log.debug(sb.toString());
            }
        } else {
            StringBuilder sb = new StringBuilder("Timeout couldn't be ");
            sb.append("canceled for command").append(command);
            log.warn(sb.toString());
        }
    }

    public void terminate() {
        cleanPendingCommandMap();
        cleanTimeoutFutureMap();
        executor = null;
    }

    private void cleanPendingCommandMap() {
        synchronized (pendingCommandMap) {
            pendingCommandMap.clear();
        }
    }

    private void cleanTimeoutFutureMap() {
        synchronized (timeoutFutureMap) {
            timeoutFutureMap.clear();
        }
    }
}
