package kr.okplace.job.common.webcrawl;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class SingleWebPageProvider extends AbstractWebPageProvider implements WebPageProvider, InitializingBean {

	private String pagePath;
	private boolean provided = false;
	
	/**
	 * @param pagePath
	 */
	public void setPagePath(String page) {
		this.pagePath = page;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasLength(pagePath,	"property[pagePath] must be set!");
	}

	/* (non-Javadoc)
	 * @see kr.okplace.job.common.webcrawl.WebPageProvider#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return !provided;
	}

	/* (non-Javadoc)
	 * @see kr.okplace.job.common.webcrawl.WebPageProvider#getNext()
	 */
	@Override
	public WebPage getNext() throws MalformedURLException, IOException {
		provided = true;
		return getWebPage(pagePath);
	}

}
