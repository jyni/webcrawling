package kr.okplace.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Superman
 */
public class RoadNameAddress extends KoreanAddress implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(RoadNameAddress.class);
	
	private static final List<String> PROVINCE_LIST = Arrays.asList("서울","부산","대구","인천","광주","대전","울산 ","세종","경기","강원","충북","충남","전북","전남","경북","경남","제주","충청북","충청남","전라북","전라남","경상북","경상남");
	private static final int PROVINCE_SIZE = PROVINCE_LIST.size() - 6;
	
	private static final Pattern SIMPLE_PATTERN = Pattern.compile("경기도 화성시 팔탄면 푸른들판로 661-3", Pattern.UNICODE_CASE);

	/* (non-Javadoc)
	 * @see kr.okplace.base.KoreanAddress#getOtherType()
	 */
	@Override
	public KoreanAddress getOtherType() {
		// TODO Auto-generated method stub
		return this;
	}

	/* (non-Javadoc)
	 * @see kr.okplace.base.KoreanAddress#getNormalized(int)
	 */
	@Override
	public String getNormalized(int level) {
		return getDomains();
	}

	/* (non-Javadoc)
	 * @see kr.okplace.base.KoreanAddress#getStandardized()
	 */
	@Override
	public String getStandardized() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	public void parse() {
		// TODO Auto-generated method stub
		
	}
}
