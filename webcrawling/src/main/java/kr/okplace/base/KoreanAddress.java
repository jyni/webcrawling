package kr.okplace.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * PROVINCE CITY
 * 광역자치단체	기초자치단체		행정시·일반구	읍·면		동		통·리		반		번지/호	상세주소
 * 세종										한솔동						123-4
 * 세종시								조치원읍			교리				12-3
 * 세종시								금남면				감성리				1
 * 서울		종로								종로1가					1
 * 서울시		종로구								종로1가동					1
 * 서울특별시	강남구								삼성동						338-3	○○빌딩 A호
 * 경기도		수원시			장안구					정자동						687		○○아파트 A동 B호
 * 경상남도	창원시			의창구			대산면				가술리				323-4
 * 
 * @author Superman
 *
 */
public abstract class KoreanAddress implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(KoreanAddress.class);
	
	private static final List<String> PROVINCE_LIST = Arrays.asList("서울","부산","대구","인천","광주","대전","울산 ","세종","경기","강원","충북","충남","전북","전남","경북","경남","제주","충청북","충청남","전라북","전라남","경상북","경상남");
	private static final int PROVINCE_SIZE = PROVINCE_LIST.size() - 6;
	private static final Pattern SIMPLE_PATTERN = Pattern.compile("(\\S+?)(?:특별|광역)?(?:자치)?(?:시|도)?\\s+(\\S+?)(?:시|군|구)?", Pattern.UNICODE_CASE);

	public static enum Type {
		
		RoadName	(RoadNameAddress.class,		Pattern.compile("\\S+?(?:대)?로\\s*\\d+", Pattern.UNICODE_CASE)),
		LandNumber	(LandNumberAddress.class,	Pattern.compile("\\S+?(?:동|리)?(?:\\s+산)?\\s*\\d+", Pattern.UNICODE_CASE));
		
		private Class<? extends KoreanAddress> clazz;
		private Pattern pattern;
		
		private Type(Class<? extends KoreanAddress> clazz, Pattern pattern) {
			this.clazz = clazz;
			this.pattern = pattern;
		}
		
		public Type getOtherType() {
			
			for(Type t: values()) {
				if(t!=this) {
					return t;
				}
			}
			
			return null;
		}
		
		public KoreanAddress getInstance(String input) throws InstantiationException, IllegalAccessException {
			
			Matcher m = pattern.matcher(input);
			if(m.find()) {
				KoreanAddress instance = clazz.newInstance();
				instance.setType(this);
				instance.setDomains(input);
				return instance;
			}
			
			return null;
		}
	}

	private Type type;
	private AddressProvider addressProvider;

	private AddressDomain domains;
	
	/**
	 * @param contents
	 * @return
	 */
	public static KoreanAddress getInstance(String contents) {
		return getInstance(contents, null);
	}
	
	/**
	 * @param contents
	 * @param addressProvider
	 * @return
	 */
	public static KoreanAddress getInstance(String contents, AddressProvider addressProvider) {

		if(contents==null) {
			throw new IllegalArgumentException(contents);
		}
		
		String address = contents;
		Matcher m = SIMPLE_PATTERN.matcher(contents);
		boolean matched = m.find() && PROVINCE_LIST.contains(m.group(1));
		if(matched) {
			
			address = contents.substring(m.end());
		}
		
		KoreanAddress instance = null;
		for(Type t: Type.values()) {
			
			try {
				instance = t.getInstance(address);
			}
			catch (InstantiationException e) {
				log.warn("can't instantiate [" + address + "]!", e);
			}
			catch (IllegalAccessException e) {
				log.warn("can't access [" + address + "]!", e);
			}
			
			if(instance!=null) {
				
				if(addressProvider!=null) {
					instance.setAddressProvider(addressProvider);
					if(matched) {
						instance.setDomains(new AddressDomain(m.group(1), m.group(2), new AddressDomain(m.group(3), m.group(4), "")));
					}
				}
				
				break;
			}
		}
		
		return instance;
	}
	
	/**
	 * <p>기초자치단체 >	읍·면·동 > 통·리 > 번지/호</p>
	 * <p>같은 경우(level 3)</p>
	 * 
	 * @param address
	 * @return
	 */
	public boolean equals(KoreanAddress address) {
		return equals(address, 3);
	}

	/**
	 * @param thatAddress
	 * @param level
	 * @return
	 */
	public boolean equals(KoreanAddress thatAddress, int level) {

		if(thatAddress==null) {
			return false;
		}
		
		KoreanAddress thisAddress = this;
		if(thatAddress.getType()!=this.getType()) {
			thisAddress = getOtherType();
		}
		
		String cores1 = thisAddress.getNormalized(level);
		String cores2 = thatAddress.getNormalized(level);
		
		return cores1.equals(cores2);
	}
	
	/**
	 * @return
	 */
	public KoreanAddress getOtherType() {
		
		if(addressProvider==null) {
			return this;
		}

		return addressProvider.getAddress(type.getOtherType(), getContents());
	}

	/**
	 * @return
	 */
	public RoadNameAddress getRoadNameAddress() {
		
		if(type==Type.RoadName) {
			return (RoadNameAddress)this;
		}
		
		return addressProvider==null? null: (RoadNameAddress)addressProvider.getAddress(Type.RoadName, getContents());
	}

	/**
	 * @return
	 */
	public LandNumberAddress getLandNumberAddress() {
		
		if(type==Type.LandNumber) {
			return (LandNumberAddress)this;
		}
		
		return addressProvider==null? null: (LandNumberAddress)addressProvider.getAddress(Type.LandNumber, getContents());
	}
	
	/**
	 * @return
	 */
	public String getNormalized() {
		return getNormalized(0);
	}

	abstract String getNormalized(int level);

	abstract String getStandardized();

	public AddressProvider getAddressProvider() {
		return addressProvider;
	}

	public void setAddressProvider(AddressProvider addressProvider) {
		this.addressProvider = addressProvider;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public AddressDomain getDomains() {
		return domains;
	}

	public String getContents() {
		return domains==null? null: domains.getStandardized();
	}
	
	public void setDomains(AddressDomain contents) {
		this.domains = contents;
	}
}
