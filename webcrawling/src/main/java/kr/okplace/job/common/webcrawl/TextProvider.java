package kr.okplace.job.common.webcrawl;

import java.io.IOException;
import java.io.InputStream;

public interface TextProvider {
	public String getText(InputStream inputStream, String encoding) throws IOException;
}
