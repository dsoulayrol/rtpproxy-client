/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.client;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mhack
 */
public class RTPProxyClientConfig {

    /**
     * Default bind port used by UDP service if it isn't specified.
     */
    public static final int DEFAULT_BIND_PORT = 9876;
    /**
     * Default scheduled thread pool executor pool size, used by the command
     * timeout service.
     */
    public static final int DEFAULT_SCHEDULED_POOL_SIZE = 3;
    
    private int bindPort;
    private int poolSize;
    private List<RTPProxyServer> serverList;

    public RTPProxyClientConfig() {
        serverList = new ArrayList<RTPProxyServer>();
        setBindPort(DEFAULT_BIND_PORT);
        setPoolSize(DEFAULT_SCHEDULED_POOL_SIZE);
    }

    public int getBindPort() {
        return bindPort;
    }

    public void setBindPort(int bindPort) {
        this.bindPort = bindPort;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public List<RTPProxyServer> getServerList() {
        return serverList;
    }

    public void addServer(RTPProxyServer server) {
        serverList.add(server);
    }

    public boolean removeServer(RTPProxyServer server) {
        return serverList.remove(server);
    }
}
