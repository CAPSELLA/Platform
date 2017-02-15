package gr.uoa.di.madgik.datatransformation.harvester.timesmanagement.observetimes;

public interface TimesFile {

	public void register(ObserveTimesFile obj);
    public void unregister(ObserveTimesFile obj);

    public void notifyObservers();
     
    public Object getUpdate(ObserveTimesFile obj);
}