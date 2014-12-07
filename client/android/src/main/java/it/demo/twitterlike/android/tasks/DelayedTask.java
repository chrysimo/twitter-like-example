package it.demo.twitterlike.android.tasks;

import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

public class DelayedTask extends GroundyTask {

	public static final String DELAY_MILLIS_PARAMETER = "delay";

	@Override
	protected TaskResult doInBackground() {
		long delay = getLongArg(DELAY_MILLIS_PARAMETER, 0);

		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
		}
		return succeeded().addAll(getArgs());
	}

}
