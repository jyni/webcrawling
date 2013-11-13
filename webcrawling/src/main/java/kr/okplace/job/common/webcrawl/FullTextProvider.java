package kr.okplace.job.common.webcrawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Superman
 */
public class FullTextProvider implements TextProvider {

	private static final Log log = LogFactory.getLog(FullTextProvider.class);
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	/* (non-Javadoc)
	 * @see kr.okplace.job.nxmile.ContentExtractor#getContent(java.io.InputStream, java.lang.String)
	 */
	@Override
	public String getText(InputStream inputStream, String encoding) throws IOException {

		StringBuffer content = new StringBuffer();
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(inputStream, encoding));
			
			String line;
			while ((line = rd.readLine()) != null) {
				content.append(line).append(LINE_SEPARATOR);
			}

			if (log.isDebugEnabled()) {
				log.debug("content:\n" + content);
			}
		}
		catch (UnsupportedEncodingException e) {
			throw e;
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if(rd!=null) {
				try { rd.close(); } catch (Exception e) { }
			}
		}
		
		return content.toString();
	}

}
