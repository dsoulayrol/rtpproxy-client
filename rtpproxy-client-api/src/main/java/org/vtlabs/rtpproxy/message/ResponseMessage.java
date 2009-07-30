/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vtlabs.rtpproxy.message;

import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author mhack
 */
public class ResponseMessage {

    private final Pattern errorPattern = Pattern.compile("^E(.*)$");
    private final Matcher errorMatcher = errorPattern.matcher("");

    private String cookie;
    private String message;
    private InetSocketAddress srcAddr;

    public ResponseMessage(String cookie, String messageLine,
            InetSocketAddress srcAddr) {
        this.cookie = cookie;
        this.message = messageLine;
        this.srcAddr = srcAddr;
    }

    public static ResponseMessage parseMessage(String rawMessage,
            InetSocketAddress srcAddr) {
        String arrMessage[] = StringUtils.split(rawMessage, " ", 2);
        return new ResponseMessage(arrMessage[0], arrMessage[1],
                srcAddr);
    }

    public String getCookie() {
        return cookie;
    }

    public String getMessageLine() {
        return message;
    }

    public InetSocketAddress getSrcAddr() {
        return srcAddr;
    }

    public boolean isError() {
        return errorMatcher.reset(message).matches();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ResponseMessage[cookie = ");
        sb.append(cookie);
        sb.append(", messageLine = ").append(message);
        sb.append(", srcAddr = ").append(srcAddr);
        sb.append("]");
        return sb.toString();
    }
}
