package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager;

import java.util.Map;

import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;

public class RWActions {

	public static void readFromFile(FilesManagerRead f, boolean initialization) {
		f.readFromFile(initialization);
	}
	
	public static void registerUriOnFile(FilesManagerWrite f, MessageForEveryDataProvider messageForEveryDataProvider) {
		f.registerUriOnFile(messageForEveryDataProvider);
	}
	
	public static void registerUriOnFile(FilesManagerWrite f, Map<String, String> parameters) {
		f.registerUriOnFile(parameters);
	}
	
	public static void writeToFile(FilesManagerWrite f) {
		f.writeToFile();
	}
	
}