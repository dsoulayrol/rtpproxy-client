package org.vtlabs.rtpproxy.client;

/**
 *
 * @author Marcos Hack <marcosh@voicetechnology.com.br>
 */
public class RTPProxyServer {

    private String serverIpAddress;
    private Integer serverPort;
    private Long capacity;

    public RTPProxyServer(String serverIpAddress, Integer serverPort, Long capacity) {
        this.serverIpAddress = serverIpAddress;
        this.serverPort = serverPort;
        this.capacity = capacity;
    }
}
