package gprofiler.test;

import gcore.units.TimeUnit;
import gprofiler.time.CodeTime;

public class Tester {
	public static void main(String[] args) {
		TimeUnit t = CodeTime.getActualRunTime(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
				}
			}
		});

		System.out.println(t.convertTo(TimeUnit.SECONDS));
	}

	public static int factorial(int x) {
		if (x == 0)
			return 1;
		return x * factorial(x - 1);
	}
}
