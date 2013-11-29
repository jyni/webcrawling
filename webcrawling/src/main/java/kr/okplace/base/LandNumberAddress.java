package kr.okplace.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 대한민국의 주소 - 위키백과, 우리 모두의 백과사전.> 2 지번 주소
 * http://ko.wikipedia.org/wiki/%EB%8C%80%ED%95%9C%EB%AF%BC%EA%B5%AD%EC%9D%98_%EC%A3%BC%EC%86%8C#.EC.A7.80.EB.B2.88_.EC.A3.BC.EC.86.8C

 * 광역자치단체	기초자치단체		행정시·일반구	읍·면		동 · 리	번지		상세주소
 * 서울특별시	강남구								삼성동		338-3	○○빌딩 A호
 * 경기도		수원시			장안구					정자동		687		○○아파트 A동 B호
 * 경상남도	창원시			의창구			대산면		가술리		323-4
 * 
 * @author Superman
 */
public class LandNumberAddress extends KoreanAddress implements Serializable {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(LandNumberAddress.class);
	
	private static final List<String> PROVINCE_LIST = Arrays.asList("서울","부산","대구","인천","광주","대전","울산 ","세종","경기","강원","충북","충남","전북","전남","경북","경남","제주","충청북","충청남","전라북","전라남","경상북","경상남");
	private static final int PROVINCE_SIZE = PROVINCE_LIST.size() - 6;
	
	private static final Pattern SIMPLE_PATTERN = Pattern.compile("(?:((\\S+?)(?:시|군|구))\\s+)?(?:((\\S+?)구)\\s+)?((\\S+?)(?:읍|면|동))", Pattern.UNICODE_CASE);
	
	/*
	 * 광역자치단체	기초자치단체		행정시·일반구	읍·면		동		통·리		반		번지/호	상세주소
	 * 세종시										한솔동						123-4
	 * 세종시								조치원읍			교리				12-3
	 * 세종시								금남면				감성리				1
	 * 서울		종로								종로1가					1
	 * 서울시		종로구								종로1가동					1
	 * 서울특별시	강남구								삼성동						338-3	○○빌딩 A호
	 * 경기도		수원시			장안구					정자동						687		○○아파트 A동 B호
	 * 경상남도	창원시			의창구			대산면				가술리				323-4
	 */
	private enum Domain {
		PROVINCE	(Pattern.compile("(\\S+?)(?:특별|광역)?(?:자치)?(?:시|도)?", Pattern.UNICODE_CASE)),
		CITY		(Pattern.compile("(\\S+?)(?:시|군|구)?", Pattern.UNICODE_CASE)),
		DISTRICT	(Pattern.compile("(\\S+?)(?:구)?", Pattern.UNICODE_CASE)),
		TOWN		(Pattern.compile("(\\S+?)(?:읍|면|동)?", Pattern.UNICODE_CASE)),
		VILLAGE		(Pattern.compile("(?:(\\S+?)리|(\\d+)통)", Pattern.UNICODE_CASE)),
		BLOCK		(Pattern.compile("(\\d+)반", Pattern.UNICODE_CASE)),
		NUMBER		(Pattern.compile("(산)?\\s*(\\d+)(?:-(\\d+))?(?:번지)?(?:\\s*(\\d+)호)?", Pattern.UNICODE_CASE)),
		DETAIL		(Pattern.compile(".*", Pattern.UNICODE_CASE));
		private Pattern pattern;
		private Domain(Pattern pattern) {
			this.pattern = pattern;
		}
		public Matcher matcher(String input) {
			return this.pattern.matcher(input);
		}
		public boolean valid(Matcher m) {
			
			switch(this) {
			case PROVINCE:
				return PROVINCE_LIST.contains(m.group(1));
			default:
				return true;
			}
		}
		public String getCore(Matcher m) {

			String group1 = m.group(1);
			
			switch(this) {

			// 경상남 -> 경남
			case PROVINCE:
				int i = PROVINCE_LIST.indexOf(group1);
				return (i < PROVINCE_SIZE)? group1: PROVINCE_LIST.get(i-6);
			
			// 리와 통중 하나
			case VILLAGE:
				return (group1!=null && !group1.isEmpty())? group1: m.group(2);

			// 번지 format : (산)?(\d+)(?:-(\d+))?
			case NUMBER:
				
				String core = (group1==null || group1.isEmpty())? m.group(2): group1.concat(m.group(2));
				
				String group3 = m.group(3);
				if(group3!=null && !group3.isEmpty()) {
					core = core.concat("-").concat(group3);
				}
				else {
					String group4 = m.group(4);
					if(group4!=null && !group4.isEmpty()) {
						core = core.concat("-").concat(group4);
					}
				}

				return core;
				
			case DETAIL:
				return m.group();
				
			default:
				return group1;
			}
		}
	}
	
	protected List<Domain[]> domainsList = new ArrayList<Domain[]>(){
		private static final long serialVersionUID = 1L; {
			add(new Domain[]{Domain.CITY, Domain.DISTRICT, Domain.TOWN, Domain.VILLAGE, Domain.BLOCK, Domain.NUMBER});
			add(new Domain[]{Domain.CITY, Domain.DISTRICT, Domain.TOWN, Domain.VILLAGE, Domain.NUMBER});
			add(new Domain[]{Domain.CITY, Domain.TOWN, Domain.VILLAGE, Domain.NUMBER});
			add(new Domain[]{Domain.CITY, Domain.TOWN, Domain.NUMBER});
	}};

	protected Map<Domain, String> parts;
	protected Map<Domain, String> cores;

	/* (non-Javadoc)
	 * @see kr.okplace.base.KoreanAddress#getOtherType()
	 */
	@Override
	public KoreanAddress getOtherType() {
		// TODO Auto-generated method stub
		return this;
	}
	
	/**
	 * @param level
	 * @return
	 */
	public String getNormalized(int level) {

		if(cores==null) {
			parse();
		}
		
		Domain[] domains = level==0? Domain.values(): domainsList.get(level-1);
		StringBuffer sb = new StringBuffer();
		String core;
		for(Domain p: domains) {
			core = cores.get(p);
			if(core!=null && !core.isEmpty()) {
				sb.append(' ').append(core);
			}
		}
			
		return sb.length()==0? "": sb.substring(1);
	}
	
	public String getStandardized() {

		if(parts==null) {
			parse();
		}
		
		StringBuffer sb = new StringBuffer();
		String part;
		for(Domain p: Domain.values()) {
			part = parts.get(p);
			if(part!=null && !part.isEmpty()) {
				sb.append(' ').append(part);
			}
		}
		
		return sb.length()==0? "": sb.substring(1);
	}

	/**
	 * 
	 */
	public void parse() {

		String contents = getContents();
		if(contents==null) {
			parts = null;
			cores = null;
			return;
		}
		
		parts = new HashMap<Domain, String>();
		cores = new HashMap<Domain, String>();
		
		// 시도(광역자치단체) parsing
		String province = contents.split("\\s+", 2)[0];
		
		contents = parse(province, new Domain[]{Domain.PROVINCE});

		// 시군 구 읍면동 구분자가 있는 경우 check
		Matcher m = SIMPLE_PATTERN.matcher(contents);
		String detail = contents;
		// 시군 구 읍면동 으로 구분되어 있는 경우
		if(m.find()) {
			detail = parse(contents, m);
		}
		// 시군 구 읍면동 으로 구분되어 있지않은 경우
/*		else for(Part[] pa: partsSequence) {
			
			detail = parse(content, pa);
			if(cores.containsKey(Part.NUMBER)) {
				break;
			}
		}
*/
		// 나머지 정보를 저장.
		parts.put(Domain.DETAIL, detail);
	}
	
	/**
	 * <p>simple format(SIMPLE_PATTERN) parsing</p>
	 * 
	 * @param content
	 * @param m
	 * @return
	 */
	private String parse(String content, Matcher m) {
		
		String group1 = m.group(1);
		// 시군이 없능 경우(구가 처음 있거나, 구가 없는 경우)
		if(group1==null || group1.isEmpty()) {
			parts.put(Domain.CITY, m.group(3));
			cores.put(Domain.CITY, m.group(4));
		}
		// 시군이 있능 경우
		else {
			parts.put(Domain.CITY, m.group(1));
			cores.put(Domain.CITY, m.group(2));
			parts.put(Domain.DISTRICT, m.group(3));
			cores.put(Domain.DISTRICT, m.group(4));
		}

		// 읍명동
		parts.put(Domain.TOWN, m.group(5));
		cores.put(Domain.TOWN, m.group(6));

		// 리통 반 번지/호 parsing
		String detail = parse(content.substring(m.end()).trim(), new Domain[]{Domain.VILLAGE, Domain.BLOCK, Domain.NUMBER});
		return detail;
	}

	/**
	 * @param content
	 * @param partList
	 * @return
	 */
	private String parse(String content, Domain[] partList) {
		
		Matcher m;
		String s = content;
		for(Domain p: partList) {
			m = p.matcher(s);
			if(m.find() && p.valid(m)) {
				parts.put(p, m.group());
				cores.put(p, p.getCore(m));
				s = s.substring(m.end()).trim();
			}
		}
		
		return s;
	}
}
