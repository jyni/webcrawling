package kr.okplace.job.common.webcrawl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author Superman
 */
public class WebPageChainedWebPageList extends WebPageGatheredWebPageList implements WebPageProvider, InitializingBean {
	
	private static final Log log = LogFactory.getLog(WebPageChainedWebPageList.class);

	private BeanExtractor<String> sourceExtractor;
	
	private List<String> visitedPages = new ArrayList<String>();

	public void setSourceExtractor(BeanExtractor<String> stringExtractor) {
		this.sourceExtractor = stringExtractor;
	}
	
	/* (non-Javadoc)
	 * @see kr.okplace.job.common.webcrawl.WebPageGatheredWebPageList#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		Assert.notNull(sourceExtractor,	"property[sourceExtractor] must be set!");
	}

	/**
	 * @param content
	 * @throws Exception 
	 */
	/* (non-Javadoc)
	 * @see kr.okplace.job.common.webcrawl.WebPageGatheredWebPageList#addPageList(kr.okplace.job.common.webcrawl.WebPage)
	 */
	@Override
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

		sourceExtractor.setInput(content);
		String source;
		while(sourceExtractor.hasNext()) {
			
			source = sourceExtractor.getNext();
			if(!visitedPages.contains(source)) {
				visitedPages.add(source);
				addPageList(getWebPage(source));
			}
		}
	}
}
