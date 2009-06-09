/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.mock.client;

import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.client.RTPProxySession;

/**
 *
 * @author mhack
 */
public class RTPProxyClientListenerMOCK
        implements RTPProxyClientListener {

    public boolean isTimeout;
    public String timeoutSessionID;
    public Object timeoutAppData;
    public boolean isCreateFail;
    public String createFailedSessionID;
    public Object createFailedAppData;
    public Object createFailedThrowable;
    public boolean isUpdateFail;
    public RTPProxySession updateFailedSession;
    public Object updateFailedAppData;
    public Object updateFailedThrowable;
    public boolean isCreate;
    public RTPProxySession createdSession;
    public Object createdAppData;
    public boolean isUpdate;
    public RTPProxySession updatedSession;
    public Object updatedAppData;

    public void createSessionTimeout(String sessionID, Object appData) {
        isTimeout = true;
        timeoutSessionID = sessionID;
        timeoutAppData = appData;
    }

    public void createSessionFailed(String sessionID, Object appData,
            Throwable t) {
        isCreateFail = true;
        createFailedSessionID = sessionID;
        createFailedAppData = appData;
        createFailedThrowable = t;
    }

    public void updateSessionFailed(RTPProxySession session, Object appData,
            Throwable t) {
        isUpdateFail = true;
        updateFailedSession = session;
        updateFailedAppData = appData;
        updateFailedThrowable = t;
    }

    public void updateSessionTimeout(RTPProxySession session, Object appData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sessionCreated(RTPProxySession session, Object appData) {
        isCreate = true;
        createdSession = session;
        createdAppData = appData;
    }

    public void sessionUpdated(RTPProxySession session, Object appData) {
        isUpdate = true;
        updatedSession = session;
        updatedAppData = appData;
    }
}