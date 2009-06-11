package org.vtlabs.rtpproxy.client;

import java.net.InetSocketAddress;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxyServer {
    private InetSocketAddress address;

    public RTPProxyServer() {
    }

    public RTPProxyServer(InetSocketAddress serverAddress) {
        address = serverAddress;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RTPProxyServer[");
        sb.append("address = ").append(address);
        sb.append("]");
        return sb.toString();
    }

}
