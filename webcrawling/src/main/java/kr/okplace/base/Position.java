package kr.okplace.base;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sk.mps.util.Coordination;

/**
 * <h1>위치 정보와 좌표변환</h1>
 * <p>위치 정보를 저장하는 Class. 좌표 변환 Logic을 가지고 있다.</p>
 * <ul>
 *	<li>SK 정규화 좌표 (x, y)를  Integer 타입으로 저장하고 long 타입으로 반환한다. ({@link #setX(Integer) setX(Integer)}, {@link #getX() getX()},
 *  {@link #setY(Integer) setY(Integer)}, {@link #getY() getY()})</li>
 * <li>SK 정규화 좌표 (x, y)를  String 타입으로 저장하고 반환한다. ({@link #setStringX(String) setStringX(String)},
 *  {@link #getStringX() getStringX()}, {@link #setStringY(String) setStringY(String)}, {@link #getStringY() getStringY()})</li>
 * <li>Degree 좌표 (longitude, latitude)를  String 타입으로 저장하고 반환한다. ({@link #setLongitude(String) setLongitude(String)},
 *  {@link #getLongitude() getLongitude()}, {@link #setLatitudeY(String) setLatitude(String)}, {@link #getLatitude() getLatitude()})</li>
 * </ul>
 * 
 * @author Superman
 * @see kr.okplace.benefit.domain.Location#getCenter()
 * @see kr.okplace.benefit.domain.MerchantLocation#getEntrance()
 * @see com.sk.mps.util.Coordination#point2Degree(String)
 * @see com.sk.mps.util.Coordination#pos2Point(double)
 */
public class Position implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(Position.class);
	
	private Integer x;
	private Integer y;
	
	/**
	 * SK 정규화 좌표 x를  long 타입으로 반환한다.
	 * 
	 * @return SK 정규화 좌표 x를  long 타입으로 반환.
	 */
	public long getX() {
		return x;
	}
	/**
	 * SK 정규화 좌표 x를 String 타입으로 반환한다.
	 * 
	 * @returnSK 정규화 좌표 x를 String 타입으로 반환.
	 */
	public String getStringX() {
		
		try {
			return x==null? null: Integer.valueOf(x).toString();
		}
		catch (Exception e) {
			log.debug("Number[" + x + "] Converting Error!", e);
			return null;
		}
	}
	/**
	 * Degree 경도 좌표 longitude를 String 타입으로 반환한다.
	 * 
	 * @return Degree 경도 좌표 longitude를 String 타입으로 반환
	 * @see com.sk.mps.util.Coordination#point2Degree(String)
	 * @see com.sk.mps.util.Coordination#pos2Point(int)
	 */
	public String getLongitude() {
		
		try {
			return x==null? null: Double.valueOf(Coordination.point2Degree(Coordination.pos2Point(x))).toString();
		}
		catch (Exception e) {
			log.debug("Coordinate Converting Error for value of " + x + "!", e);
			return null;
		}
	}
	/**
	 * String 타입의 Degree 경도 좌표 longitude를  저장한다.
	 * 
	 * @param longitude String 타입의 Degree 경도 좌표
	 * @see com.sk.mps.util.Coordination#degree2Pos(double)
	 */
	public void setLongitude(String longitude) {
		
		if(longitude==null || longitude.length()==0) {
			return;
		}
		
		try {
			this.x = Coordination.degree2Pos(Double.valueOf(longitude));
		}
		catch (NumberFormatException e) {
			log.debug("Coordinate Converting Error for longitude of " + longitude + "!", e);
		}
	}
	/**
	 * String 타입의 SK 정규화 x좌표를  저장한다.
	 * 
	 * @param xs String 타입의 SK 정규화 x좌표
	 */
	public void setStringX(String xs) {
		
		if(xs==null || xs.length()==0) {
			return;
		}
		
		try {
			this.x = Integer.valueOf(xs);
		}
		catch (NumberFormatException e) {
			log.debug("Number[" + xs + "] Converting Error!", e);
		}
	}
	/**
	 * Integer 타입의 SK 정규화 x좌표를  저장한다.
	 * 
	 * @param x Integer 타입의 SK 정규화 x좌표
	 */
	public void setX(Integer x) {
		this.x = x;
	}
	
	/**
	 * SK 정규화 좌표 y를 long 타입으로 반환한다.
	 * 
	 * @return SK 정규화 좌표 y를 long 타입으로 반환.
	 */
	public long getY() {
		return y;
	}
	/**
	 * SK 정규화 좌표 y를 String 타입으로 반환한다.
	 * 
	 * @returnSK 정규화 좌표 y를 String 타입으로 반환.
	 */
	public String getStringY() {
		
		try {
			return y==null? null: Integer.valueOf(y).toString();
		}
		catch (Exception e) {
			log.debug("Number[" + y + "] Converting Error!", e);
			return null;
		}
	}
	/**
	 * Degree 위도 좌표 latitude를 String 타입으로 반환한다.
	 * 
	 * @return Degree 좌표 latitude를 String 타입으로 반환.
	 * @see com.sk.mps.util.Coordination#point2Degree(String)
	 * @see com.sk.mps.util.Coordination#pos2Point(int)
	 */
	public String getLatitude() {
		
		try {
			return y==null? null: Double.valueOf(Coordination.point2Degree(Coordination.pos2Point(y))).toString();
		}
		catch (Exception e) {
			log.debug("Number[" + y + "] Converting Error!", e);
			return null;
		}
	}
	/**
	 * String 타입의 Degree 위도 좌표 latitude를  저장한다.
	 * 
	 * @param latitude String 타입의 Degree 위도 좌표.
	 * @see com.sk.mps.util.Coordination#degree2Pos(double)
	 */
	public void setLatitude(String latitude) {
		
		if(latitude==null || latitude.length()==0) {
			return;
		}
		
		try {
			this.y = Coordination.degree2Pos(Double.valueOf(latitude));
		}
		catch (NumberFormatException e) {
			log.debug("Coordinate Converting Error for latitude of " + latitude + "!", e);
		}
	}
	/**
	 * String 타입의 SK 정규화 y좌표를  저장한다.
	 * 
	 * @param ys String 타입의 SK 정규화 y좌표.
	 */
	public void setStringY(String ys) {
		
		if(ys==null || ys.length()==0) {
			return;
		}
		
		try {
			this.y = Integer.valueOf(ys);
		}
		catch (NumberFormatException e) {
			log.debug("Number[" + ys + "] Converting Error!", e);
		}
	}
	/**
	 * Integer 타입의 SK 정규화 y좌표를  저장한다.
	 * 
	 * @param y Integer 타입의 SK 정규화 y좌표.
	 */
	public void setY(Integer y) {
		this.y = y;
	}
}
