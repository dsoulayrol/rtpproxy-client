/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.stress;

import java.util.UUID;
import org.apache.log4j.Logger;
import org.vtlabs.rtpproxy.client.RTPProxyClient;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.client.RTPProxySession;

/**
 *
 * @author marcoshack
 */
public class StressTask implements Runnable, RTPProxyClientListener {
    private RTPProxyClient client;
    private Statistics stats;
    private int taskID;
    private static Logger log = Logger.getLogger(StressTask.class);

    public StressTask(RTPProxyClient client, Statistics stats, int taskID) {
        this.client = client;
        this.stats = stats;
        this.taskID = taskID;
    }

    public void run() {
        log.info(new StringBuilder("Running task ").append(taskID));

        try {
            client.createSession(UUID.randomUUID().toString(), null, this);
            stats.incCreateRequest();

        } catch (Exception e) {
            log.error("Error creating RTPProxy session", e);
            stats.incCreateFail();
        }
    }

    public void sessionCreated(RTPProxySession session, Object appData) {
        log.info(new StringBuilder("Session created on task ").append(taskID)
                .append(": ").append(session));

        stats.incCreateSucess();

        try {
            client.updateSession(session, null, this);
            stats.incUpdateRequest();

        } catch (Exception e) {
            log.error(new StringBuilder("Fail updating session ")
                    .append(session), e);
            stats.incUpdateFail();
        }
    }

    public void sessionUpdated(RTPProxySession session, Object appData) {
        log.info(new StringBuilder("Session updated on task ").append(taskID)
                .append(": ").append(session));

        stats.incUpdateSucess();

        try {
            client.destroySession(session, null, this);
            stats.incDestroyRequest();

        } catch (Exception e) {
            log.error(new StringBuilder("Fail destroying session ")
                    .append(session), e);
            stats.incDestroyFail();
        }
    }

    public void sessionDestroyed(RTPProxySession session, Object appData) {
        log.info(new StringBuilder("Session destroyed on task ").append(taskID)
                .append(": ").append(session));

        stats.incDestroySucess();
    }

    public void createSessionTimeout(String sessionID, Object appData) {
        log.error(new StringBuilder("Session creation timeout on task ")
                .append(taskID).append(": sessionID = ").append(sessionID));

        stats.incCreateTimeout();
    }

    public void createSessionFailed(String sessionID, Object appData,
            Throwable t) {
        log.error(new StringBuilder("Session creation failed on task ")
                .append(taskID).append(": ").append(sessionID), t);

        stats.incCreateFail();
    }

    public void updateSessionTimeout(RTPProxySession session, Object appData) {
        log.error(new StringBuilder("Session creation timeout on task ")
                .append(taskID).append(": ").append(session));

        stats.incUpdateTimeout();
    }

    public void updateSessionFailed(RTPProxySession session, Object appData,
            Throwable t) {
        log.error(new StringBuilder("Session update failed on task ")
                .append(taskID).append(": ").append(session), t);

        stats.incUpdateFail();
    }

    public void destroySessionTimeout(RTPProxySession session, Object appData) {
        log.error(new StringBuilder("Session destruction timeout on task ")
                .append(taskID).append(": ").append(session));
        
        stats.incDestroyTimeout();
    }

    public void destroySessionFailed(RTPProxySession session, Object appData,
            Throwable t) {
        log.error(new StringBuilder("Session destruction failed on task ")
                .append(taskID).append(": ").append(session), t);
        
        stats.incDestroyFail();
    }
}
