package gr.uoa.di.madgik.service;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import gr.uoa.di.madgik.config.AppConfig;
import gr.uoa.di.madgik.config.AppConfig.GeoserverTemplate;
import gr.uoa.di.madgik.controller.ShapefileController;
import gr.uoa.di.madgik.utils.Configure;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import sun.misc.BASE64Encoder;

@Service
public class GsService {
	
	private static Logger logger = LoggerFactory.getLogger(GsService.class);


	@Override
	public String toString() {
		return "GsRest [restUrl=" + restUrl + ", username=" + username + "]";
	}
	
	
	private GeoserverTemplate geoserver;



	public final String METHOD_DELETE = "DELETE";
	public final String METHOD_GET = "GET";
	public final String METHOD_POST = "POST";

	public final String METHOD_PUT = "PUT";

	private String password;

	private String restUrl;

	private String username;
	
	private String dsName;
	
	private String workspace;
	
	private GeoServerRESTPublisher publisher;
	private GeoServerRESTReader reader;

	

	/**
	 * Creates a {@link GsService} instance to work on a Geoserver that allows anonymous read- and write access.
	 * 
	 * @param gsBaseUrl
	 *            The base URL of Geoserver. Usually ending with "../geoserver"
	 */
	
	@Autowired
	public GsService(AppConfig config) {
		geoserver =  config.getProperties();
		
		String gsBaseUrl = geoserver.getGsBaseUrl();//environment.getRequiredProperty("geoserver.baseUrl");

		logger.debug("I'm creating GsService with baseUrl:"+gsBaseUrl);
		System.out.println("I'm creating GsService with baseUrl:"+gsBaseUrl);
		if (!gsBaseUrl.endsWith("rest")) {
			if (!gsBaseUrl.endsWith("/"))
				gsBaseUrl += "/";
			this.restUrl = gsBaseUrl + "rest";
		}

		this.username = geoserver.getUsername();//environment.getRequiredProperty("geoserver.username");
		this.password = geoserver.getPassword();  //environment.getRequiredProperty("geoserver.password");
		this.workspace = geoserver.getWorkspace(); //environment.getRequiredProperty("geoserver.workspace");
		this.dsName = geoserver.getDsName(); //environment.getRequiredProperty("geoserver.datastore");
		publisher = new GeoServerRESTPublisher( gsBaseUrl, username, password);
		try {
			reader = new GeoServerRESTReader(gsBaseUrl,username,password);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean deleteShapefile(String layerName) {
		return publisher.removeLayer(workspace, layerName);
	}
	

	
	public boolean uploadShape(String datasetName, File zipFile) throws FileNotFoundException, IllegalArgumentException{
		
		return publisher.publishShp(workspace, dsName, datasetName, zipFile);
	}
	
	public String uploadShape(URL zip, String filename) throws IOException {
		
		InputStream os = zip.openStream();
		try {
			InputStreamReader postDataReader = new InputStreamReader(os);
			String returnString = sendRESTstring(METHOD_PUT, "/workspaces/" + workspace + "/datastores/" + dsName
					+ "/file.shp", postDataReader, "application/zip", null);

			return returnString;
		} finally {
			os.close();
		}
	}




	public boolean setDefaultWs(String wsName) throws IOException {
		String xml = "<workspace><name>" + wsName + "</name></workspace>";

		return 200 == sendRESTint(METHOD_PUT, "/workspaces/default.xml", xml);
	}



	public boolean createDatastorePg(String workspace, String dsName, String dsNamespace, String host, String port,
			String db, String user, String pwd, boolean exposePKs) throws IOException {

		String dbType = "postgis";

		return createDbDatastore(workspace, dsName, dsNamespace, host, port, db, user, pwd, dbType, exposePKs);
	}


	public boolean createDbDatastore(String workspace, String dsName, String dsNamespace, String host, String port,
			String db, String user, String pwd, String dbType, boolean exposePKs) throws IOException {

		String exposePKsParamter = "<entry key=\"Expose primary keys\">" + exposePKs + "</entry>";

		String xml = "<dataStore><name>" + dsName + "</name><enabled>true</enabled><connectionParameters><host>" + host
				+ "</host><port>" + port + "</port><database>" + db + "</database><user>" + user + "</user><passwd>"
				+ pwd + "</passwd><dbtype>" + dbType + "</dbtype><namespace>" + dsNamespace + "</namespace>"
				+ exposePKsParamter + "</connectionParameters></dataStore>";

		int returnCode = sendRESTint(METHOD_POST, "/workspaces/" + workspace + "/datastores", xml);
		return 201 == returnCode;
	}

	

	/**
	 * Create a <em>Featuretype</em> based on an existing datastore.
	 * 
	 * @param wsName
	 *            the GeoServer workspace name
	 * @param dsName
	 *            the GeoServer datastore name
	 * @param ftName
	 *            the featureTypeName you want to create, e.g. the name of a PostGIS table or the name of a Shapefile
	 *            (without .shp)
	 * @param srs
	 *            <code>null</code> or <code>EPSG:????</code> syntax.
	 * @param nativeWKT
	 *            <code>null</code> or WKT declaration of the CRS.
	 * 
	 * @return <code>true</code> if the creation was successful.
	 * 
	 * @throws IOException
	 */
	public boolean createFeatureType(String wsName, String dsName, String ftName, String srs, String nativeWKT)
			throws IOException {

		// "<entry key=\"namespace\"><name>" + dsName
		// + "</name></entry>";

		String nameTitleParam = "<name>" + ftName + "</name><title>" + ftName + "</title>";

		String enabledTag = "<enabled>" + true + "</enabled>";

		String srsTag = srs != null ? "<srs>" + srs + "</srs>" : "";

		String nativeCrsTag = nativeWKT != null ? "<nativeCRS>" + nativeWKT + "</nativeCRS>" : "";

		String prjPolTag = "<projectionPolicy>FORCE_DECLARED</projectionPolicy>";

		String xml = "<featureType>" + nameTitleParam + srsTag + prjPolTag + enabledTag + nativeCrsTag
				+ "</featureType>";

		int sendRESTint = sendRESTint(METHOD_POST, "/workspaces/" + wsName + "/datastores/" + dsName + "/featuretypes",
				xml);
		return 201 == sendRESTint;
	}

	/**
	 * Uploads an SLD to the Geoserver
	 * 
	 * @param stylename
	 * @param sldString
	 *            SLD-XML as String
	 * @return <code>true</code> successfully uploaded
	 */
	public boolean createSld(String stylename, String sldString) throws IOException {

		return null != createSld_location(stylename, sldString);
	}




	/**
	 * @param stylename
	 * @param sldString
	 * @return REST location URL string to the new style
	 * @throws IOException
	 */
	public String createSld_location(String stylename, String sldString) throws IOException {

		String location = sendRESTlocation(METHOD_POST, "/styles/" + "?name=" + stylename, sldString,
				"application/vnd.ogc.sld+xml", "application/vnd.ogc.sld+xml");
		return location;
	}

	public boolean createWorkspace(String workspaceName) throws IOException {
		return 201 == sendRESTint(METHOD_POST, "/workspaces", "<workspace><name>" + workspaceName
				+ "</name></workspace>");
	}




	/**
	 * To avoid
	 * "org.geoserver.rest.RestletException: java.lang.IllegalArgumentException: Unable to delete resource referenced by layer"
	 * use deleteLayer first.
	 */
	public boolean deleteFeatureType(String wsName, String dsName, String ftName) {
		try {
			return sendRESTint(METHOD_DELETE, "/workspaces/" + wsName + "/datastores/" + dsName + "/featuretypes/"
					+ ftName, null) == 200;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteLayer(String lName) {
		try {

			int result = sendRESTint(METHOD_DELETE, "/layers/" + lName, null);
			return result == 200;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteSld(String styleName, Boolean... purge_) throws IOException {
		Boolean purge = null;
		if (purge_.length > 1)
			throw new IllegalArgumentException("only one purge paramter allowed");
		if (purge_.length == 1) {
			purge = purge_[0];
		}
		if (purge == null)
			purge = false;
		int result = sendRESTint(METHOD_DELETE, "/styles/" + styleName + ".sld?purge=" + purge.toString(), null);
		// + "&name=" + styleName
		return result == 200;
	}

	
	/**
	 * Tell this instance of {@link GsService} to not use authorization
	 */
	public void disableAuthorization() {
		this.password = null;
		this.username = null;
	}

	/**
	 * Tell this {@link GsService} instance to use authorization
	 * 
	 * @param username
	 *            cleartext username
	 * @param password
	 *            cleartext password
	 */
	public void enableAuthorization(String username, String password) {
		this.password = password;
		this.username = username;
	}

	public String getDatastore(String wsName, String dsName) throws MalformedURLException, ProtocolException,
			IOException {
		return sendRESTstring(METHOD_GET, "/workspaces/" + wsName + "/datastores/" + dsName, null);
	}

	public String getFeatureType(String wsName, String dsName, String ftName) throws IOException {
		return sendRESTstring(METHOD_GET,
				"/workspaces/" + wsName + "/datastores/" + dsName + "/featuretypes/" + ftName, null);
	}


	


	/**
	 * @return <code>true</code> if authorization is used for requests
	 */
	public boolean isAuthorization() {
		return password != null && username != null;
	}

	private List<String> parseXmlWithregEx(String xml, Pattern pattern) {
		ArrayList<String> list = new ArrayList<String>();

		Matcher nameMatcher = pattern.matcher(xml);
		while (nameMatcher.find()) {
			String name = nameMatcher.group(1);
			list.add(name.trim());
		}
		return list;
	}

	public boolean purgeSld(String styleName) throws IOException {
		return deleteSld(styleName, true);
	}

	private HttpURLConnection sendREST(String method, String urlAppend, Reader postDataReader, String contentType,
			String accept) throws MalformedURLException, IOException {
		boolean doOut = !METHOD_DELETE.equals(method) && postDataReader != null;
		// boolean doIn = true; // !doOut

		String link = restUrl + urlAppend;
		URL url = new URL(link);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(doOut);
		// uc.setDoInput(false);
		if (contentType != null && !"".equals(contentType)) {
			connection.setRequestProperty("Content-type", contentType);
			connection.setRequestProperty("Content-Type", contentType);
		}
		if (accept != null && !"".equals(accept)) {
			connection.setRequestProperty("Accept", accept);
		}

		connection.setRequestMethod(method.toString());

		if (isAuthorization()) {
			String userPasswordEncoded = new BASE64Encoder().encode((username + ":" + password).getBytes());
			connection.setRequestProperty("Authorization", "Basic " + userPasswordEncoded);
		}

		connection.connect();
		if (connection.getDoOutput()) {
			Writer writer = new OutputStreamWriter(connection.getOutputStream());
			char[] buffer = new char[1024];

			Reader reader = new BufferedReader(postDataReader);
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}

			writer.flush();
			writer.close();
		}
		return connection;
	}

	private HttpURLConnection sendREST(String method, String urlEncoded, String postData, String contentType,
			String accept) throws MalformedURLException, IOException {
		StringReader postDataReader = postData == null ? null : new StringReader(postData);
		return sendREST(method, urlEncoded, postDataReader, contentType, accept);
	}

	public int sendRESTint(String method, String url, String xmlPostContent) throws IOException {
		return sendRESTint(method, url, xmlPostContent, "application/xml", "application/xml");
	}

	/**
	 * @param method
	 *            e.g. 'POST', 'GET', 'PUT' or 'DELETE'
	 * @param urlEncoded
	 *            e.g. '/workspaces' or '/workspaces.xml'
	 * @param contentType
	 *            format of postData, e.g. null or 'text/xml'
	 * @param accept
	 *            format of response, e.g. null or 'text/xml'
	 * @param postData
	 *            e.g. xml data
	 * @throws IOException
	 * @return null, or response of server
	 */
	public int sendRESTint(String method, String urlEncoded, String postData, String contentType, String accept)
			throws IOException {
		HttpURLConnection connection = sendREST(method, urlEncoded, postData, contentType, accept);

		return connection.getResponseCode();
	}

	/**
	 * @param method
	 *            e.g. 'POST', 'GET', 'PUT' or 'DELETE'
	 * @param urlEncoded
	 *            e.g. '/workspaces' or '/workspaces.xml'
	 * @param contentType
	 *            format of postData, e.g. null or 'text/xml'
	 * @param accept
	 *            format of response, e.g. null or 'text/xml'
	 * @param postData
	 *            e.g. xml data
	 * @return null, or location field of the response header
	 */
	public String sendRESTlocation(String method, String urlEncoded, String postData, String contentType, String accept)
			throws IOException {
		HttpURLConnection connection = sendREST(method, urlEncoded, postData, contentType, accept);

		return connection.getHeaderField("Location");
	}

	/**
	 * Sends a REST request and return the answer as a String.
	 * 
	 * @param method
	 *            e.g. 'POST', 'GET', 'PUT' or 'DELETE'
	 * @param urlEncoded
	 *            e.g. '/workspaces' or '/workspaces.xml'
	 * @param contentType
	 *            format of postData, e.g. null or 'text/xml'
	 * @param accept
	 *            format of response, e.g. null or 'text/xml'
	 * @param is
	 *            where to read the data from
	 * @throws IOException
	 * @return null, or response of server
	 */
	public String sendRESTstring(String method, String urlEncoded, Reader is, String contentType, String accept)
			throws IOException {
		HttpURLConnection connection = sendREST(method, urlEncoded, is, contentType, accept);

		// Read response
		InputStream in = connection.getInputStream();
		try {

			int len;
			byte[] buf = new byte[1024];
			StringBuffer sbuf = new StringBuffer();
			while ((len = in.read(buf)) > 0) {
				sbuf.append(new String(buf, 0, len));
			}
			return sbuf.toString();
		} finally {
			in.close();
		}
	}

	public String sendRESTstring(String method, String url, String xmlPostContent) throws IOException {
		return sendRESTstring(method, url, xmlPostContent, "application/xml", "application/xml");
	}

	/**
	 * Sends a REST request and return the answer as a String
	 * 
	 * @param method
	 *            e.g. 'POST', 'GET', 'PUT' or 'DELETE'
	 * @param urlEncoded
	 *            e.g. '/workspaces' or '/workspaces.xml'
	 * @param contentType
	 *            format of postData, e.g. null or 'text/xml'
	 * @param accept
	 *            format of response, e.g. null or 'text/xml'
	 * @param postData
	 *            e.g. xml data
	 * @throws IOException
	 * @return null, or response of server
	 */
	public String sendRESTstring(String method, String urlEncoded, String postData, String contentType, String accept)
			throws IOException {
		HttpURLConnection connection = sendREST(method, urlEncoded, postData, contentType, accept);

		// Read response
		InputStream in = connection.getInputStream();
		try {

			int len;
			byte[] buf = new byte[1024];
			StringBuffer sbuf = new StringBuffer();
			while ((len = in.read(buf)) > 0) {
				sbuf.append(new String(buf, 0, len));
			}
			return sbuf.toString();
		} finally {
			in.close();
		}
	}

	/**
	 * Works: curl -u admin:geoserver -v -XPUT -H 'Content-type: application/zip' --data-binary
	 * 
	 * @/home/stefan/Desktop/arabicData.zip http:/
	 *                                      /localhost:8085/geoserver/rest/workspaces/ws/datastores/test1/file.shp
	 */
	public String uploadShape(String workspace, String dsName, URL zip, String filename) throws IOException {
		System.out.println("I'm in second upload");
		InputStream os = zip.openStream();
		try {
			InputStreamReader postDataReader = new InputStreamReader(os);

			String returnString = sendRESTstring(METHOD_PUT, "/workspaces/" + workspace + "/datastores/" + dsName
					+ "/"+filename+".shp", postDataReader, "application/zip", null);

			// "?configure=all"
			return returnString;
		} finally {
			os.close();
		}
	}
	

	/**
	 * @throws IOException
	 */
	public boolean createCoverage(String wsName, String csName, String cName) throws IOException {

		String xml = "<coverage><name>" + cName + "</name><title>" + cName + "</title></coverage>";

		int sendRESTint = sendRESTint(METHOD_POST,
				"/workspaces/" + wsName + "/coveragestores/" + csName + "/coverages", xml);

		return 201 == sendRESTint;
	}

	public boolean reload() throws IOException {
		return 201 == sendRESTint(METHOD_POST, "/reload", null);
	}

}
