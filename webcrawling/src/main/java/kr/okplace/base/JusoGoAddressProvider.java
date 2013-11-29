package kr.okplace.base;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.HtmlUtils;

import kr.okplace.base.KoreanAddress.Type;
import kr.okplace.job.common.webcrawl.SingleWebPageProvider;

/**
 * http://www.juso.go.kr/support/AddressMainSearch.do?searchType=location_newaddr&searchKeyword=%ED%8C%94%ED%83%84%EB%A9%B4+%EA%B5%AC%EC%9E%A5%EB%A6%AC+113-9
 * 
 * @author Superman
 */
public class JusoGoAddressProvider implements AddressProvider, Serializable {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(JusoGoAddressProvider.class);
	
	private static final String PATH = "http://www.juso.go.kr/support/AddressMainSearch.do?searchType=location_newaddr&searchKeyword=";
	private static final Pattern PATTERN = Pattern.compile("<td>1\\.</td>[\\s\\S]+?<a[^>]+>(.+?)</a>[\\s\\S]+?<span>\\s+([\\S ]+)\\s+</span>[\\s\\S]+?<td align=\"center\">\\s*(\\S+)\\s*</td>", Pattern.UNICODE_CASE);
	private static final Map<String, String> REPLACED_STRING = new TreeMap<String, String>() {
		private static final long serialVersionUID = 1L; {
				put("<\\w+[^>]*>", "");
				put("</\\w+\\s*>", "");
	}};
	
	private SingleWebPageProvider webPageProvider;
	
	public JusoGoAddressProvider() {
		this.webPageProvider = new SingleWebPageProvider();
	}
	
	@Override
	public KoreanAddress getAddress(Type type, String address) {

		webPageProvider.setPagePath(PATH + address);
		if(!webPageProvider.hasNext()) {
			return null;
		}
		
		String text;
		
		try {
			text = webPageProvider.getNext().getText();
		}
		catch (MalformedURLException e) {
			log.warn("Malformed URL [" + PATH + address + "]!", e);
			return null;
		}
		catch (IOException e) {
			log.warn("can't open [" + PATH + address + "]!", e);
			return null;
		}

		Matcher m = PATTERN.matcher(text);
		if(!m.find()) {
			return null;
		}

		/**
		 * group(1) : 도로명 주소
		 * group(2) : 지번 주소
		 * 
		 * Type.RoadName   -> type.ordinal 0
		 * Type.LandNumber -> type.ordinal 1 
		 */
		String content = HtmlUtils.htmlUnescape(m.group(type.ordinal() + 1));
		for(String k: REPLACED_STRING.keySet()) {
			content = content.replaceAll(k, REPLACED_STRING.get(k));
		}
		
		return KoreanAddress.getInstance(content, this);
	}
}
