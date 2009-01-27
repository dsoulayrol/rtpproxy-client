package org.vtlabs.rtpproxy.client;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxyClient {
    private List<RTPProxyEventListener> listenerList;
    private UDPServer udpServer;

    public RTPProxyClient() {
        listenerList = new ArrayList<RTPProxyEventListener>();
    }

    public void addListener(RTPProxyEventListener listener) {
        synchronized(listenerList) {
            listenerList.add(listener);
        }
    }
    
}
