package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager;

import java.util.Map;

import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;

public interface FilesManagerWrite {

	public void registerUriOnFile(MessageForEveryDataProvider messageForEveryDataProvide);
	public void registerUriOnFile(Map<String, String> parameters);
	
	public void writeToFile();
	
}
