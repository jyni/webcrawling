package kr.okplace.job.common.webcrawl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Superman
 */
public class PostMethodWebPage extends AbstractWebPage implements WebPage {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(PostMethodWebPage.class);

	private String parameters;
	
	public PostMethodWebPage() {}

	public PostMethodWebPage(String url) {
		setUrl(url);
	}

	/* (non-Javadoc)
	 * @see kr.okplace.job.common.webcrawl.AbstractWebPage#setUrl(java.lang.String)
	 */
	@Override
	public void setUrl(String url) {
		
		if(url!=null && url.contains("?")) {
			String[] u = url.split("\\?");
			parameters = u[1];
			super.setUrl(u[0]);
		}
	}
	
	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	/**
	 * @param pagePath
	 * @param parameters
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public URLConnection getURLConnection() throws MalformedURLException, IOException {
	
		URLConnection connection = getURL().openConnection();

		if(!getHeaders().containsKey("Content-Type")) {
			putHeader("Content-Type", "application/x-www-form-urlencoded");
		}
		
		for(String key: getHeaders().keySet()) {
			connection.setRequestProperty(key, getHeaders().get(key));
		}
		
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setReadTimeout(1000 * 30);
	
		OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

		if(parameters!=null) {
			wr.write(parameters);
		}
		wr.flush();
		wr.close();
	
		return connection;
	}
}
