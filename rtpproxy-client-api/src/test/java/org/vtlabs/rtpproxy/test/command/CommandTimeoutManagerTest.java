/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.test.command;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.Ignore;

/**
 *
 * @author mhack
 */
@Ignore
public class CommandTimeoutManagerTest {

    protected class ScheduledThreadPoolMOCK
            extends ScheduledThreadPoolExecutor {

        public boolean wasScheduled;
        public boolean wasRemoved;
        public long scheduledTimeout;
        public TimeUnit timeoutUnit;

        public ScheduledThreadPoolMOCK(int poolSize) {
            // MOCK doesn't need more threads to do .. nothing :)
            super(1);
            wasScheduled = false;
            wasRemoved = false;
        }

        @Override
        public boolean remove(Runnable task) {
            return wasRemoved = true;
        }

        @Override
        public ScheduledFuture<?> schedule(Runnable task, long timeout,
                TimeUnit unit) {
            wasScheduled = true;
            scheduledTimeout = timeout;
            timeoutUnit = unit;
            return null;
        }
    }
}
