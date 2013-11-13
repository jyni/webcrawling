package kr.okplace.job.common.webcrawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class WebPageCodeParameterList extends WebPageList implements WebPageProvider, InitializingBean {

	private static final String SPLITTER = "[,\\s]+";
	
	private String pagePath;
	private String parameters;
	private List<String> codes;
	private int total;
	private int index = 0;
	
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
	 * @param codes
	 */
	public void setCodes(String codes) {

		Assert.isTrue(codes!=null && !codes.trim().isEmpty(), "property[codes] should be provided!");
		
		this.codes = Arrays.asList(codes.trim().split(SPLITTER));
		this.total = this.codes.size();
	}
	
	/**
	 * @param resource
	 * @throws IOException
	 */
	public void setCodeListFile(Resource resource) throws IOException {
		
		log.info("Resource[" + resource + "] loaded!");

		codes = new ArrayList<String>();
		
		BufferedReader br = null;
		try {
			InputStream is = resource.getInputStream();
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				for(String code: line.split(SPLITTER)) {
					codes.add(code);
				}
			}
		}
		catch (IOException e) {
			log.error("resource[" + resource + "] input error!", e);
			throw e;
		}
		finally {
			this.total = this.codes.size();
			try { if(br!=null) br.close(); } catch (IOException e) {}
		}
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notEmpty(codes, "property[codes] must be set!");
	}
	
	public boolean hasNext() {
		return index < total;
	}
	
	public String getNextPagePath() {
		return (parameters==null? pagePath: pagePath + '?' + parameters) + codes.get(index++);
	}
}
