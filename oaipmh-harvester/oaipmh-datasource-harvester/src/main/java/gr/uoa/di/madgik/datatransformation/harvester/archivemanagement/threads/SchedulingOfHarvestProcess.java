package gr.uoa.di.madgik.datatransformation.harvester.archivemanagement.threads;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gr.uoa.di.madgik.datatransformation.harvester.requestmanagement.HarvestingOnExecution;

public class SchedulingOfHarvestProcess extends TimerTask {

	private static final ExecutorService executor = Executors.newFixedThreadPool(10);

	private static SchedulingOfHarvestProcess threadLoader = null;

	protected SchedulingOfHarvestProcess() {
	}

	public static SchedulingOfHarvestProcess periodicTask() {
		if (threadLoader == null) {
			threadLoader = new SchedulingOfHarvestProcess();
		}
		return threadLoader;
	}

	@Override
	public void run() {
		try {
			synchronized (HarvestingOnExecution.class) {
				HarvestingOnExecution.getHarvestingInstance().setInExecution(true);
			}

			CoordinateHarvestingProcess.executeHarvesting(executor);

			synchronized (HarvestingOnExecution.class) {
				HarvestingOnExecution.getHarvestingInstance().setInExecution(false);
			}

			ManageFiles mf = new ManageFiles();
			mf.mergeQueueWithArchive();
		} catch (Exception e) {
		}
	}

}
