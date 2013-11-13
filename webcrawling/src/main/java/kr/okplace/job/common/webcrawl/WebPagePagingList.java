package kr.okplace.job.common.webcrawl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class WebPagePagingList extends WebPageList implements WebPageProvider, InitializingBean {

	static final Log log = LogFactory.getLog(WebPagePagingList.class);

	private String pagePath;
	private String parameters;
	private Integer pagesTotal;
	private Integer rowsTotal;
	private Integer rowsOnAPage = 10;
	private int currentPage = 0;


	/**
	 * @param pagePath
	 */
	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}

	/**
	 * @param parameters
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	/**
	 * @param pagesTotal the pagesTotal to set
	 */
	public void setPagesTotal(Integer pagesTotal) {
		this.pagesTotal = pagesTotal;
	}
	
	/**
	 * @param rowsTotal
	 */
	public void setRowsTotal(Integer rowsTotal) {
		this.rowsTotal = rowsTotal;
	}

	/**
	 * @param rowsOnAPage
	 */
	public void setRowsOnAPage(Integer rowsOnAPage) {
		this.rowsOnAPage = rowsOnAPage;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		Assert.notNull(pagePath, "property[pagePath] must be set!");
		
		if(parameters==null && pagePath.contains("?")) {
			int p = pagePath.indexOf("?");
			parameters = pagePath.substring(p+1);
			pagePath = pagePath.substring(0, p);
		}
		
		if(pagesTotal==null && rowsTotal!=null) {
			pagesTotal = rowsTotal/rowsOnAPage;
		}
		
		Assert.notNull(pagesTotal, "property[pagesTotal] must be set!");
	}

	/* (non-Javadoc)
	 * @see kr.okplace.job.nxmile.WebPageList#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return currentPage < pagesTotal;
	}

	/* (non-Javadoc)
	 * @see kr.okplace.job.nxmile.WebPageList#getNextPagePath()
	 */
	@Override
	public String getNextPagePath() {
		++currentPage;
		return (parameters==null? pagePath: pagePath + '?' + parameters) + currentPage;
	}
}