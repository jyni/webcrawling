package kr.okplace.job.common.webcrawl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author Superman
 */
public class WebPageGatheredWebPageList extends WebPageList implements WebPageProvider, InitializingBean {
	
	private static final Log log = LogFactory.getLog(WebPageGatheredWebPageList.class);
	
	protected WebPageProvider webPageProvider;
	protected BeanExtractor<String> targetExtractor;
	protected boolean includeSource = false;

	/**
	 * @param webPageProvider
	 */
	public void setWebPageProvider(WebPageProvider webPageProvider) {
		this.webPageProvider = webPageProvider;
	}

	/**
	 * @param stringExtractor
	 */
	public void setTargetExtractor(BeanExtractor<String> stringExtractor) {
		this.targetExtractor = stringExtractor;
	}
	
	/**
	 * @param includeSource
	 */
	public void setIncludeSource(boolean includeSource) {
		this.includeSource = includeSource;
	}

	/* (non-Javadoc)
	 * @see kr.okplace.job.common.webcrawl.WebPageList#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(webPageProvider,	"property[webPageProvider] must be set!");
		Assert.notNull(targetExtractor,	"property[targetExtractor] must be set!");
	}

	/* (non-Javadoc)
	 * @see kr.okplace.job.common.webcrawl.WebPageList#hasNext()
	 */
	@Override
	public boolean hasNext() throws Exception {
		
		if(super.getPages()==null) {
			initPageList();
		}
		
		return super.hasNext();
	}
	
	/* (non-Javadoc)
	 * @see kr.okplace.job.common.webcrawl.WebPageList#getNext()
	 */
	@Override
	public WebPage getNext() throws Exception {
		
		if(super.getPages()==null) {
			initPageList();
		}
		
		return super.getNext();
	}
	
	/**
	 * @throws Exception
	 */
	private void initPageList() throws Exception {
		
		WebPage webPage;
		while(webPageProvider.hasNext()) {
			webPage = webPageProvider.getNext();
			addPageList(webPage);
		}

		if(log.isInfoEnabled()) {
			log.info("" + (getPages()==null? 0: getPages().size()) + " Web Page URLs gathered!");
		}
	}

	/**
	 * @param content
	 * @throws Exception 
	 */
	protected void addPageList(WebPage webPage) throws Exception {

		String content = webPage.getText();
		targetExtractor.setInput(content);
		String target;
		while(targetExtractor.hasNext()) {

			target = targetExtractor.getNext();
			if(log.isDebugEnabled()) {
				log.debug("extracted text: " + target);
			}
			
			addPage(target);
		}

		if(includeSource) {
			addPage(webPage.getURL().toString());
		}
	}
}
