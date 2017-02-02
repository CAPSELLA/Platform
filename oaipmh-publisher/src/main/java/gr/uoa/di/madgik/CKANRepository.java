package gr.uoa.di.madgik;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import eu.trentorise.opendata.jackan.model.CkanDataset;
import eu.trentorise.opendata.jackan.model.CkanOrganization;
import eu.trentorise.opendata.jackan.model.CkanResource;
import eu.trentorise.opendata.jackan.model.CkanTag;
import gr.uoa.di.madgik.ckan.oaipmh.metadata.Metadata;
import gr.uoa.di.madgik.ckan.oaipmh.metadata.OAIDCItem;
import gr.uoa.di.madgik.ckan.oaipmh.repository.Record;
import gr.uoa.di.madgik.ckan.oaipmh.repository.Repository;
import gr.uoa.di.madgik.ckan.oaipmh.repository.ResumptionToken;
import gr.uoa.di.madgik.ckan.oaipmh.repository.SetSpec;
import gr.uoa.di.madgik.ckan.oaipmh.utils.DeletionEnum;
import gr.uoa.di.madgik.ckan.oaipmh.utils.UTCDatetime;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.ListIdentifiers;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadResumptionTokenError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.CannotDisseminateFormatError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.IdDoesNotExistError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoMetadataFormatsError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoRecordsMatchError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoSetHierarchyError;


/**
 *
 * @author  
 */
public class CKANRepository extends Repository {

    private static final Logger logger = Logger.getLogger(CKANRepository.class);
    private CKANAdapterAPI ckanAdapter;
    private final String language;
    private final int daysToExpireForResumptionToken;

    @Inject
    public CKANRepository(CKANAdapterAPI ckanAdapter) {

        logger.debug("Create CKANRepository");
        InputStream stream = null;
		try {
			stream = CKANRepository.class.getClassLoader().getResourceAsStream("repository.properties");
		} catch (Exception e) {
			logger.fatal("cannot find repository.properties file");
		};

        Properties props = new Properties();

        try {
            props.load(stream);
        } catch (IOException ex) {
            logger.error("failed to load the properties file", ex);

        }

        repositoryName = props.getProperty("name").trim();

        language = props.getProperty("language").trim();

        daysToExpireForResumptionToken = Integer.parseInt(props.getProperty("daysToExpireForResumptionToken").trim());

        adminEmails = new ArrayList<String>();

        for (String email : props.getProperty("adminEmails").split(",")) {
            adminEmails.add(email);
        }
        
        requestURL = props.getProperty("baseUrl").trim();
	
        this.ckanAdapter = ckanAdapter;
        hasResumptionTokenSupport = true;
        deletedRecord = DeletionEnum.NO;
        this.earliestDatestamp = "2013-10-04T14:58:31Z";
    }

    @Override
    public List<Metadata> getMetadataFormats() throws NoMetadataFormatsError {
        List<Metadata> metaList = new ArrayList<Metadata>();
        metaList.add(new OAIDCItem());
        return metaList;
    }

    @Override
    public List<Metadata> getMetadataFormats(String identifier) throws NoMetadataFormatsError, IdDoesNotExistError {
        logger.debug("getMetadataFormats");
        try {
            CkanDataset dataset = ckanAdapter.getDataset(identifier);
            if (dataset == null) {
                throw new IdDoesNotExistError();
            } else {
                List<Metadata> metaList = new ArrayList<Metadata>();
                metaList.add(new OAIDCItem());
                return metaList;
            }

        } catch (Exception e) {
            throw new IdDoesNotExistError();
        }
    }

    @Override
    public List<SetSpec> getSetSpecs() throws NoSetHierarchyError {
        logger.debug("getSetSpecs");

        List<CkanOrganization> organizations = ckanAdapter.getOrganizations();

        List<SetSpec> specs = new ArrayList<SetSpec>();

        for (CkanOrganization organization : organizations) {
            specs.add(createSetSpec(organization));
        }
        if (specs.isEmpty()) {
            throw new NoSetHierarchyError();
        }
        return specs;
    }

    @Override
    public List<SetSpec> getSetSpecs(ResumptionToken resumptionToken) throws NoSetHierarchyError, BadResumptionTokenError {
        throw new BadResumptionTokenError();
    }

    @Override
    public Record getRecord(String id, String metadataPrefix) throws IdDoesNotExistError, CannotDisseminateFormatError {
        logger.debug("getRecord");

        try {
            if (!getMetadataFormats().get(0).getPrefix().equals(metadataPrefix)) {
                throw new CannotDisseminateFormatError();
            }
        } catch (NoMetadataFormatsError e1) {
            throw new CannotDisseminateFormatError();
        }
        try {
            CkanDataset dataset = ckanAdapter.getDataset(id);
            if (dataset == null) {
                throw new IdDoesNotExistError();
            }
            return datasetToRecord(dataset, metadataPrefix);
        } catch (Exception e) {
            throw new IdDoesNotExistError();
        }
    }

    @Override
    public List<Record> getRecords(final String metadataPrefix, ListIdentifiers verb) throws CannotDisseminateFormatError,
            NoRecordsMatchError {
        logger.debug("getRecords");

        int completeListSize;

        try {
            if (!getMetadataFormats().get(0).getPrefix().equals(metadataPrefix)) {
                throw new CannotDisseminateFormatError();
            }
        } catch (NoMetadataFormatsError e1) {
            throw new CannotDisseminateFormatError();
        }
        try {
            List<CkanDataset> datasets = ckanAdapter.getDatasets();

            if (datasets.size() < 1) {
                throw new NoRecordsMatchError();
            }
            List<Record> records = new ArrayList<Record>();
            for (CkanDataset dataset : datasets) {
                Record r = datasetToRecord(dataset, metadataPrefix);
                records.add(r);

            }
            completeListSize = ckanAdapter.getDatasetsNumber();

            if (completeListSize > datasets.size()) {
                //create resumptionToken
                ResumptionToken token = new ResumptionToken();

                ZonedDateTime time = ZonedDateTime.now(ZoneOffset.UTC);
                time = time.plusDays(daysToExpireForResumptionToken);
                UTCDatetime expirationDate = new UTCDatetime(time);
                token.setCompleteListSize(completeListSize);
                token.setCursor(0);
                token.setExpirationDate(expirationDate);

                verb.setResumptionToken(token.getResumptionToken());
            }

            return records;
        } catch (Exception e) {
            throw new NoRecordsMatchError();
        }
    }

    @Override
    public List<Record> getRecords(UTCDatetime from, UTCDatetime until, String metadataPrefix)
            throws CannotDisseminateFormatError, NoRecordsMatchError {
        throw new NoRecordsMatchError();
    }

    @Override
    public List<Record> getRecords(String metadataPrefix, ResumptionToken resumptionToken)
            throws CannotDisseminateFormatError, BadResumptionTokenError, NoRecordsMatchError {
        logger.debug("getRecords with resumptionToken");

        try {
            if (!getMetadataFormats().get(0).getPrefix().equals(metadataPrefix)) {
                throw new CannotDisseminateFormatError();
            }
        } catch (NoMetadataFormatsError e1) {
            throw new CannotDisseminateFormatError();
        }
        try {

            List<CkanDataset> datasets = ckanAdapter.getDatasets(resumptionToken.getCursor());

            if (datasets.size() < 1) {
                throw new NoRecordsMatchError();
            }

            List<Record> records = new ArrayList<Record>();
            for (CkanDataset dataset : datasets) {
                Record r = datasetToRecord(dataset, metadataPrefix);
                records.add(r);

            }
            return records;
        } catch (Exception e) {
            throw new NoRecordsMatchError();
        }

    }

    @Override
    public List<Record> getRecords(String metadataPrefix, SetSpec set, ListIdentifiers verb)
            throws CannotDisseminateFormatError, NoSetHierarchyError {
        logger.debug("getRecords with set");
        
        int completeListSize;

        try {
            if (!getMetadataFormats().get(0).getPrefix().equals(metadataPrefix)) {
                throw new CannotDisseminateFormatError();
            }
        } catch (NoMetadataFormatsError e1) {
            throw new CannotDisseminateFormatError();
        }
        try {
            List<CkanDataset> datasets = ckanAdapter.getDatasets(set.toString());

            if (datasets.size() < 1) {
                throw new NoSetHierarchyError();
            }
            List<Record> records = new ArrayList<Record>();
            for (CkanDataset dataset : datasets) {

                Record r = datasetToRecord(dataset, metadataPrefix);
                records.add(r);

            }
            completeListSize = ckanAdapter.getDatasetsNumber(set.toString());

            if (completeListSize > datasets.size()) {
                //create resumptionToken
                ResumptionToken token = new ResumptionToken();

                ZonedDateTime time = ZonedDateTime.now(ZoneOffset.UTC);
                time = time.plusDays(daysToExpireForResumptionToken);
                UTCDatetime expirationDate = new UTCDatetime(time);
                token.setCompleteListSize(completeListSize);
                token.setCursor(0);
                token.setExpirationDate(expirationDate);

                verb.setResumptionToken(token.getResumptionToken());
            }

            return records;
        } catch (Exception e) {
            throw new NoSetHierarchyError();
        }
    }

    @Override
    public List<Record> getRecords(String metadataPrefix, ResumptionToken resumptionToken, SetSpec set)
            throws CannotDisseminateFormatError, NoSetHierarchyError, BadResumptionTokenError {
                logger.debug("getRecords with set");
        logger.debug("getRecords with set and resumptionToken");

        try {
            if (!getMetadataFormats().get(0).getPrefix().equals(metadataPrefix)) {
                throw new CannotDisseminateFormatError();
            }
        } catch (NoMetadataFormatsError e1) {
            throw new CannotDisseminateFormatError();
        }
        try {

            List<CkanDataset> datasets = ckanAdapter.getDatasets(set.toString(), resumptionToken.getCursor());

            if (datasets.size() < 1) {
                throw new NoSetHierarchyError();
            }

            List<Record> records = new ArrayList<Record>();
            for (CkanDataset dataset : datasets) {
                Record r = datasetToRecord(dataset, metadataPrefix);
                records.add(r);

            }
            return records;
        } catch (Exception e) {
            throw new NoSetHierarchyError();
        }
    }

    @Override
    public List<Record> getRecords(UTCDatetime from, UTCDatetime until, String metadataPrefix,
            ResumptionToken resumptionToken, SetSpec set)
            throws CannotDisseminateFormatError, NoRecordsMatchError, NoSetHierarchyError, BadResumptionTokenError {
        throw new NoRecordsMatchError();
    }

    private Record datasetToRecord(CkanDataset dataset, String metadataPrefix) throws Exception {
        logger.debug("datasetToRecord");
        
        if (dataset == null) {
            return null;
        }

        Record record = new Record(dataset.getName(), metadataPrefix);

        List<SetSpec> setSpecs = new ArrayList<SetSpec>();

        setSpecs.add(createSetSpec(dataset.getOrganization()));

        record.setSetSpecs(setSpecs);

        // metadata of record
        OAIDCItem dc = new OAIDCItem();

        dc.setTitle(dataset.getTitle());
        dc.setCreator(dataset.getAuthor());

        List<String> subjects = new ArrayList<String>();

        for (CkanTag tag : dataset.getTags()) {
            subjects.add(tag.getDisplayName());
        }
        dc.setSubjects(subjects);

        dc.setDescription(dataset.getNotes());
        dc.setPublisher(dataset.getAuthor());
        dc.setContributor(dataset.getMaintainer());

        dc.setDate(get_data(dataset));

        dc.setType(dataset.getType());

        List<String> formats = new ArrayList<String>();
        List<String> sources = new ArrayList<String>();
        for (CkanResource resource : dataset.getResources()) {
            if (!formats.contains(resource.getFormat())) {
                formats.add(resource.getFormat());
            }
            sources.add(resource.getUrl());
        }
        dc.setFormats(formats);
        dc.setSources(sources);

        dc.setIdentifier(dataset.getName());

        dc.setLanguage(language);
        dc.setRights(dataset.getLicenseTitle());

        record.setMetadata(dc);

        return record;
    }

    public String get_data(CkanDataset dataset) {
        logger.debug("get_data");
        
        String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS z";
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat isoFormatter = new SimpleDateFormat(ISO_FORMAT);

        isoFormatter.setTimeZone(utc);

        Timestamp date = dataset.getMetadataCreated();

        ZonedDateTime zonedDateTime = ZonedDateTime.of(date.toLocalDateTime(), ZoneOffset.UTC);

        return zonedDateTime.toString();
    }

    public SetSpec createSetSpec(CkanOrganization organization) {
        logger.debug("createSetSpec");
        
        SetSpec set = new SetSpec(organization.getName(), organization.getTitle());

        OAIDCItem dc = new OAIDCItem();

        dc.setDescription(organization.getDescription());

        set.setSetDescription(dc);

        return set;
    }

	@Override
	public void closeConnection() {		
	}
}
