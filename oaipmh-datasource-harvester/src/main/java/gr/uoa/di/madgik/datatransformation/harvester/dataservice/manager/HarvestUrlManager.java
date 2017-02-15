package gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.Verbs;

import gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger.FetchAllInfoMessenger;
import gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger.InfoForPopulation;
import gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger.RegisteredUrlsInfoMessenger;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive.RegisteredRequests;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.queue.QueuedRequests;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.CustomTimes;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.DefaultTime;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.TimesReader;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.times.WriteUrls;
import gr.uoa.di.madgik.datatransformation.harvester.utils.GetCurrentDateTimestamp;

public class HarvestUrlManager {

	public static List<Verbs> fetchVerbs() {
		List<Verbs> verbs = new ArrayList<Verbs>();
		for (Verbs verb : Verbs.values())
			verbs.add(verb);
		return verbs;
	}

	public static FetchAllInfoMessenger fetchInfoForRegisteredUris() {
		List<RegisteredUrlsInfoMessenger> info = new ArrayList<>();

		Map<String, List<MessageForEveryDataProvider>> map = new HashMap<String, List<MessageForEveryDataProvider>>();
		map = RegisteredRequests.getRegisteredRequestsInstance().getRegisteredRequestsMapping();
		for (Map.Entry<String, List<MessageForEveryDataProvider>> node : map.entrySet()) {
			RegisteredUrlsInfoMessenger registeredUrlsInfo = new RegisteredUrlsInfoMessenger();
			if (node.getValue() == null)
				continue;
			if (node.getValue().isEmpty())
				continue;
			if (node.getValue().get(0).getToDelete())
				continue;

			for (MessageForEveryDataProvider mm : node.getValue()) {
				registeredUrlsInfo.getMessage().add(mm);
			}
			if (registeredUrlsInfo.getMessage().isEmpty())
				continue;

			registeredUrlsInfo.setUri(node.getKey());
			CustomTimes t;
			if ((t = TimesReader.getTimesReaderInstance().getFromTimesMapping(node.getKey())) != null) {
				registeredUrlsInfo.setTime(t.getTime());
				registeredUrlsInfo.setTimeUnit(t.getTimeUnit().toString());
			} else {
				registeredUrlsInfo.setTime(TimesReader.getTimesReaderInstance().getConfiguredTime().getTime());
				registeredUrlsInfo
						.setTimeUnit(TimesReader.getTimesReaderInstance().getConfiguredTime().getTimeUnit().toString());
			}
			info.add(registeredUrlsInfo);
		}

		List<InfoForPopulation> toPopulate = new ArrayList<InfoForPopulation>();
		for (RegisteredUrlsInfoMessenger rl : info) {
			List<MessageForEveryDataProvider> messages = rl.getMessage();
			for (MessageForEveryDataProvider m : messages) {
				if (m.getToDelete())
					continue;
				InfoForPopulation ip = new InfoForPopulation();
				if (m.getAddInfoR() == null || m.getAddInfoR().getRepositoryName() == null)
					ip.setName(m.getInfoForHarvesting().getUrl());
				else
					ip.setName(m.getAddInfoR().getRepositoryName());

				if (m.getAddInfoHP() != null && m.getAddInfoHP().getServiceUnavailable()) {
					ip.setStatus(false);
					ip.setStatusMessage(m.getStatusMessage());
				} else {
					if (m.getRetryAfter() != null) {
						ip.setStatus(false);
						ip.setStatusMessage("Data Publisher needs " + m.getRetryAfter().getTime()
								+ m.getRetryAfter().getTimeUnit() + " to publish metadata");
					} else {
						if (m.isExecuting()) {
							ip.setStatus(true);
							ip.setStatusMessage("End point is harvested");
						} else if (m.isForceReharvest()) {
							ip.setStatus(true);
							ip.setStatusMessage("End point is about to reharvest when resources will be available");
						} else {
							ip.setStatus(false);
							ip.setStatusMessage(m.getStatusMessage());
						}
					}
				}
				ip.setNumberOfRecords(m.getSizeOfList());
				ip.setIntervalTime(String.valueOf(rl.getTime()));
				ip.setTimeUnit(rl.getTimeUnit());
				if (m.getLastHarvestingTime() != null)
					ip.setLastHarvestingTime(
							GetCurrentDateTimestamp.convertTo(m.getLastHarvestingTime(), "yyyy-MM-dd'T'hh:mm:ss"));
				else
					ip.setLastHarvestingTime(" - ");
				ip.setUrl(m.getInfoForHarvesting().getUrl());
				ip.setSchema(m.getInfoForHarvesting().getListRecords().getMetadataPrefix());
				toPopulate.add(ip);
			}
		}

		info = new ArrayList<>();
		for (Map.Entry<String, List<MessageForEveryDataProvider>> node : QueuedRequests.getQueuedRequestsInstance()
				.getQueuedRequestsMapping().entrySet()) {
			if (node.getValue().get(0).getToDelete())
				continue;

			RegisteredUrlsInfoMessenger registeredUrlsInfo = new RegisteredUrlsInfoMessenger();
			registeredUrlsInfo.setUri(node.getKey());

			for (MessageForEveryDataProvider m : node.getValue()) {
				if (m.getToDelete())
					continue;

				CustomTimes t;
				if ((t = TimesReader.getTimesReaderInstance().getFromTimesMapping(node.getKey())) != null) {
					registeredUrlsInfo.setTime(t.getTime());
					registeredUrlsInfo.setTimeUnit(t.getTimeUnit().toString());
				} else {
					registeredUrlsInfo.setTime(TimesReader.getTimesReaderInstance().getConfiguredTime().getTime());
					registeredUrlsInfo.setTimeUnit(
							TimesReader.getTimesReaderInstance().getConfiguredTime().getTimeUnit().toString());
				}
				registeredUrlsInfo.getMessage().add(m);
			}
			info.add(registeredUrlsInfo);
		}

		for (RegisteredUrlsInfoMessenger rl : info) {
			List<MessageForEveryDataProvider> messages = rl.getMessage();
			for (MessageForEveryDataProvider m : messages) {
				if (m.getToDelete())
					continue;
				InfoForPopulation ip = new InfoForPopulation();
				if (m.getAddInfoR() == null || m.getAddInfoR().getRepositoryName() == null)
					ip.setName(m.getInfoForHarvesting().getUrl());
				else
					ip.setName(m.getAddInfoR().getRepositoryName());

				if (m.getAddInfoR() != null && m.getAddInfoHP().getServiceUnavailable()) {
					ip.setStatus(false);
					ip.setStatusMessage(m.getStatusMessage());
				} else {
					if (m.getRetryAfter() != null) {
						ip.setStatus(false);
						ip.setStatusMessage("Data Publisher needs " + m.getRetryAfter().getTime()
								+ m.getRetryAfter().getTimeUnit() + " to publish metadata");
					} else {
						if (m.getStatus() != null && m.getStatusMessage() != null) {
							ip.setStatus(false);
							ip.setStatusMessage(m.getStatusMessage());
						} else {
							if (m.isExecuting()) {
								ip.setStatus(true);
								ip.setStatusMessage("End point is harvested");
							} else if (m.isForceReharvest()) {
								ip.setStatus(true);
								ip.setStatusMessage("End point is about to reharvest when resources will be available");
							} else {
								ip.setStatus(false);
								ip.setStatusMessage("End point is !harvested");
							}
						}
					}
				}

				ip.setNumberOfRecords(m.getSizeOfList());
				ip.setIntervalTime(String.valueOf(rl.getTime()));
				ip.setTimeUnit(rl.getTimeUnit());
				if (m.getLastHarvestingTime() != null)
					ip.setLastHarvestingTime(
							GetCurrentDateTimestamp.convertTo(m.getLastHarvestingTime(), "yyyy-MM-dd'T'hh:mm:ss"));
				else
					ip.setLastHarvestingTime("Has not harvested yet.");
				ip.setUrl(m.getInfoForHarvesting().getUrl());
				ip.setSchema(m.getInfoForHarvesting().getListRecords().getMetadataPrefix());
				toPopulate.add(ip);
			}
		}

		FetchAllInfoMessenger fetchAllInfoMessenger = new FetchAllInfoMessenger();
		fetchAllInfoMessenger.getInfo().addAll(toPopulate);
		fetchAllInfoMessenger.setDt(TimesReader.getTimesReaderInstance().getConfiguredTime());

		return fetchAllInfoMessenger;
	}

	public static ReposInfoMessenger fetchInfoForEachRepo() {
		ReposInfoMessenger info = new ReposInfoMessenger();

		Map<String, List<MessageForEveryDataProvider>> mappingArchive = new HashMap<String, List<MessageForEveryDataProvider>>();
		mappingArchive = RegisteredRequests.getRegisteredRequestsInstance().getRegisteredRequestsMapping();
		for (Map.Entry<String, List<MessageForEveryDataProvider>> node : mappingArchive.entrySet()) {
			List<MessageForEveryDataProvider> messagesForEveryDataProvider = node.getValue();
			List<HashMap<String, String>> listOfInfos = new ArrayList<>();

			for (MessageForEveryDataProvider m : messagesForEveryDataProvider) {

				HashMap<String, String> infoPerUri = new HashMap<>();
				infoPerUri.put("uri", node.getKey());
				if (m.getLastHarvestingTime() != null)
					infoPerUri.put("last_harvesting_time", m.getLastHarvestingTime().toString());
				else
					infoPerUri.put("last_harvesting_time", "");
				if (m.getMetadataPrefix() != null)
					infoPerUri.put("metadata_prefix", m.getMetadataPrefix().toString());
				else
					infoPerUri.put("metadata_prefix", "");

				if (m.getAddInfoHP() != null) {
					if (m.getAddInfoHP().getNoRecords())
						infoPerUri.put("has_no_records", "true");
					else
						infoPerUri.put("has_no_records", "false");
					if (m.getAddInfoHP().getSupportsDateQuery())
						infoPerUri.put("supports_date_queries", "true");
					else
						infoPerUri.put("supports_date_queries", "false");
					if (m.getAddInfoHP().getServiceUnavailable())
						infoPerUri.put("service_is_unavailable", "true");
					else
						infoPerUri.put("service_is_unavailable", "false");
				} else {
					infoPerUri.put("has_no_records", "null");
					infoPerUri.put("supports_date_queries", "null");
					infoPerUri.put("service_is_unavailable", "null");
				}
				infoPerUri.put("size_of_list", Integer.toString(m.getSizeOfList()));
				listOfInfos.add(infoPerUri);
			}
			info.putInfo(node.getKey(), listOfInfos);
		}
		return info;
	}

	public static List<String> fetchUris() {
		List<String> uris = new ArrayList<>();
		for (Map.Entry<String, List<MessageForEveryDataProvider>> mapping : RegisteredRequests
				.getRegisteredRequestsInstance().getRegisteredRequestsMapping().entrySet()) {
			if (!mapping.getValue().isEmpty())
				if (!mapping.getValue().get(0).getToDelete()) {
					for (MessageForEveryDataProvider m : mapping.getValue()) {
						if (!uris.contains(m.getInfoForHarvesting().getUrl()))
							uris.add(mapping.getKey());
					}
				}
		}
		return uris;
	}

	public static boolean deleteUri(String uri) {
		if (!RegisteredRequests.getRegisteredRequestsInstance().removeFromRegisteredRequests(uri))
			return false;
		else
			return true;
	}

	public static boolean reharvestUri(String uri, String metadataPrefix) {
		return RegisteredRequests.getRegisteredRequestsInstance().reharvestUri(uri, metadataPrefix);
	}

	public static boolean editRegisteredUri(String url, Integer newTime, String timeUnit, String defaultTime,
			String defaultTimeUnit) {
		TimeUnit defTU = null;
		TimeUnit tU = null;
		boolean returnTrue = false;
		if ((RegisteredRequests.getRegisteredRequestsInstance().getFromRegisteredRequestsMapping(url)) != null) {
			synchronized (TimesReader.class) {
				CustomTimes ct = TimesReader.getTimesReaderInstance().getFromTimesMapping(url);
				DefaultTime dt = TimesReader.getTimesReaderInstance().getConfiguredTime();

				if (defaultTime == null)
					defaultTime = String.valueOf(dt.getTime());

				if (defaultTime != null && !defaultTime.isEmpty() && defaultTimeUnit != null
						&& !defaultTimeUnit.isEmpty()) {
					if (defaultTimeUnit.toUpperCase().equals(TimeUnit.DAYS.toString()))
						defTU = TimeUnit.DAYS;
					else if (defaultTimeUnit.toUpperCase().equals(TimeUnit.HOURS.toString()))
						defTU = TimeUnit.HOURS;
					else if (defaultTimeUnit.toUpperCase().equals(TimeUnit.MINUTES.toString()))
						defTU = TimeUnit.MINUTES;
					else
						defTU = TimeUnit.DAYS;
				}
				if (timeUnit != null && !timeUnit.isEmpty()) {
					if (timeUnit.toUpperCase().equals(TimeUnit.DAYS.toString()))
						tU = TimeUnit.DAYS;
					else if (timeUnit.toUpperCase().equals(TimeUnit.HOURS.toString()))
						tU = TimeUnit.HOURS;
					else if (timeUnit.toUpperCase().equals(TimeUnit.MINUTES.toString()))
						tU = TimeUnit.MINUTES;
					else
						tU = TimeUnit.DAYS;
				}

				if (dt.getTime() != Integer.parseInt(defaultTime) || dt.getTimeUnit() != defTU) {
					DefaultTime newDefaultTime = new DefaultTime(Integer.parseInt(defaultTime), defTU);
					TimesReader.getTimesReaderInstance().setConfiguredTime(newDefaultTime);

					synchronized (WriteUrls.class) {
						WriteUrls.writeToFile();
					}

					dt = TimesReader.getTimesReaderInstance().getConfiguredTime();
					returnTrue = true;
				}
				/* new custom time is the same with defaulttime */
				if (dt.getTime() == newTime && dt.getTimeUnit() == tU) {
					if (ct != null) {
						TimesReader.getTimesReaderInstance().removeFromTimesMapping(url);

						synchronized (WriteUrls.class) {
							WriteUrls.writeToFile();
						}
						returnTrue = true;
					}
				} else {
					// url has custom time
					CustomTimes newCT = new CustomTimes(url, newTime, tU);
					TimesReader.getTimesReaderInstance().addToTimesMapping(newCT);

					synchronized (WriteUrls.class) {
						WriteUrls.writeToFile();
					}
					returnTrue = true;
				}
			}
		}
		return returnTrue;
	}

}