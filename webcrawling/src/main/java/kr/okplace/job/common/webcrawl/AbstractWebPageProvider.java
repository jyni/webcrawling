package kr.okplace.job.common.webcrawl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Superman
 */
public class AbstractWebPageProvider {

	protected static final Log log = LogFactory.getLog(AbstractWebPageProvider.class);

	private AbstractWebPage webPageTemplate;

	/**
	 * @param webPageTemplate
	 */
	public void setWebPageTemplate(AbstractWebPage webPageTemplate) {

		if(this.webPageTemplate!=null) {
			webPageTemplate.setEncoding(this.webPageTemplate.getEncoding());
			webPageTemplate.setTextProvider(this.webPageTemplate.getTextProvider());
		}
		
		this.webPageTemplate = webPageTemplate;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {

		if(webPageTemplate==null) {
			webPageTemplate = new GetMethodWebPage();
		}
		
		((AbstractWebPage)webPageTemplate).setEncoding(encoding);
	}
	
	/**
	 * @param textProvider
	 */
	public void setTextProvider(TextProvider textProvider) {

		if(webPageTemplate==null) {
			webPageTemplate = new GetMethodWebPage();
		}
		
		((AbstractWebPage)webPageTemplate).setTextProvider(textProvider);
	}
	
	/**
	 * @param url
	 * @return
	 */
	protected WebPage getWebPage(String url) {

		if(webPageTemplate==null) {
			webPageTemplate = new GetMethodWebPage();
		}

		WebPage instance = null;
		try {
			instance = webPageTemplate.clone();
			instance.setUrl(url);
			if(log.isInfoEnabled()) {
				log.info("WebPage cloned with URL[" + url + "]!");
			}
		}
		catch (CloneNotSupportedException e) {
			log.error("Can't clone " + webPageTemplate, e);
		}
		
		return instance;
	}
}
