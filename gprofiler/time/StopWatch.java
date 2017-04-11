package gprofiler.time;

import gcore.units.TimeUnit;

public class StopWatch {
	private volatile long systemStartTime;
	private volatile long elapsedTime;
	private volatile boolean isTimeElapsing = false;

	public void start() {
		if (!isTimeElapsing) {
			isTimeElapsing = true;
			systemStartTime = System.nanoTime();
		}
	}

	public void stop() {
		if (isTimeElapsing) {
			elapsedTime = System.nanoTime() - systemStartTime;
			isTimeElapsing = false;
		}
	}

	public TimeUnit getElapsedTime() {
		if (isTimeElapsing) {
			return new TimeUnit(System.nanoTime() - systemStartTime, TimeUnit.NANOSECONDS);
		} else {
			return new TimeUnit(elapsedTime, TimeUnit.NANOSECONDS);
		}
	}
}
