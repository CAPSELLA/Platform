package gr.uoa.di.madgik;

import eu.trentorise.opendata.jackan.CheckedCkanClient;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.CkanQuery;
import eu.trentorise.opendata.jackan.exceptions.JackanException;
import eu.trentorise.opendata.jackan.model.CkanDataset;
import eu.trentorise.opendata.jackan.model.CkanOrganization;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author  
 */
public class CKANAdapterAPI {

    private static CkanClient client;
    private String url;
    private String apiKey;
    private int limit;
    private Logger logger = Logger.getLogger(CKANAdapterAPI.class.getName());

    public CKANAdapterAPI(String url, String apiKey, int limit) {
        this.url = url;
        this.apiKey = apiKey;
        this.limit = limit;
    }

    public void createClient() {
        client = new CheckedCkanClient(url, apiKey);
    }

    public String getUrl() {
        return url;
    }

    public CkanDataset getDataset(String id) {
        CkanDataset d;
        try {
            d = client.getDataset(id);
        } catch (JackanException j) {
            logger.debug("there is no dataset with id = "+id);
            return null;
        }
        return d;
    }

    public List<CkanDataset> getDatasets() {
        List<CkanDataset> d;
        try {
            d = client.searchDatasets("", limit, 0).getResults();
        } catch (JackanException j) {
            d = new ArrayList<CkanDataset>();
            logger.debug("there are no datasets");
        }
        return d;
    }

    public List<CkanDataset> getDatasets(String organization) {
        List<CkanDataset> d;
        try {
            CkanQuery query = CkanQuery.filter().byOrganizationName(organization);
            d = client.searchDatasets(query, limit, 0).getResults();
        } catch (JackanException j) {
            d = new ArrayList<CkanDataset>();
            logger.debug("there are no datasets");
        }
        return d;
    }

    public List<CkanDataset> getDatasets(String organization, int offset) {
        List<CkanDataset> d;
        try {
            CkanQuery query = CkanQuery.filter().byOrganizationName(organization);
            d = client.searchDatasets(query, limit, offset).getResults();
        } catch (JackanException j) {
            d = new ArrayList<CkanDataset>();
            logger.debug("there are no datasets");
        }
        return d;
    }

    public List<CkanDataset> getDatasets(int offset) {
        List<CkanDataset> d;
        try {
            d = client.searchDatasets("", limit, offset).getResults();
        } catch (JackanException j) {
            d = new ArrayList<CkanDataset>();
            logger.debug("there are no datasets");
        }
        return d;
    }

    public int getDatasetsNumber() {
        return client.getDatasetList().size();
    }

    public int getDatasetsNumber(String organization) {
        return client.getOrganization(organization).getPackageCount();
    }

    public List<CkanOrganization> getOrganizations() {
        return client.getOrganizationList();

    }

}
