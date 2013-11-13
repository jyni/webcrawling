package kr.okplace.job.common.webcrawl;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractWebPage implements Cloneable {

	private static final Log log = LogFactory.getLog(AbstractWebPage.class);

	private URL url;
	private String encoding = "UTF-8";
	private Map<String, String> headers = new LinkedHashMap<String, String>();
	
	private TextProvider textProvider;

	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public WebPage clone() throws CloneNotSupportedException {
		return (WebPage) super.clone();
	}
	
	public URL getURL() {
		return url;
	}

	public void setUrl(String url) {
		
		try {
			this.url = new URL(url);
		}
		catch (MalformedURLException e) {
			log.warn("URL[" + url + "] is malformed!");
		}
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void putHeader(String key, String value) {
		headers.put(key, value);
	}

	public void setContentType(String contentType) {
		headers.put("Content-Type", contentType);
	}

	public void setCookie(String cookie) {
		headers.put("Cookie", cookie);
	}

	public void setReferer(String referer) {
		headers.put("Referer", referer);
	}
	
	public void setHeaders(Map<String, String> headers) {
		headers.putAll(headers);
	}

	/**
	 * Default Text Provider: FullTextProvider
	 * 
	 * @return
	 */
	public TextProvider getTextProvider() {
		
		if(textProvider==null) {
			textProvider = new FullTextProvider();
		}
		
		return textProvider;
	}
	
	/**
	 * @param textProvider
	 */
	public void setTextProvider(TextProvider textProvider) {
		this.textProvider = textProvider;
	}

	/**
	 * @return
	 */
	public String getText() {
		
		try {
			InputStream connection = getInputStream();
			return getTextProvider().getText(connection, this.encoding);
		}
		catch (Exception e) {
			log.warn("");
		}
		
		return null;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public InputStream getInputStream() throws Exception {
	
		URLConnection connection = getURLConnection();

/*	
		String encoding = connection.getContentEncoding();
		if (log.isDebugEnabled()) {
			log.debug("encoding: " + encoding);
		}
	
		if (encoding == null) {
			encoding = this.encoding;
		}
*/	
		return connection.getInputStream();
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public abstract URLConnection getURLConnection() throws Exception;
}
