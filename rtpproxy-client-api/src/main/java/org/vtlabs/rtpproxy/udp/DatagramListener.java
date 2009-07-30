/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.udp;

import org.vtlabs.rtpproxy.message.ResponseMessage;

/**
 *
 * @author mhack
 */
public interface DatagramListener {
    public void processResponse(ResponseMessage message);
}
