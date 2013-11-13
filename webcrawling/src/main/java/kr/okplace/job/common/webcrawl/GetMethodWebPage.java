package kr.okplace.job.common.webcrawl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Superman
 */
public class GetMethodWebPage extends AbstractWebPage implements WebPage {

	private static final Log log = LogFactory.getLog(GetMethodWebPage.class);

	public GetMethodWebPage() {}

	public GetMethodWebPage(String url) {
		setUrl(url);
	}

	/**
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public URLConnection getURLConnection() throws MalformedURLException, IOException {
	
		URLConnection connection = getURL().openConnection();
		if (log.isDebugEnabled()) {
			log.debug("connection: " + connection.getURL());
		}

		if(!getHeaders().containsKey("Cache-Control")) {
			putHeader("Cache-Control", "no-cache");
		}
		
		for(String key: getHeaders().keySet()) {
			connection.setRequestProperty(key, getHeaders().get(key));
		}
	
		return connection;
	}
}
