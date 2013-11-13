package kr.okplace.job.common.webcrawl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class WebPageItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> implements ItemReader<T>, InitializingBean {
	
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(WebPageItemReader.class);
	
	private WebPageProvider webPageProvider;
	private BeanExtractor<T> beanExtractor;

	private boolean matchNext = true;

	public WebPageItemReader() {
		setName(ClassUtils.getShortName(WebPageItemReader.class));
	}

	public void setWebPageProvider(WebPageProvider webPageProvider) {
		this.webPageProvider = webPageProvider;
	}

	public void setBeanExtractor(BeanExtractor<T> beanExtractor) {
		this.beanExtractor = beanExtractor;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(webPageProvider,		"property[webPageProvider] must be set!");
		Assert.notNull(beanExtractor,		"property[patternBeans] must be set!");
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader#doRead()
	 */
	@Override
	protected T doRead() throws Exception {
		
		if(matchNext) {

			if(!webPageProvider.hasNext()) {
				return null;
			}
			
			beanExtractor.setInput(webPageProvider.getNext().getText());
			matchNext = false;
		}

		if(!beanExtractor.hasNext()) {
			matchNext = true;
			return doRead();
		}

		return beanExtractor.getNext();
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader#doOpen()
	 */
	@Override
	protected void doOpen() throws Exception {
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader#doClose()
	 */
	@Override
	protected void doClose() throws Exception {
	}
}
