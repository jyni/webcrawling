package kr.okplace.job.common;

import kr.okplace.job.smartdial.SmartDialDelivery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

/**
 * <h1></h1>
 * <p></p>
 * 
 * @author Superman
 */
public interface SmartDialDeliveryMapper {
	
	/**
	 * @param benefit
	 * @return
	 */
	@Insert({
		"INSERT INTO MERCHANT (",
		"merchant_seq, external_key, status, brand_name, merchant_name, phone, function, provider_code, ",
		"poi_yn, delivery_yn, deliv_indus_cd, reg_dttm, inserter, upd_dttm, updater) ",
		"VALUES (MERCHANT_SEQ.NEXTVAL, #{poi_id,jdbcType=VARCHAR}, '1', #{brand_name,jdbcType=VARCHAR}, #{merchant_name,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR}, ",
		"'{\"functionCodeList\":[\"F02\"],\"functions\":{\"deliveries\":[{\"bizType\":\"${industryCategoryCode}\",\"area\":\"\",\"hours\":\"\"}]}}', ",
		"'775', 'Y', 'B', #{industryCategoryCode,jdbcType=VARCHAR}, SYSDATE, 'POI_BACK_FILL', SYSDATE, 'POI_BACK_FILL')"
	})
	int insert(SmartDialDelivery smartDialDelivery);
	
	
	/**
	 * @param benefit
	 * @return
	 */
	@Insert({
		"MERGE INTO location l USING dual ON (",
		"l.poi_id=#{poi_id,jdbcType=VARCHAR}) ",
		"WHEN MATCHED THEN UPDATE SET xpos=#{xpos,jdbcType=VARCHAR}, ypos=#{ypos,jdbcType=VARCHAR} ",
		"WHEN NOT MATCHED THEN ",
		"INSERT (location_seq,poi_id,xpos,ypos) ",
		"VALUES(LOCATION_SEQ.NEXTVAL, #{poi_id,jdbcType=VARCHAR}, #{xpos,jdbcType=VARCHAR}, #{ypos,jdbcType=VARCHAR})"
	})
	int insertMerge(SmartDialDelivery smartDialDelivery);
	
	/**
	 * @param benefit
	 * @return
	 */
	@Insert({
		"INSERT INTO MERCHANT_LOCATION(merchant_seq,location_seq,address) ",
		"VALUES((SELECT MAX(merchant_seq) FROM merchant WHERE external_key=#{poi_id,jdbcType=VARCHAR} and provider_code = '775'),",
		"(SELECT MIN(location_seq) FROM location WHERE poi_id=#{poi_id,jdbcType=VARCHAR}), #{address,jdbcType=VARCHAR})"
	})
	int insertMerchantLocation(SmartDialDelivery smartDialDelivery);
	
	/**
	 * @param benefit
	 * @return
	 */
	@Insert({
		"INSERT INTO MERCH_DELIV_AREA (merchant_seq) ",
		"VALUES ((SELECT MAX(merchant_seq) FROM merchant WHERE external_key=#{poi_id,jdbcType=VARCHAR} and provider_code = '775'))"
	})
	int insertMerchDelivArea(SmartDialDelivery smartDialDelivery);
}