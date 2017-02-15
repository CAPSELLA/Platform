package gr.uoa.di.madgik.datatransformation.harvester.dataservice.servicecontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import gr.uoa.di.madgik.datatransformation.harvester.core.MessageForEveryDataProvider;
import gr.uoa.di.madgik.datatransformation.harvester.core.requestedtypes.verbs.Verbs;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.HarvestUrlManager;
import gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.ReposInfoMessenger;
import gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger.EditInfoMessenger;
import gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger.FetchAllInfoMessenger;
import gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger.MinorMessenger;
import gr.uoa.di.madgik.datatransformation.harvester.dataservice.manager.messenger.NewServiceMessenger;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.archive.RegisteredRequests;
import gr.uoa.di.madgik.datatransformation.harvester.filesmanagement.queue.QueuedRequests;
import gr.uoa.di.madgik.datatransformation.harvester.requestmanagement.RequestManager;
import gr.uoa.di.madgik.datatransformation.harvester.responsesofservice.ServiceResponse;
import gr.uoa.di.madgik.datatransformation.harvester.utils.GetProperties;


@Controller
@RequestMapping("/harvesturl")
public class HarvestUrlController {

	@RequestMapping(value = "/harvester", method = RequestMethod.GET)
	public String harvester(Model model) {
		return "harvester";
	}
	
	@RequestMapping(value = "/registerurl/getVerbs", method = RequestMethod.GET)
	public @ResponseBody List<Verbs> fetchVerbs(HttpServletRequest request)
			throws Exception {
		return HarvestUrlManager.fetchVerbs();
	}
	//*****************TO remove********************************************************************
	@RequestMapping(value = "/registerurl/getUrlForService", method = RequestMethod.GET)
	public @ResponseBody List<String>  fetchUrlForService(HttpServletRequest request) {
		List<String> newL = new ArrayList<>();
		newL.add(GetProperties.getPropertiesInstance().getNewServiceUrlParameters());
		return newL;
	}
	
	@RequestMapping(value = "/registerurl/getUrlForDeleteService", method = RequestMethod.GET)
	public @ResponseBody List<String>  fetchUrlForDeleteService(HttpServletRequest request) {
		List<String> newL = new ArrayList<>();
		newL.add(GetProperties.getPropertiesInstance().getDeleteServiceUrlParameters());
		return newL;
	}
	//************************************************************************************************
	
	@RequestMapping(value = "/registerurl/getInfoForRegisteredUris", method = RequestMethod.GET)
	public @ResponseBody ServiceResponse  fetchInfoForRegisteredUris(HttpServletRequest request)
			throws Exception {
	
		FetchAllInfoMessenger messenger = HarvestUrlManager.fetchInfoForRegisteredUris();
		return (messenger.getInfo().isEmpty()? new ServiceResponse(false, null, "Not data sources"): new ServiceResponse(true, messenger, "Info returned"));
	}

	@RequestMapping(value = "/registerurl/getUris", method = RequestMethod.GET)
	public @ResponseBody List<String> fetchUris(HttpServletRequest request)
			throws Exception {	
		if(HarvestUrlManager.fetchUris().isEmpty())
		{
			List<String> list = new ArrayList();
			list.add("No registered Uris");
			return list;
		}
		
		return HarvestUrlManager.fetchUris();
	}
	
	@RequestMapping(value = "/registerurl/deleteUri", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody ServiceResponse deleteRegisteredUri(@RequestBody String uri, HttpServletRequest request)
			throws Exception {
		
		if (HarvestUrlManager.deleteUri(uri))
			return new ServiceResponse(true, null, "uri deleted");
		else return new ServiceResponse(false, null, "uri not found");
	}
	
	@RequestMapping(value = "/registerurl/registerUri", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody ServiceResponse registereUri(@RequestBody NewServiceMessenger nsm, HttpServletRequest request)
			throws Exception {
		
		if (nsm.getNewUri()==null || nsm.getNewUri().trim().isEmpty()) return new ServiceResponse(false, null, "URI was not specified");
		
		String regex = "^http(\\w)*://";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(nsm.getNewUri().trim());
		if (!matcher.find())
			return new ServiceResponse(false, null, "Uri is not absolute");
		
		if (!nsm.getNewIntervalTime().trim().isEmpty()) {
			try {
				if (!nsm.getDefaultTime()) {
					Integer.parseInt(nsm.getNewIntervalTime());
					if (!(nsm.getNewTimeUnit().toUpperCase().equals("DAYS") || nsm.getNewTimeUnit().toUpperCase().equals("HOURS") || nsm.getNewTimeUnit().toUpperCase().equals("MINUTES")))
						return new ServiceResponse(false, null, "TimeUnit has not an appropriate format");
				}
			} catch (NumberFormatException e) {
				return new ServiceResponse(false, null, "TimeUnit has not an appropriate format");
			}
		}
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("harvest-new-service", "true");
		parameters.put("newUri", nsm.getNewUri().trim());
		parameters.put("schema", nsm.getSchema().trim());
		parameters.put("defaultTime", nsm.getDefaultTime().toString());
		parameters.put("newIntervalTime", nsm.getNewIntervalTime().trim());
		parameters.put("newTimeUnit", nsm.getNewTimeUnit().trim());
		
		List<MessageForEveryDataProvider> registered = null;
		if (((registered=RegisteredRequests.getRegisteredRequestsInstance().getFromRegisteredRequestsMapping(nsm.getNewUri().trim()))!=null) &&
			  !registered.isEmpty()) {
			RegisteredRequests.getRegisteredRequestsInstance().setToDeleteFalse(nsm.getNewUri().trim());

			if (RegisteredRequests.getRegisteredRequestsInstance().checkExistence(nsm.getNewUri().trim(), nsm.getSchema().trim()))
				return new ServiceResponse(true, null, "URI is already registered");
		}
		
		synchronized (QueuedRequests.class) {
			List<MessageForEveryDataProvider> queued = null;
			if (((queued = QueuedRequests.getQueuedRequestsInstance().getFromQueuedRequestsMapping(nsm.getNewUri().trim()))!=null) &&
				  !queued.isEmpty())
				if (!queued.get(0).getToDelete()) {
					for (MessageForEveryDataProvider mm: queued) {
						if ((nsm.getSchema().trim().equals("all") && mm.getSchemaSupportedForUrl().equals("all")) ||
							(nsm.getSchema().trim().equals("dc") && mm.getSchemaSupportedForUrl().equals("dc"))||
							(nsm.getSchema().trim().equals("dc") && mm.getSchemaSupportedForUrl().equals("all")))
							return new ServiceResponse(true, null, "URI is already registered");
					}
				}
		}
		
		try {
			RequestManager.proceedToRegister(parameters);
		} catch (Exception e) {
			return new ServiceResponse(false, null, "URI was not registered -- uri was not specified correctly");
		}
		return new ServiceResponse(true, null, "URI was registered");
	}
	
	@RequestMapping(value = "/registerurl/reharvestUri", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody Boolean reharvestRegisteredUri(@RequestBody MinorMessenger mm, HttpServletRequest request)
			throws Exception {
		
		return HarvestUrlManager.reharvestUri(mm.getUrl(), mm.getMetadataPrefix());
	}
	
	@RequestMapping(value = "/registerurl/editRegUri", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody Boolean editRegisteredUri(@RequestBody EditInfoMessenger editInfoM, HttpServletRequest request)
			throws Exception {
		
		if ((editInfoM.getTime()==null && editInfoM.getTimeUnit()== null) || (editInfoM.getDefaultTime()==null && editInfoM.getDefaultTimeUnit()==null))
			return false;
		int newtime;
		try {
			newtime = Integer.parseInt(editInfoM.getTime());
		} catch (NumberFormatException e) {
			return false;
		}
		if (editInfoM.getUrl()==null || editInfoM.getUrl().isEmpty())
			return false;

		return HarvestUrlManager.editRegisteredUri(editInfoM.getUrl().trim(), newtime, 
				 editInfoM.getTimeUnit(), editInfoM.getDefaultTime(), 
				 editInfoM.getDefaultTimeUnit());
	}
	
	@RequestMapping(value = "/registerurl/getInfoForEachRepo", method = RequestMethod.GET)
	public @ResponseBody ReposInfoMessenger fetchInfoForEachRepo(HttpServletRequest request)
			throws Exception {
		ReposInfoMessenger r = HarvestUrlManager.fetchInfoForEachRepo();
		if(r == null)
			return new ReposInfoMessenger();
		return HarvestUrlManager.fetchInfoForEachRepo();
	}
	
}