package kr.okplace.job.common.webcrawl;

public interface BeanExtractor<T> {

	/**
	 * @param input
	 */
	public void setInput(CharSequence input);

	/**
	 * @return
	 * @throws Exception
	 */
	public boolean hasNext() throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	public abstract T getNext() throws Exception;
}