/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.test;

import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vtlabs.rtpproxy.client.NoServerAvailableException;
import org.vtlabs.rtpproxy.client.RTPProxyClient;
import org.vtlabs.rtpproxy.client.RTPProxyClientConfig;
import org.vtlabs.rtpproxy.client.RTPProxyClientConfigException;
import org.vtlabs.rtpproxy.client.RTPProxyClientConfigurator;
import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.client.RTPProxySession;

/**
 *
 * @author mhack
 */
public class RTPProxyClientTest implements RTPProxyClientListener {

    private static Logger log;
    private RTPProxyClient client;
    private RTPProxyClientConfig config;
    private RTPProxySession session;

    public static void main(String[] args) {
        try {
            configureLog4j();
            log = LoggerFactory.getLogger(RTPProxyClientTest.class);
            
            RTPProxyClientTest test = new RTPProxyClientTest();
            test.run();

        } catch (Exception ex) {
            log.error("Unexpected error starting RTPProxyClientTest", ex);
        }
    }

    public RTPProxyClientTest() throws RTPProxyClientConfigException, IOException {
        config = RTPProxyClientConfigurator.load("127.0.0.1:22222");
        client = new RTPProxyClient(config);
    }

    public void run() {
        try {
            String sessionID = UUID.randomUUID().toString();
            Object appData = new Object();

            log.info("Creating new session. SessionID = " + sessionID);
            client.createSession(sessionID, appData, this);

            synchronized(this) {
                // We'll be notified when 'sessionUpdated()' callback method
                // is called.
                wait();
            }

            log.info("Session sucessful created", session);
            
        } catch (NoServerAvailableException noServerEx) {
            log.error("No servers available to create session.", noServerEx);

        } catch (Exception e) {
            log.error("Unexpected error running RTPProxyClientTest", e);
        }
    }

    public void sessionCreated(RTPProxySession session, Object appData) {
        log.info("Session created: " + session);
        this.session = session;

        try {
            log.info("Updating session " + session);
            client.updateSession(session, appData, this);

        } catch (NoServerAvailableException noServerEx) {
            log.error("No server available on updating session.", noServerEx);
        }
    }

    public void sessionUpdated(RTPProxySession session, Object appData) {
        log.info("Session updated: " + session);

        synchronized(this) {
            notify();
        }
    }

    public void createSessionTimeout(String sessionID, Object appData) {
        log.error("Session creation timeout: " + sessionID);
    }

    public void updateSessionTimeout(RTPProxySession session, Object appData) {
        log.error("Session update timeout: " + session);
    }

    public void createSessionFailed(String sessionID, Object appData, Throwable t) {
        log.error("Session creation failed: " + sessionID, t);
    }

    public void updateSessionFailed(RTPProxySession session, Object appData, Throwable t) {
        log.error("Session update failed: " + session, t);
    }

    private static void configureLog4j() {
        String configFile = "conf/log4j.properties";
        System.setProperty("log4j.configuration", "file:" + configFile);
    }
}
