/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.test.udp;

import java.net.SocketAddress;
import java.util.Set;
import org.apache.mina.common.CloseFuture;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoFilterChain;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoService;
import org.apache.mina.common.IoServiceConfig;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.IoSessionConfig;
import org.apache.mina.common.TrafficMask;
import org.apache.mina.common.TransportType;
import org.apache.mina.common.WriteFuture;

/**
 *
 * @author mhack
 */
public class IoSessionAdapter implements IoSession {

    public IoService getService() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IoServiceConfig getServiceConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IoHandler getHandler() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IoSessionConfig getConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IoFilterChain getFilterChain() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public WriteFuture write(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CloseFuture close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getAttachment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object setAttachment(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getAttribute(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object setAttribute(String arg0, Object arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object setAttribute(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object removeAttribute(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean containsAttribute(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<String> getAttributeKeys() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TransportType getTransportType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isConnected() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isClosing() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CloseFuture getCloseFuture() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SocketAddress getRemoteAddress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SocketAddress getLocalAddress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SocketAddress getServiceAddress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getIdleTime(IdleStatus arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getIdleTimeInMillis(IdleStatus arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setIdleTime(IdleStatus arg0, int arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getWriteTimeout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getWriteTimeoutInMillis() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setWriteTimeout(int arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TrafficMask getTrafficMask() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTrafficMask(TrafficMask arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void suspendRead() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void suspendWrite() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void resumeRead() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void resumeWrite() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getReadBytes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getWrittenBytes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getReadMessages() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getWrittenMessages() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getWrittenWriteRequests() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getScheduledWriteRequests() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getScheduledWriteBytes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getCreationTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getLastIoTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getLastReadTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getLastWriteTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isIdle(IdleStatus arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getIdleCount(IdleStatus arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getLastIdleTime(IdleStatus arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
