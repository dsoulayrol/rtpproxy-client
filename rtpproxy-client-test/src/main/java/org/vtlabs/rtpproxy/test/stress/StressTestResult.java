package org.vtlabs.rtpproxy.test.stress;

import java.text.DecimalFormat;

/**
 *
 * @author marcoshack
 */
public class StressTestResult {

    public static String getTextResult(StressTestStatistics stats) {
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuilder sb = new StringBuilder("Stress Test Results:\n\t");
        sb.append("create request(s): ");
        sb.append(stats.getCreateRequestCounter()).append(" (");
        sb.append(df.format(stats.getCreateSucessRate() * 100)).append("% sucess, ");
        sb.append(df.format(stats.getCreateFailRate() * 100)).append("% fail, ");
        sb.append(df.format(stats.getCreateTimeoutRate() * 100)).append("% timeout)\n\t");
        sb.append("update request(s): ");
        sb.append(stats.getUpdateRequestCounter()).append(" (");
        sb.append(df.format(stats.getUpdateSucessRate() * 100)).append("% sucess, ");
        sb.append(df.format(stats.getUpdateFailRate() * 100)).append("% fail, ");
        sb.append(df.format(stats.getUpdateTimeoutRate() * 100)).append("% timeout)\n\t");
        sb.append("destroy request(s): ");
        sb.append(stats.getDestroyRequestCounter()).append(" (");
        sb.append(df.format(stats.getDestroySucessRate() * 100)).append("% sucess, ");
        sb.append(df.format(stats.getDestroyFailRate() * 100)).append("% fail, ");
        sb.append(df.format(stats.getDestroyTimeoutRate() * 100)).append("% timeout)\n");
        return sb.toString();
    }
}
