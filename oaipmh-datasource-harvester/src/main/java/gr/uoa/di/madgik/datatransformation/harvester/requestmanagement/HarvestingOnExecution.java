package gr.uoa.di.madgik.datatransformation.harvester.requestmanagement;

public class HarvestingOnExecution {

	private boolean inExecution;
	
	private static HarvestingOnExecution harvestingInstance = null;

	protected HarvestingOnExecution(){
		this.inExecution = false;
	};
	
	public static synchronized HarvestingOnExecution getHarvestingInstance() {
			if (harvestingInstance == null)
				harvestingInstance = new HarvestingOnExecution();

		return harvestingInstance;
	}
	
	
	public void setInExecution(boolean value) {
		this.inExecution = value;
	}
	
	public boolean getInExecution() {
		return this.inExecution;
	}
}