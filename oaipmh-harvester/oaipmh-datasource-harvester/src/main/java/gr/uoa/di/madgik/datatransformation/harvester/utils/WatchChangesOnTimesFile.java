package gr.uoa.di.madgik.datatransformation.harvester.utils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import gr.uoa.di.madgik.datatransformation.harvester.timesmanagement.observetimes.ObserveTimesFile;
import gr.uoa.di.madgik.datatransformation.harvester.timesmanagement.observetimes.TimesFile;

public class WatchChangesOnTimesFile implements TimesFile {

	private final static Logger logger = Logger.getLogger(WatchChangesOnTimesFile.class);

	public static WatchChangesOnTimesFile watchChangesOnTimesFile = null;
	
	protected WatchChangesOnTimesFile() {
		this.observers = new ArrayList<ObserveTimesFile>();
	}
	
	private int countEvents = 0;
	
	public static WatchChangesOnTimesFile getWatchChangesOnTimesFileInstance() {
	if (watchChangesOnTimesFile == null) {
		watchChangesOnTimesFile = new WatchChangesOnTimesFile();
			
			
		Thread threadForWatcher = new Thread() {
			public void run() {
					WatchChangesOnTimesFile.getWatchChangesOnTimesFileInstance().setListener();
				}
			};
			threadForWatcher.start();
		}
		return watchChangesOnTimesFile;	
	}
	
	public void setListener() {
		Path directory = Paths.get(GetProperties.getPropertiesInstance().getTimesDir());
		try (final WatchService watchService = FileSystems.getDefault().newWatchService()){

			directory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

			for(;;) {
				WatchKey wk = watchService.take();
				for (WatchEvent<?> event : wk.pollEvents()) {
					
					WatchEvent.Kind<?> kind = event.kind();
					if (kind == StandardWatchEventKinds.OVERFLOW) continue;

					final Path changed = (Path) event.context();
					if (changed.endsWith(GetProperties.getPropertiesInstance().getTimesFile())) {
						countEvents++;
						if (countEvents==2) {
							countEvents = 0;
							this.postMessage(true);
						}
					}
				}
				boolean valid = wk.reset();
				if (!valid) logger.info("Key has been unregistered");
			}
		} catch (IOException | InterruptedException e) {
			logger.error(e.getMessage());
		}
	}
	
	
	List<ObserveTimesFile> observers;
	private boolean fileChanged;
	
	@Override
	public void register(ObserveTimesFile obj) {
		if (obj == null) throw new NullPointerException();
		if (!this.observers.contains(obj)) this.observers.add(obj);
	}

	@Override
	public void unregister(ObserveTimesFile obj) {
		this.observers.remove(obj);
	}

	@Override
	public void notifyObservers() {
		List<ObserveTimesFile> obs = null;
		obs = new ArrayList<>(this.observers);
		for (ObserveTimesFile o: obs) {
			o.update();
		}
	}

	@Override
	public Object getUpdate(ObserveTimesFile obj) {
		return this.fileChanged;
	}
	
	public void postMessage(boolean boolValue){
		this.fileChanged = boolValue;
		this.notifyObservers();
	}
}
