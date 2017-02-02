package gr.uoa.di.madgik.ckan.oaipmh.repository;

import gr.uoa.di.madgik.ckan.oaipmh.metadata.Metadata;
import gr.uoa.di.madgik.ckan.oaipmh.utils.DeletionEnum;
import gr.uoa.di.madgik.ckan.oaipmh.utils.UTCDatetime;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.ListIdentifiers;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.BadResumptionTokenError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.CannotDisseminateFormatError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.IdDoesNotExistError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoMetadataFormatsError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoRecordsMatchError;
import gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoSetHierarchyError;

import java.util.List;

/**
 *
 * @author  
 *
 */
public abstract class Repository {

    protected String requestURL;
    protected String repositoryName;
    protected String earliestDatestamp;
    protected String granularity = "YYYY-MM-DDThh:mm:ssZ";
    protected List<String> adminEmails;
    protected String compression = null;
    protected List<String> descriptions = null;

    protected boolean hasResumptionTokenSupport = false;

    protected DeletionEnum deletedRecord = DeletionEnum.NO;

    public void setHasResumptionTokenSupport(boolean hasResumptionTokenSupport) {
        this.hasResumptionTokenSupport = hasResumptionTokenSupport;
    }

    public boolean hasResumptionTokenSupport() {
        return hasResumptionTokenSupport;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public void setEarliestDatestamp(String earliestDatestamp) {
        this.earliestDatestamp = earliestDatestamp;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }

    public void setAdminEmails(List<String> adminEmails) {
        this.adminEmails = adminEmails;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public void setDeletedRecord(DeletionEnum deletedRecord) {
        this.deletedRecord = deletedRecord;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getEarliestDatestamp() {
        return earliestDatestamp;
    }

    public String getDeletedRecord() {
        return deletedRecord.toString();
    }

    public String getGranularity() {
        return granularity;
    }

    public List<String> getAdminEmails() {
        return adminEmails;
    }

    public String getCompression() {
        return compression;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    /**
     *
     * @return @throws NoMetadataFormatsError There are no metadata formats
     * available for the specified item.
     */
    public abstract List<Metadata> getMetadataFormats()
            throws NoMetadataFormatsError;

    /**
     *
     * @param identifier
     * @return
     * @throws NoMetadataFormatsError There are no metadata formats available
     * for the specified item.
     * @throws IdDoesNotExistError The value of the identifier argument is
     * unknown or illegal in this repository.
     */
    public abstract List<Metadata> getMetadataFormats(String identifier)
            throws NoMetadataFormatsError, IdDoesNotExistError;

    /**
     *
     * @return @throws NoSetHierarchyError The repository does not support sets
     */
    public abstract List<SetSpec> getSetSpecs() throws NoSetHierarchyError;

    /**
     *
     * @param resumptionToken
     * @return
     * @throws NoSetHierarchyError The repository does not support sets
     * @throws BadResumptionTokenError The value of the resumptionToken argument
     * is invalid or expired.
     */
    abstract public List<SetSpec> getSetSpecs(ResumptionToken resumptionToken)
            throws NoSetHierarchyError, BadResumptionTokenError;

    /**
     *
     * @param id
     * @param metadataPrefix
     * @return
     * @throws IdDoesNotExistError The value of the id argument is unknown or
     * illegal in this repository.
     * @throws CannotDisseminateFormatError The value of the metadataPrefix
     * argument is not supported by the repository.
     */
    abstract public Record getRecord(String id, String metadataPrefix)
            throws IdDoesNotExistError, CannotDisseminateFormatError;

    /**
     *
     * @param metadataPrefix
     * @param verb
     * @return
     * @throws CannotDisseminateFormatError The value of the metadataPrefix
     * argument is not supported by the repository.
     * @throws gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoRecordsMatchError
     */
    abstract public List<Record> getRecords(String metadataPrefix, ListIdentifiers verb)
            throws CannotDisseminateFormatError, NoRecordsMatchError;

    /**
     *
     * @param from
     * @param until
     * @param metadataPrefix
     * @return
     * @throws CannotDisseminateFormatError The value of the metadataPrefix
     * argument is not supported by the repository.
     * @throws NoRecordsMatchError The combination of the values of the from,
     * until, set and metadataPrefix arguments results in an empty list.
     */
    abstract public List<Record> getRecords(UTCDatetime from,
            UTCDatetime until, String metadataPrefix)
            throws CannotDisseminateFormatError, NoRecordsMatchError;

    /**
     *
     * @param metadataPrefix
     * @param resumptionToken
     * @return
     * @throws CannotDisseminateFormatError The value of the metadataPrefix
     * argument is not supported by the repository.
     * @throws BadResumptionTokenError The value of the resumptionToken argument
     * is invalid or expired.
     * @throws gr.uoa.di.madgik.ckan.oaipmh.verbs.errors.NoRecordsMatchError
     */
    abstract public List<Record> getRecords(String metadataPrefix,
            ResumptionToken resumptionToken)
            throws CannotDisseminateFormatError, BadResumptionTokenError, NoRecordsMatchError;

    /**
     *
     * @param metadataPrefix
     * @param set
     * @param verb
     * @return
     * @throws CannotDisseminateFormatError The value of the metadataPrefix
     * argument is not supported by the repository.
     * @throws NoSetHierarchyError The repository does not support sets
     */
    abstract public List<Record> getRecords(String metadataPrefix, SetSpec set, ListIdentifiers verb)
            throws CannotDisseminateFormatError, NoSetHierarchyError;

    /**
     *
     * @param metadataPrefix
     * @param resumptionToken
     * @param set
     * @return
     * @throws CannotDisseminateFormatError The value of the metadataPrefix
     * argument is not supported by the repository.
     * @throws NoSetHierarchyError The repository does not support sets
     * @throws BadResumptionTokenError The value of the resumptionToken argument
     * is invalid or expired.
     */
    abstract public List<Record> getRecords(String metadataPrefix,
            ResumptionToken resumptionToken, SetSpec set)
            throws CannotDisseminateFormatError, NoSetHierarchyError,
            BadResumptionTokenError;

    /**
     *
     * @param from
     * @param until
     * @param metadataPrefix
     * @param resumptionToken
     * @param set
     * @return
     * @throws CannotDisseminateFormatError The value of the metadataPrefix
     * argument is not supported by the repository.
     * @throws NoRecordsMatchError The combination of the values of the from,
     * until, set and metadataPrefix arguments results in an empty list.
     * @throws NoSetHierarchyError The repository does not support sets
     * @throws BadResumptionTokenError The value of the resumptionToken argument
     * is invalid or expired.
     */
    abstract public List<Record> getRecords(UTCDatetime from,
            UTCDatetime until, String metadataPrefix,
            ResumptionToken resumptionToken, SetSpec set)
            throws CannotDisseminateFormatError, NoRecordsMatchError,
            NoSetHierarchyError, BadResumptionTokenError;

    abstract public void closeConnection();
}
