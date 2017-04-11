package gprofiler.time;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import gcore.units.TimeUnit;

public class CPUStopWatch {
	private volatile long systemStartTime;
	private volatile long elapsedTime;
	private volatile boolean isTimeElapsing = false;

	public void start() {
		if (!isTimeElapsing) {
			isTimeElapsing = true;
			systemStartTime = getTime();
		}
	}

	public void stop() {
		if (isTimeElapsing) {
			elapsedTime = getTime() - systemStartTime;
			isTimeElapsing = false;
		}
	}

	public TimeUnit getElapsedTime() {
		if (isTimeElapsing) {
			return new TimeUnit(getTime() - systemStartTime, TimeUnit.NANOSECONDS);
		} else {
			return new TimeUnit(elapsedTime, TimeUnit.NANOSECONDS);
		}
	}

	/**
	 * Method used in order to eliminate the problem with using wall-clock time
	 * for a stop watch
	 * 
	 * @return
	 * @since Mar 9, 2017
	 */
	private long getTime() {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;
	}
}
