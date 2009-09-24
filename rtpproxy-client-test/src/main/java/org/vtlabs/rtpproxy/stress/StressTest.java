package org.vtlabs.rtpproxy.stress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.vtlabs.rtpproxy.client.RTPProxyClient;
import org.vtlabs.rtpproxy.config.RTPProxyClientConfig;
import org.vtlabs.rtpproxy.config.RTPProxyClientConfigurator;
import org.vtlabs.rtpproxy.test.BaseTest;

/**
 *
 * @author Marcos Hack <marcoshack@gmail.com>
 */
public class StressTest extends BaseTest {
    private static final int DEFAULT_NUMBER_OF_THREADS = 10;
    private static final int DEFAULT_NUMBER_OF_TASKS   = 5;
    private static final long DEFAULT_TASK_INTERVAL    = 500L;
    private static final long DEFAULT_TEST_DURATION    = 10L;
    private static final String DEFAULT_SERVER_LIST    = "127.0.0.1:22222";

    private static Logger log = Logger.getLogger(StressTest.class);
    private int nThreads;
    private int nTasks;
    private long taskInterval;
    private long testDuration;
    private String serverList;
    private Statistics stats;
    private ScheduledThreadPoolExecutor executor;
    private RTPProxyClient client;
    private List<ScheduledFuture<StressTask>> tasks;

    public static void main(String[] args) {
        log.info("Starting RTPProxy-Client stress test");

        try {
            int nThreads = Integer.getInteger("rtpproxy.stress.threads", DEFAULT_NUMBER_OF_THREADS);
            int nTasks = Integer.getInteger("rtpproxy.stress.tasks", DEFAULT_NUMBER_OF_TASKS);
            long taskInterval = Long.getLong("rtpproxy.stress.task_interval", DEFAULT_TASK_INTERVAL);
            long testDuration = Long.getLong("rtpproxy.stress.period", DEFAULT_TEST_DURATION);
            String serverList = System.getProperty("rtpproxy.stress.servers", DEFAULT_SERVER_LIST);

            StressTest test = new StressTest();
            test.setThreadNumber(nThreads);
            test.setTaskNumber(nTasks);
            test.setTaskInterval(taskInterval);
            test.setTestDuration(testDuration);
            test.setServerList(serverList);
            test.start();
            test.waitTestDuration();
            test.stop();
            test.showTestResults();

            log.info("Stress test sucessful terminated");
            System.exit(0);

        } catch (Exception e) {
            log.error("Error executing StressTest", e);
            System.exit(1);
        }
    }

    public void start() throws Exception {
        log.info(new StringBuilder("Stress rate: ").append(getStressRate())
                .append(" sessions/sec"));

        RTPProxyClientConfig config = RTPProxyClientConfigurator.load(serverList);
        client = new RTPProxyClient(config);
        stats = new Statistics();
        executor = new ScheduledThreadPoolExecutor(nThreads);
        stats.start();
        startTasks();
    }

    public void stop() throws Exception {
        stats.end();
        stopTasks();
        stopExecutor();
    }

    public void showTestResults() {
        StringBuilder sb = new StringBuilder("Stress Test Results:\n\t");
        sb.append("create request(s): ");
        sb.append(stats.getCreateRequestCounter()).append(" (");
        sb.append(stats.getCreateSucessRate() * 100).append("% sucess, ");
        sb.append(stats.getCreateFailRate() * 100).append("% fail, ");
        sb.append(stats.getCreateTimeoutRate() * 100).append("% timeout)\n\t");
        sb.append("update request(s): ");
        sb.append(stats.getUpdateRequestCounter()).append(" (");
        sb.append(stats.getUpdateSucessRate() * 100).append("% sucess, ");
        sb.append(stats.getUpdateFailRate() * 100).append("% fail, ");
        sb.append(stats.getUpdateTimeoutRate() * 100).append("% timeout)\n\t");
        sb.append("destroy request(s): ");
        sb.append(stats.getDestroyRequestCounter()).append(" (");
        sb.append(stats.getDestroySucessRate() * 100).append("% sucess, ");
        sb.append(stats.getDestroyFailRate() * 100).append("% fail, ");
        sb.append(stats.getDestroyTimeoutRate() * 100).append("% timeout)\n\t");
        System.out.print(sb);
    }

    public void setTaskNumber(int nTasks) {
        this.nTasks = nTasks;
    }

    public void setThreadNumber(int nThreads) {
        this.nThreads = nThreads;
    }

    public void setServerList(String serverList) {
        this.serverList = serverList;
    }

    public void setTaskInterval(long taskInterval) {
        this.taskInterval = taskInterval;
    }

    public void setTestDuration(long testDuration) {
        this.testDuration = testDuration;
    }

    private void stopTasks() {
        log.info("Canceling remaining tasks");
        for (ScheduledFuture<StressTask> task : tasks) {
            task.cancel(true);
        }
    }

    private void stopExecutor() throws InterruptedException {
        log.info("Stopping thread pool executor");
        executor.shutdownNow();
        executor.awaitTermination(-1, TimeUnit.SECONDS);
    }

    private void waitTestDuration()
            throws InterruptedException {

        log.info(new StringBuilder("Waiting ").append(testDuration)
                .append(" seconds for test completion"));
        
        Thread.sleep(testDuration * 1000);
    }

    private void startTasks() {
        tasks = new ArrayList<ScheduledFuture<StressTask>>(nTasks);

        for (int i = 0; i < nTasks; i++) {
            tasks.add((ScheduledFuture<StressTask>)executor.scheduleAtFixedRate(
                    new StressTask(client, stats, i),
                    taskInterval,
                    taskInterval,
                    TimeUnit.MILLISECONDS));
        }
    }

    /**
     * Number of sessions created per second.
     */
    private double getStressRate() {
        double result = (1000 / taskInterval) * nTasks;
        return result;
    }
}
