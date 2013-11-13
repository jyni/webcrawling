package kr.okplace.base;

import java.util.Date;

/**
 * <h1>기간 정보</h1>
 * <p>시작일과 종료일의 날짜 정보</p>
 * 
 * @author Superman
 */
public class Period {

	private Date opening;
	private Date closing;
	
	/**
	 * 기간 시작일 반환
	 * 
	 * @return 기간 시작일
	 */
	public Date getOpening() {
		return opening;
	}
	/**
	 * 기간 시작일 저장
	 * 
	 * @param opening 기간 시작일
	 */
	public void setOpening(Date opening) {
		this.opening = opening;
	}
	/**
	 * 기간 종료일 반환
	 * 
	 * @return 기간 종료일
	 */
	public Date getClosing() {
		return closing;
	}
	/**
	 * 기간 종료일 저장
	 * 
	 * @param closing 기간 종료일
	 */
	public void setClosing(Date closing) {
		this.closing = closing;
	}
}
