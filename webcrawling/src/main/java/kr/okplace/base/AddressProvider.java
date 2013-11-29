package kr.okplace.base;

import kr.okplace.base.KoreanAddress.Type;

public interface AddressProvider {
	
	/**
	 * @param type
	 * @param address
	 * @return
	 */
	public KoreanAddress getAddress(Type type, String address);
}
