package kr.okplace.job.common.webcrawl;

import java.io.InputStream;
import java.net.URL;

public interface WebPage {

	public abstract void setUrl(String url);

	public abstract URL getURL();

	public abstract String getText();

	public abstract InputStream getInputStream() throws Exception;
}