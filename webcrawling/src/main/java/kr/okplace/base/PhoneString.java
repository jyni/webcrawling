package kr.okplace.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>전화번호 String parsing방법</p>
 * <p>comma(,), slash(/)는 분리하여, 국번을 동일하게 부여하고 목록처리한다.</p>
 * <p>wiggle(~)은 국번을 동일하게 부여하고 범위처리한다.</p>
 * <p>hiphen(-), period(.)는 전화번호 구분자로 보고 무시한다.</p>
 * 
 * @author Superman
 */
public class PhoneString implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Pattern NUMBER_RANGE = Pattern.compile("(\\d+)~(\\d+)");
	@SuppressWarnings("unused")
	private static final Pattern NUMBER_LIST = Pattern.compile("[,\\/]");
	
	private List<String> phoneList;
	
	public PhoneString(String string) {
		parse(string);
	}

	public List<String> getPhoneList() {
		return phoneList;
	}
	public boolean contains(String phone) {
		
		if(phoneList==null) {
			return false;
		}
		
		for(String p: phoneList) {
			if(phone.equals(getNormalizedTelNo(p))) {
				return true;
			}
		}
		
		return false;
	}
	public void addPhone(String phone) {
		
		if(phoneList==null) {
			phoneList = new ArrayList<String>();
		}
		
		phoneList.add(phone);
	}
	public void setPhoneList(List<String> phoneList) {
		this.phoneList = phoneList;
	}

	public String getMainPhone() {
		return phoneList==null || phoneList.isEmpty()? null: phoneList.get(0);
	}

	public void setMainPhone(String mainPhone) {
		
		if(phoneList==null) {
			phoneList = new ArrayList<String>();
		}

		if(phoneList.contains(mainPhone)) {
			phoneList.remove(mainPhone);
		}
		
		phoneList.add(0, mainPhone);
		
	}
	
	public boolean isUnique() {
		return phoneList!=null && phoneList.size()==1;
	}

	/**
	 * @param telNo
	 * @return
	 */
	public static String getNormalizedTelNo(String telNo) {
		
		String[] sa = telNo.split("[^\\d]+");
		StringBuffer sb = new StringBuffer();
		for(String s: sa) {
			sb.append(s);
		}
		
		return sb.toString();
	}
	
	/**
	 * @return
	 */
	public String getNormalizedString() {
		
		if(phoneList==null || phoneList.isEmpty()) {
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		String before = null;
		boolean abbr;
		for(String phone: phoneList) {
			
			if(before==null) {
				before = phone;
				sb.append(',').append(phone);
				continue;
			}
			
			abbr = false;
			if(before.contains("-")) {
				for(int i=before.lastIndexOf('-'); i>0; i=before.lastIndexOf('-', i-1)) {
					if(phone.startsWith(before.substring(0, i))) {
						sb.append(',').append(phone.substring(i + 1));
						abbr = true;
						break;
					}	
				}
			}
			else {
				for(int i=before.length()-1; i>0; i--) {
					if(phone.startsWith(before.substring(0, i))) {
						sb.append(',').append(phone.substring(i));
						abbr = true;
						break;
					}
				}
			}
			
			if(!abbr) {
				before = phone;
				sb.append(',').append(phone);
			}
		}

		return sb.substring(1).toString();
	}
	
	/**
	 * <p>전화번호(String) parsing방법</p>
	 * <p>comma(,), slash(/)는 분리하여, 국번을 동일하게 부여하고 목록처리한다.</p>
	 * <p>wiggle(~)은 국번을 동일하게 부여하고 범위처리한다.</p>
	 * <p>hyphen(-), period(.)는 전화번호 구분자로 보고 무시한다.</p>
	 * 
	 * @param string
	 */
	private void parse(String string) {
		
		if(string==null || string.isEmpty()) {
			return;
		}

		String[] phones = extendRange(string.replaceAll("[\\s\\(]+", "").replaceAll("\\)", "-")).split("[,\\/]");
		if(phones.length==1) {
			addPhone(string);
			return;
		}
		
		addPhone(phones[0]);
		
		String[] first = phones[0].split("[\\-\\.]");
		String[] phone;
		// 전화번호 구분자로 구분되어 있지 않은 경우
		if(first.length==0) for(int i=1; i<phones.length; i++) {
			
			// 8보다 같거나 작고, 1로 시작하지않으면 확장한다.
			if(phones[i].length()<=8 && !phones[i].startsWith("1")) {
				phones[i] = fill(phones[i], phones[0]);
			}
			
			addPhone(phones[i]);
		}
		// 전화번호 구분자로 구분되어 있는 경우
		else for(int i=1; i<phones.length; i++) {
	
			// 4보다 같거나 작으면 확장한다.
			if(phones[i].length()<=4) {
				phones[i] = fill(phones[i], phones[0]);
			}

			phone = phones[i].split("[\\-\\.]");
			// 구분 갯수가 작고, 1로 시작하지않으면 확장한다.
			if(phone.length<first.length && !phones[i].startsWith("1")) {
				phones[i] = fill(phone, first);
			}
			
			addPhone(phones[i]);
		}
	}
	
	/**
	 * <p>wiggle(~)로 구분된 번호는 범위내 숫자를 comma(,)로 구분하고 대체한다.</p>
	 * 
	 * @param string
	 * @return
	 */
	private String extendRange(String string) {

		Matcher m = NUMBER_RANGE.matcher(string);
		StringBuffer replaced = new StringBuffer();
		StringBuffer sb;
		String group1, group2;
		int length1, length2;
		while(m.find()) {
			
			sb = new StringBuffer();
			group1 = m.group(1);
			group2 = m.group(2);
			length1 = group1.length();
			length2 = group2.length();
			// 뒷번호의 길이가 앞번호의 길이보다 작으면, 채워넣는다.
			if(length1>length2) {
				group2 = group1.substring(0, length1-length2) + group2;
			}

			for(int i=new Integer(group1); i<=new Integer(group2); i++) {
				sb.append(',').append(i);
			}

			// 뒷번호가 앞번호보다 큰 경우만 출력한다.
			if(sb.length()>0) {
				m.appendReplacement(replaced, sb.substring(1));
			}
		}
		m.appendTail(replaced);
		 
		return replaced.toString();
	}

	/**
	 * <p>전화번호 숫자 갯수가 첫번호와 다를 경우, 첫번호를 기준으로 채운다.</p>
	 * 
	 * @param string
	 * @param first
	 * @return
	 */
	private String fill(String string, String first) {
		return first.substring(0, first.length() - string.length()) + string;
	}
	
	/**
	 * <p>전화번호 구분자로 구분된 갯수가 첫번호와 다를 경우, 첫번호를 기준으로 채운다.</p>
	 * 
	 * @param phone
	 * @param first
	 * @return
	 */
	private String fill(String[] phone, String[] first) {
	
		int d = first.length-phone.length;
		
		StringBuffer p = new StringBuffer();
		for(int i=0; i<d; i++) {
			p.append('-').append(first[i]);
		}
		for(int i=d; i<first.length; i++) {
			p.append('-').append(phone[i-d]);
		}
		
		return p.substring(1).toString();
	}
}
