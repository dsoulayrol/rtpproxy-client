package org.vtlabs.rtpproxy.test.stress;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author marcoshack
 */
public class StressTestStatistics {
    private Date startTime;
    private Date endTime;
    private AtomicLong createRequestCounter;
    private AtomicLong destroyRequestCounter;
    private AtomicLong updateRequestCounter;
    private AtomicLong createSucessCounter;
    private AtomicLong destroySucessCounter;
    private AtomicLong updateSucessCounter;
    private AtomicLong createFailCounter;
    private AtomicLong destroyFailCounter;
    private AtomicLong updateFailCounter;
    private AtomicLong createTimeoutCounter;
    private AtomicLong destroyTimeoutCounter;
    private AtomicLong updateTimeoutCounter;

    public StressTestStatistics() {
        createRequestCounter = new AtomicLong(0L);
        destroyRequestCounter = new AtomicLong(0L);
        updateRequestCounter = new AtomicLong(0L);
        createSucessCounter = new AtomicLong(0L);
        destroySucessCounter = new AtomicLong(0L);
        updateSucessCounter = new AtomicLong(0L);
        createFailCounter = new AtomicLong(0L);
        destroyFailCounter = new AtomicLong(0L);
        updateFailCounter = new AtomicLong(0L);
        createTimeoutCounter = new AtomicLong(0L);
        destroyTimeoutCounter = new AtomicLong(0L);
        updateTimeoutCounter = new AtomicLong(0L);
    }

    public void start() {
        startTime = GregorianCalendar.getInstance().getTime();
    }

    public void end() {
        endTime = GregorianCalendar.getInstance().getTime();
    }

    public void incCreateRequest() {
        createRequestCounter.incrementAndGet();
    }

    public void incDestroyRequest() {
        destroyRequestCounter.incrementAndGet();
    }

    public void incUpdateRequest() {
        updateRequestCounter.incrementAndGet();
    }

    public void incCreateSucess() {
        createSucessCounter.incrementAndGet();
    }

    public void incDestroySucess() {
        destroySucessCounter.incrementAndGet();
    }

    public void incUpdateSucess() {
        updateSucessCounter.incrementAndGet();
    }

    public void incCreateFail() {
        createFailCounter.incrementAndGet();
    }

    public void incDestroyFail() {
        destroyFailCounter.incrementAndGet();
    }

    public void incUpdateFail() {
        updateFailCounter.incrementAndGet();
    }
    
    public void incCreateTimeout() {
        createTimeoutCounter.incrementAndGet();
    }

    public void incDestroyTimeout() {
        destroyTimeoutCounter.incrementAndGet();
    }

    public void incUpdateTimeout() {
        updateTimeoutCounter.incrementAndGet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Statistics[");
        sb.append(", duration = ").append(getDurationInMillis()).append(" ms");
        sb.append(", createRequest = ").append(createRequestCounter);
        sb.append(", updateRequest = ").append(updateRequestCounter);
        sb.append(", destroyRequest = ").append(updateRequestCounter);
        sb.append(", createSucess = ").append(createSucessCounter);
        sb.append(", updateSucess = ").append(updateSucessCounter);
        sb.append(", destroySucess = ").append(updateSucessCounter);
        sb.append(", createFail = ").append(createFailCounter);
        sb.append(", updateFail = ").append(updateFailCounter);
        sb.append(", destroyFail = ").append(updateFailCounter);
        sb.append(", createTimeout = ").append(createTimeoutCounter);
        sb.append(", updateTimeout = ").append(updateTimeoutCounter);
        sb.append(", destroyTimeout = ").append(updateTimeoutCounter);
        return sb.toString();
    }

    public long getDurationInMillis() {
        if (endTime != null && startTime != null) {
            return endTime.getTime() - startTime.getTime();
        } else {
            return 0L;
        }
    }

    public long getCreateFailCounter() {
        return createFailCounter.get();
    }

    public long getCreateRequestCounter() {
        return createRequestCounter.get();
    }

    public long getCreateSucessCounter() {
        return createSucessCounter.get();
    }

    public long getCreateTimeoutCounter() {
        return createTimeoutCounter.get();
    }

    public long getDestroyFailCounter() {
        return destroyFailCounter.get();
    }

    public long getDestroyRequestCounter() {
        return destroyRequestCounter.get();
    }

    public long getDestroySucessCounter() {
        return destroySucessCounter.get();
    }

    public long getDestroyTimeoutCounter() {
        return destroyTimeoutCounter.get();
    }

    public long getUpdateFailCounter() {
        return updateFailCounter.get();
    }

    public long getUpdateRequestCounter() {
        return updateRequestCounter.get();
    }

    public long getUpdateSucessCounter() {
        return updateSucessCounter.get();
    }

    public long getUpdateTimeoutCounter() {
        return updateTimeoutCounter.get();
    }

    public double getCreateSucessRate() {
        return getRate(createSucessCounter, createRequestCounter);
    }

    public double getCreateFailRate() {
        return getRate(createFailCounter, createRequestCounter);
    }

    public double getCreateTimeoutRate() {
        return getRate(createTimeoutCounter, createRequestCounter);
    }

    public double getUpdateSucessRate() {
        return getRate(updateSucessCounter, updateRequestCounter);
    }

    public double getUpdateFailRate() {
        return getRate(updateFailCounter, updateRequestCounter);
    }

    public double getUpdateTimeoutRate() {
        return getRate(updateTimeoutCounter, updateRequestCounter);
    }

    public double getDestroySucessRate() {
        return getRate(destroySucessCounter, destroyRequestCounter);
    }

    public double getDestroyFailRate() {
        return getRate(destroyFailCounter, destroyRequestCounter);
    }

    public double getDestroyTimeoutRate() {
        return getRate(destroyTimeoutCounter, destroyRequestCounter);
    }

    private double getRate(AtomicLong share, AtomicLong divisor) {
        if (divisor.get() > 0) {
            return (double) share.get() / divisor.get();
        } else {
            return 0L;
        }
    }
}
