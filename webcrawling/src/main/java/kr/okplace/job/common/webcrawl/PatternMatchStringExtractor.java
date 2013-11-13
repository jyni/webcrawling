package kr.okplace.job.common.webcrawl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Superman
 */
public class PatternMatchStringExtractor extends PatternMatcherWrapper implements BeanExtractor<String> {
	
	private String template;
	private Map<String, Integer> matchingGroups;

	/**
	 * @param template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/* (non-Javadoc)
	 * @see kr.okplace.job.common.webcrawl.BeanExtractor#getNext()
	 */
	public String getNext() {
		
		if(template==null) {
			return getMatchingGroup(0);
		}
		
		if(matchingGroups==null) {
			initMatchingGroups();
		}

		String content = this.template;
		for(String key: matchingGroups.keySet()) {
			content = content.replace(key, getMatchingGroup(matchingGroups.get(key)));
		}
		
		return content;
	}

	/**
	 * @param content
	 * @return
	 */
	public List<String> getStringList(String content) {
		
		setInput(content);

		List<String> pagePaths = new ArrayList<String>();
		while(hasNext()) {
			pagePaths.add(getNext());
		}
		
		return pagePaths;
	}
	
	/**
	 * 
	 */
	private void initMatchingGroups() {
		
		matchingGroups = new HashMap<String, Integer>();
		Pattern pattern = Pattern.compile("\\$(\\d)");
		Matcher matcher = pattern.matcher(template);
		while(matcher.find()) {
			matchingGroups.put(matcher.group(0), Integer.valueOf(matcher.group(1)));
		}
	}
}
