package kr.okplace.job.common.webcrawl;


public interface WebPageProvider {

	/**
	 * @return
	 * @throws Exception
	 */
	public boolean hasNext() throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	public WebPage getNext() throws Exception;
}
