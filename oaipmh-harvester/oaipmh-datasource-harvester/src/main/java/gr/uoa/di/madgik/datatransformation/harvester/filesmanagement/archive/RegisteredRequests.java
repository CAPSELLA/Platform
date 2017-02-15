package gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;

import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.manager.RWActions;
import gr.uoa.di.madgik.datatransformation.harvester.harvestedmanagement.core.SelectedDatabase;

public class RegisteredRequests {

private static RegisteredRequests registeredRequests = null;
	
	private Map<String, List<MessageForEveryDataProvider>> registeredRequestsMapping = null;

	protected RegisteredRequests(){
		this.registeredRequestsMapping = Collections.synchronizedMap(new HashMap<String, List<MessageForEveryDataProvider>>());
	}
	
	public  static RegisteredRequests getRegisteredRequestsInstance() {
		if (registeredRequests==null) {
			registeredRequests = new RegisteredRequests();
		}
		return registeredRequests;
	}
	
	public boolean updateStatusMessagesForUrl(final MessageForEveryDataProvider message, Boolean isExecuting) {
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			synchronized (registeredRequestsMapping) {
				List<MessageForEveryDataProvider> messages;
				if ((messages = this.registeredRequestsMapping.get(message.getInfoForHarvesting().getUrl())) == null)
					messages = new ArrayList<MessageForEveryDataProvider>() {/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

					{add(message);}};
				else {
					MessageForEveryDataProvider toUpdate = null;
					for (MessageForEveryDataProvider m: messages) {
						if (m.getInfoForHarvesting().getUrl().equals(message.getInfoForHarvesting().getUrl()) &&
							m.getInfoForHarvesting().getListRecords().getMetadataPrefix().equals(message.getInfoForHarvesting().getListRecords().getMetadataPrefix())) 
						{
							toUpdate = m;
							break;
						}
					}
					if (isExecuting) {
						if (toUpdate.isExecuting()) return false;
						toUpdate.setForceReharvest(false);
					}
					messages.remove(toUpdate);
					
					toUpdate.setExecuting(isExecuting);
					messages.add(toUpdate);
				}
				this.registeredRequestsMapping.put(message.getInfoForHarvesting().getUrl(), messages);
				RWActions.writeToFile(WriteUrls.getWriteUrlsInstance());
				return true;
			}
		}
	}
	
	public void updateStatusMessagesForUrl(String url, Boolean isExecuting) {
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			synchronized (registeredRequestsMapping) {
				List<MessageForEveryDataProvider> messages;
				if ((messages = this.registeredRequestsMapping.get(url)) == null)
					return;
				MessageForEveryDataProvider mm = null;
				for (MessageForEveryDataProvider m: messages) {	
					mm = m;
					break;
				}
				messages.remove(mm);
				mm.setExecuting(isExecuting);
				messages.add(mm);
				
				this.registeredRequestsMapping.put(url, messages);
				RWActions.writeToFile(WriteUrls.getWriteUrlsInstance());
			}
		}
	}
	
	public List<MessageForEveryDataProvider> getFromRegisteredRequestsMapping(String key) {
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			synchronized (registeredRequestsMapping) {
				if (registeredRequestsMapping.get(key)==null) return null;
				List<MessageForEveryDataProvider> messages = new ArrayList<>();
				messages.addAll(registeredRequestsMapping.get(key));
				return messages;
			}
		}
	}
	
	public void setToDeleteFalse(String url) {
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			synchronized (registeredRequestsMapping) {
				List<MessageForEveryDataProvider> messages;
				if ((messages = this.registeredRequestsMapping.get(url)) == null)
					return;
				for (MessageForEveryDataProvider m: messages)
					m.setToDelete(false);
				
				this.registeredRequestsMapping.put(url, messages);
				RWActions.writeToFile(WriteUrls.getWriteUrlsInstance());
			}
		}
	}
	
	public boolean checkExistence(String url, String supportedSchema) {
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			synchronized (registeredRequestsMapping) {
				List<MessageForEveryDataProvider> messages = registeredRequestsMapping.get(url);
				for (MessageForEveryDataProvider message: messages) {
					if (message.getSchemaSupportedForUrl().equals(supportedSchema) ||
						 message.getSchemaSupportedForUrl().equals("all") && supportedSchema.equals("dc")) {
						return true;
					}
				}
				return false;
			}
		}	
	}
	
	/*for ListRecords by default*/
	public boolean reharvestUri(String uri, String metadataPrefix) {
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			synchronized (registeredRequestsMapping) {
				MessageForEveryDataProvider mm = null;
				List<MessageForEveryDataProvider> messages = registeredRequestsMapping.get(uri);
				for (MessageForEveryDataProvider m: messages) {
					if (m.getInfoForHarvesting().getListRecords().getMetadataPrefix().equals(metadataPrefix)) {
						if (m.isExecuting()) return false;
						else {
							mm = m;
							break;
						}
					}
				}
				if (mm!=null) {
					messages.remove(mm);
					mm.setForceReharvest(true);
					messages.add(mm);
					registeredRequestsMapping.put(uri, messages);
					return true;
				}
			}
		}
		return false;
	}
	
	public Map<String, List<MessageForEveryDataProvider>> getRegisteredRequestsMapping() {
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			synchronized (registeredRequestsMapping) {	
				return new HashMap<>(registeredRequestsMapping);
			}
		}
	}

	public List<MessageForEveryDataProvider> getAllMessages() {
		List<MessageForEveryDataProvider> messagesForEveryDataProviders = new ArrayList<>();
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			synchronized (registeredRequestsMapping) {
				for (Map.Entry<String, List<MessageForEveryDataProvider>> entry: registeredRequestsMapping.entrySet()) {
					for (MessageForEveryDataProvider m: entry.getValue())
						messagesForEveryDataProviders.add(m);
				}
			}
		}
		return messagesForEveryDataProviders;
	}
	
	public boolean removeFromRegisteredRequests(MessageForEveryDataProvider m) {
		return removeFromRegisteredRequests(m.getInfoForHarvesting().getUrl());
	}
	
	public boolean removeFromRegisteredRequests(String uri) {
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			synchronized (registeredRequestsMapping) {
				List<MessageForEveryDataProvider> messages = new ArrayList<>();
				List<MessageForEveryDataProvider> fetched = registeredRequestsMapping.get(uri);
				if (!(fetched!=null && !fetched.isEmpty())) return false;
				for (MessageForEveryDataProvider m: fetched) {
					MessageForEveryDataProvider mNew = new MessageForEveryDataProvider();
					mNew = m;
					mNew.setToDelete(true);
					mNew.setSizeOfList(0);
					mNew.setStatus(null);
					mNew.setStatusMessage("");
					mNew.setLastHarvestingTime(null);
					messages.add(mNew);
				}
				registeredRequestsMapping.put(uri, messages);
				RWActions.writeToFile(WriteUrls.getWriteUrlsInstance());
				return true;
			}
		}
	}
	
	public void hardRremoveFromRegisteredRequests( Set<String> urisToDelete) {
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			synchronized (this.registeredRequestsMapping) {
				Set<String> collectionIdsToDelete = new HashSet<>();
				List<MessageForEveryDataProvider> toKeepMessages = new ArrayList<>();
				
					
					for (String url: urisToDelete) {
						List<MessageForEveryDataProvider> mToDel = this.registeredRequestsMapping.get(url);
						for (MessageForEveryDataProvider mm: mToDel) {
								collectionIdsToDelete.add(mm.getCollectionID());
						}
						this.registeredRequestsMapping.put(url, toKeepMessages);
					}
				RWActions.writeToFile(WriteUrls.getWriteUrlsInstance());
				
				SelectedDatabase.getSelectedDatabaseInstance().removeFromDB(collectionIdsToDelete);
			}
		}
	}
	
	public void setInRequestsMapping(String key, MessageForEveryDataProvider value) {
		synchronized (WriteUrls.getWriteUrlsInstance().LOCK_WRITE) {
			synchronized (registeredRequestsMapping) {
				List<MessageForEveryDataProvider> list = registeredRequestsMapping.get(key);
				if (list == null) list = new ArrayList<MessageForEveryDataProvider>();
				if (!list.isEmpty())
					if (checkExistence(list, value))
						return;
				list.add(value);
				registeredRequestsMapping.put(key, list);
	
				RWActions.writeToFile(WriteUrls.getWriteUrlsInstance());
			}
		}
	}
	
	private boolean checkExistence(List<MessageForEveryDataProvider> list, MessageForEveryDataProvider value) {
		for (MessageForEveryDataProvider m: list) {
			if (m.getInfoForHarvesting().getListRecords()!=null) {
				if (m.getInfoForHarvesting().getListRecords().getMetadataPrefix()!=null && 
					value.getInfoForHarvesting().getListRecords()!=null &&
					value.getInfoForHarvesting().getListRecords().getMetadataPrefix()!=null &&
					m.getInfoForHarvesting().getListRecords().getMetadataPrefix().equals(value.getInfoForHarvesting().getListRecords().getMetadataPrefix())) {
						if (m.getToDelete()==true) m.setToDelete(false);
							return true;
				} 
			} 
		}
		return false;
	}
	
}
