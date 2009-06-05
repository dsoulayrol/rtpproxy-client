/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vtlabs.rtpproxy.test;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 *
 * @author mhack
 */
@Ignore
public class BaseTest {

    @BeforeClass
    public static void classInit() {
        BasicConfigurator.configure();
    }
}
