package kr.okplace.job.common.webcrawl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class WebPageList extends AbstractWebPageProvider implements WebPageProvider, InitializingBean {

	protected static final Log log = LogFactory.getLog(WebPageList.class);

	private List<String> pages;
	private int currentPage = 0;

	/**
	 * @return
	 */
	public List<String> getPages() {
		return this.pages;
	}
	
	/**
	 * @param page
	 */
	public void setPage(String page) {
		addPage(page);
	}
	
	/**
	 * @param page
	 */
	public void addPage(String page) {
		
		if(this.pages==null) {
			this.pages = new ArrayList<String>();
		}
		
		if(!this.pages.contains(page)) {
			this.pages.add(page);
		}
	}
	
	/**
	 * @param pages
	 */
	public void setPages(List<String> pages) {
		this.pages = pages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(pages,	"property[pages or page] must be set!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.okplace.job.nxmile.WebPageListProvider#hasNext()
	 */
	@Override
	public boolean hasNext() throws Exception {
		return pages != null && currentPage<pages.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.okplace.job.nxmile.WebPageListProvider#getNext()
	 */
	@Override
	public WebPage getNext() throws Exception {
		return getWebPage(getNextPagePath());
	}

	/**
	 * @return
	 */
	protected String getNextPagePath() {
		
		if(log.isDebugEnabled()) {
			log.debug("current Page: " + currentPage);
			log.debug("current URL: " + pages.get(currentPage));
		}
		
		return pages.get(currentPage++);
	}
}
