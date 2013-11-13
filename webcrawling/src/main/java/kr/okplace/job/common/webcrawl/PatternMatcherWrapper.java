package kr.okplace.job.common.webcrawl;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.HtmlUtils;

public class PatternMatcherWrapper {
	
	protected final Log log = LogFactory.getLog(this.getClass());

	private static final Map<String, String> REPLACED_STRING = new TreeMap<String, String>() {
		private static final long serialVersionUID = 1L; {
				put("<br ?/?>", System.getProperty("line.separator"));
				put("<\\w+[^>]*>", "");
				put("</\\w+>", "");
	}};
		
	private Pattern pattern;
	private Matcher matcher;

	public void setPattern(String regex) {
		this.pattern = Pattern.compile(regex, Pattern.UNICODE_CASE);
	}

	/**
	 * @param input
	 * @return
	 */
	public Matcher getMatcher() {
		return matcher;
	}

	/**
	 * @param input
	 */
	public void setInput(CharSequence input) {
		this.matcher = pattern.matcher(input);
	}

	/**
	 * @return
	 */
	public boolean hasNext() {
		return matcher.find();
	}

	/**
	 * @param i
	 * @return
	 */
	public String getMatchingGroup(int i) {
		return htmlUnescape(matcher.group(i));
	}

	/**
	 * @param group
	 * @return
	 */
	private String htmlUnescape(String group) {
		
		if(group==null) {
			return null;
		}
		
		String s = HtmlUtils.htmlUnescape(group.trim());
		for(String k: REPLACED_STRING.keySet()) {
			s = s.replaceAll(k, REPLACED_STRING.get(k));
		}
	
		return s;
	}
}
