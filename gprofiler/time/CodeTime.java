package gprofiler.time;

import java.util.ArrayList;

import gcore.units.TimeUnit;
import gstats.sample.Sample;
import gstats.sample.SampleStatistics;

public final class CodeTime {

	private CodeTime() {}

	/**
	 * Used to view how long the program really takes to run
	 * 
	 * @param r
	 *            runnable to execute
	 * @return The actual amount of time it took for it to run.
	 * @since Mar 9, 2017
	 */
	public static TimeUnit getActualRunTime(Runnable r) {
		StopWatch s = new StopWatch();
		s.start();
		r.run();
		s.stop();
		return s.getElapsedTime();
	}

	/**
	 * Used to view how long the program took to run only counting when it was
	 * on the cpu.
	 * 
	 * @param r
	 *            runnable to execute
	 * @return The time it took to run where it was on the cpu.
	 * @since Mar 9, 2017
	 */
	public static TimeUnit getRunTime(Runnable r) {
		CPUStopWatch s = new CPUStopWatch();
		s.start();
		r.run();
		s.stop();
		return s.getElapsedTime();
	}

	public static TimeUnit getAverageRunTime(Runnable r, int numberOfAttempts) {
		// run the get run times and then take the average
		Sample s = getRunTimes(r, numberOfAttempts, TimeUnit.NANOSECONDS);

		return TimeUnit.valueOf(SampleStatistics.mean(s), TimeUnit.NANOSECONDS);
	}

	/**
	 * generates a sample of the run times for the runnable, will also throw out
	 * large outliers to try and eliminate start up times for smaller precision
	 * runs as well.
	 * 
	 * @param r
	 *            runnable to test
	 * @param numberOfAttempts
	 *            number of times to run it
	 * @param unit
	 *            units for the results to be in
	 * @return sample containing the results of the trial
	 */
	public static Sample getRunTimes(Runnable r, int numberOfAttempts, double unit) {
		// stores all of the data for each run time.
		ArrayList<Double> sample = new ArrayList<>();

		// get the run time number of attempts number of times and each time add
		// to sample
		CPUStopWatch s = new CPUStopWatch();
		for (int x = 0; x < numberOfAttempts; x++) {
			s.start();
			r.run();
			s.stop();

			sample.add(s.getElapsedTime().convertTo(unit));
		}

		// create the sample.
		Sample smpl = new Sample(sample);

		// get the iqr of the smpl and the lower and upper bounds
		double q1 = SampleStatistics.firstQuartile(smpl);
		double q3 = SampleStatistics.thirdQuartile(smpl);
		double iqr = 2 * (q3 - q1);
		double maxAllowed = q3 + iqr;
		double minAllowed = q1 - iqr;

		// remove any data points that are outside the range [maxAllowed,
		// minAllowed]
		while (smpl.getSmallest() < minAllowed) {
			smpl.remove(smpl.getSmallest());
		}

		while (smpl.getLargest() > maxAllowed) {
			smpl.remove(smpl.getLargest());
		}

		// return the sample
		return smpl;
	}

	// TODO: goal is to be able to infer runtime on a variable using this....
	// i.e. O(n), O(n^2), O(e^n)...

}
