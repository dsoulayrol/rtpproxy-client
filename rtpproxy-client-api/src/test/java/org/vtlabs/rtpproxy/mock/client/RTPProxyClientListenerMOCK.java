/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.mock.client;

import org.vtlabs.rtpproxy.client.RTPProxyClientListener;
import org.vtlabs.rtpproxy.client.RTPProxySession;
import org.vtlabs.rtpproxy.client.RTPProxySessionImpl;

/**
 * 
 * @author mhack
 */
public class RTPProxyClientListenerMOCK implements RTPProxyClientListener {

	public boolean isCreateTimeout;
	public String createTimeoutSessionID;
	public Object createTimeoutAppData;
	public boolean isUpdateTimeout;
	public RTPProxySessionImpl updateTimeoutSession;
	public Object updateTimeoutAppData;
	public boolean isCreateFail;
	public String createFailedSessionID;
	public Object createFailedAppData;
	public Object createFailedThrowable;
	public boolean isUpdateFail;
	public RTPProxySessionImpl updateFailedSession;
	public Object updateFailedAppData;
	public Object updateFailedThrowable;
	public boolean isDestroyFail;
	public RTPProxySessionImpl destroyFailedSession;
	public Object destroyFailedAppData;
	public Object destroyFailedThrowable;
	public boolean isCreate;
	public RTPProxySessionImpl createdSession;
	public Object createdAppData;
	public boolean isUpdate;
	public RTPProxySessionImpl updatedSession;
	public Object updatedAppData;
	public boolean isDestroy;
	public RTPProxySessionImpl destroySession;
	public Object destroyAppData;
	public boolean isRecordableCreateTimeout;
	public String createRecordableTimeoutSessionID;
	public String createRecordableTimeoutAppData;
	public boolean isRecordableCreateFail;
	public String createRecordableFailedSessionID;
	public String createRecordableFailedAppData;
	public boolean isRecordableCreate;
	public String createdRecordableSession;
	public String createdRecordableAppData;
	

	public void createSessionTimeout(String sessionID, Object appData) {
		isCreateTimeout = true;
		createTimeoutSessionID = sessionID;
		createTimeoutAppData = appData;
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
		updateFailedSession = (RTPProxySessionImpl) session;
		updateFailedAppData = appData;
		updateFailedThrowable = t;
	}

	public void updateSessionTimeout(RTPProxySession session, Object appData) {
		isUpdateTimeout = true;
		updateTimeoutSession = (RTPProxySessionImpl) session;
		updateTimeoutAppData = appData;
	}

	public void sessionCreated(RTPProxySession session, Object appData) {
		isCreate = true;
		createdSession = (RTPProxySessionImpl) session;
		createdAppData = appData;
	}

	public void sessionUpdated(RTPProxySession session, Object appData) {
		isUpdate = true;
		updatedSession = (RTPProxySessionImpl) session;
		updatedAppData = appData;
	}

	public void destroySessionFailed(RTPProxySession session, Object appData,
			Throwable t) {
		isDestroyFail = true;
		destroyFailedSession = (RTPProxySessionImpl) session;
		destroyFailedAppData = appData;
		destroyFailedThrowable = t;
	}

	public void destroySessionTimeout(RTPProxySession session, Object appData) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void sessionDestroyed(RTPProxySession session, Object appData) {
		isDestroy = true;
		destroySession = (RTPProxySessionImpl) session;
		destroyAppData = appData;
	}

	public void createRecordableSessionTimeout(String sessionID, Object appData) {
		isRecordableCreateTimeout = true;
		createRecordableTimeoutSessionID = sessionID;
		createRecordableTimeoutAppData = appData;
	}

	public void createRecordableSessionFailed(String sessionID, Object appData) {
		isRecordableCreateFail = true;
		createRecordableFailedSessionID = sessionID;
		createRecordableFailedAppData = appData;
	}
	
	public void recordableSessionCreated(String sessionID, Object appData){
		isRecordableCreate = true;
		createdRecordableSession = sessionID;
		createdRecordableAppData = appData;
	}
}