package kr.okplace.job.common;

import java.util.Date;
import java.util.List;

import kr.okplace.benefit.domain.Delivery;
import kr.okplace.benefit.domain.IndustryCategory;
import kr.okplace.benefit.domain.MerchantLocationInterface;
import kr.okplace.benefit.domain.MerchantLocationKey;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

/**
 * <h1></h1>
 * <p></p>
 * 
 * @author Superman
 */
public interface MerchantLocationInterfaceMapper {

	public static final String[] INSERT_QUERY_HEADER = {
		"declare",
			"locationKey LOCATION.LOCATION_SEQ%TYPE;",
			"merchantKey MERCHANT.MERCHANT_SEQ%TYPE;",
			"tvalue NUMBER;",
    	"BEGIN",
			"merge into MERCHANT using dual on (",
				"PROVIDER_CODE = #{providerCode,jdbcType=VARCHAR} and",
				"EXTERNAL_KEY = #{merchantExternalKey,jdbcType=VARCHAR}",
			")",
			"when not matched then",
			"insert (",
		    	" MERCHANT_SEQ, PARENT_SEQ, PROVIDER_CODE, EXTERNAL_KEY,",
		    	" STATUS, POI_YN, DELIVERY_YN, INDUSTRY_CATEG_CD, DELIV_INDUS_CD,",
		    	" BRAND_NAME, MERCHANT_NAME, PHONE, DETAILS, FUNCTION,",
		    	" REG_DTTM, INSERTER, UPD_DTTM, UPDATER",
			")",
			"values (",
		    	" MERCHANT_SEQ.nextval, #{merchantParentKey,jdbcType=DECIMAL},",
		    	" #{providerCode,jdbcType=VARCHAR}, #{merchantExternalKey,jdbcType=VARCHAR},",
		    	" '1', #{poiYn,jdbcType=VARCHAR}, #{deliveryYn,jdbcType=VARCHAR},",
		    	" #{industryCategoryCode,jdbcType=VARCHAR}, #{deliveryCategoryCode,jdbcType=VARCHAR},",
		    	" #{brandName,jdbcType=VARCHAR}, #{merchantName,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR},",
		    	" #{merchantDetails,jdbcType=VARCHAR}, #{functions,jdbcType=VARCHAR},",
		    	" nvl(#{createDate,jdbcType=DATE}, current_timestamp), 'BATCH_WORKER',",
		    	" nvl(#{updateDate,jdbcType=DATE}, current_timestamp), 'BATCH_WORKER'",
			");",
			
			"select ",
				" m.MERCHANT_SEQ into merchantKey",
			"from",
				" MERCHANT m",
			"where",
				"m.PROVIDER_CODE = #{providerCode,jdbcType=VARCHAR} and",
				"m.EXTERNAL_KEY = #{merchantExternalKey,jdbcType=VARCHAR}",
			";",
	};
	
	public static final String[] INSERT_QUERY_POI = {
    		"merge into LOCATION using dual on (",
    			"POI_ID = #{poiId,jdbcType=VARCHAR}",
    		")",
    		"when not matched then",
		    "insert (",
		    	" LOCATION_SEQ, POI_ID, XPOS, YPOS",
		    ")",
		    "values (",
		    	" LOCATION_SEQ.nextval, #{poiId,jdbcType=VARCHAR}, ",
		    	" #{centerX,jdbcType=VARCHAR}, #{centerY,jdbcType=VARCHAR}",
			");",
				
			"select ",
				" l.LOCATION_SEQ into locationKey",
			"from",
				" LOCATION l",
			"where",
				" l.POI_ID = #{poiId,jdbcType=VARCHAR} and",
				" rownum = 1;",
	};
	
	public static final String[] INSERT_QUERY_NON_POI = {
		    "insert into LOCATION (",
		    	" LOCATION_SEQ, XPOS, YPOS",
		    ")",
		    "values (",
		    	" LOCATION_SEQ.nextval, #{centerX,jdbcType=VARCHAR}, #{centerY,jdbcType=VARCHAR}",
			");",
			
			"select LOCATION_SEQ.currval into locationKey from dual;",
	};
	
	public static final String[] INSERT_QUERY_TAIL = {
			"select count(1) into tvalue from MERCHANT_LOCATION",
			"where",
				"MERCHANT_SEQ = merchantKey and",
				"LOCATION_SEQ = locationKey and",
				"rownum = 1;",
				
			"if tvalue = 0 then",
				"insert into MERCHANT_LOCATION (",
			    	" LOCATION_SEQ, MERCHANT_SEQ, ADDRESS ",
				")",
		    	"values (",
		    		" locationKey, merchantKey, #{address,jdbcType=VARCHAR} ",
		    	");",
	    	"end if;",
	    	
	     "END;"
	};
	
	public class CommonQueryProvider {

		public String getInsertQuery(MerchantLocationInterface merchantLocation) {
			
			String query = "";
			for(String s: INSERT_QUERY_HEADER) {
				query = query.concat(s).concat("\n");
			}

			String poiId = merchantLocation.getPoiId();
			String[] body = poiId!=null && poiId.length()>0? INSERT_QUERY_POI: INSERT_QUERY_NON_POI;
			for(String s: body) {
				query = query.concat(s).concat("\n");
			}

			for(String s: INSERT_QUERY_TAIL) {
				query = query.concat(s).concat("\n");
			}
			
			return query;
		}

		public String getUpdateQuery(MerchantLocationInterface merchantLocation) {
			
			String query = "";
			for(String s: INSERT_QUERY_HEADER) {
				query = query.concat(s).concat("\n");
			}

			String poiId = merchantLocation.getPoiId();
			String[] body = poiId!=null && poiId.length()>0? INSERT_QUERY_POI: INSERT_QUERY_NON_POI;
			for(String s: body) {
				query = query.concat(s).concat("\n");
			}

			for(String s: INSERT_QUERY_TAIL) {
				query = query.concat(s).concat("\n");
			}
			
			return query;
		}
	}
	
	/**
	 * @param providerCode
	 * @return last date to be updated
	 */
	@Select({
		"select max(UPD_DTTM) from MERCHANT",
		"where",
			"UPDATER = 'BATCH_WORKER' and ",
			"PROVIDER_CODE = #{providerCode,jdbcType=VARCHAR}"
	})
	Date selectLastUpdateDate(String providerCode);

	@Select({
		" select",
			" INDUSTRY_CATEG_SEQ, PARENT_SEQ, INDUSTRY_CATEG_CD, USE_CLASS, ",
			" CATEG_DEPTH, CATEG_ORDER, CATEG_CODE, CATEG_NAME, CATEG_RATING ",
		" from",
			" INDUSTRY_CATEG ",
		" where",
			" USE_CLASS = #{useTarget,jdbcType=VARCHAR}"
	})
	@Results({
		@Result(column="INDUSTRY_CATEG_SEQ", property="key", 	jdbcType=JdbcType.DECIMAL),
		@Result(column="PARENT_SEQ", 	property="parentKey", 	jdbcType=JdbcType.DECIMAL),
		@Result(column="INDUSTRY_CATEG_CD", property="industryCategoryCode", jdbcType=JdbcType.VARCHAR),
		@Result(column="USE_CLASS", 	property="useTarget", 	jdbcType=JdbcType.VARCHAR),
		@Result(column="CATEG_DEPTH", 	property="depth", 	jdbcType=JdbcType.DECIMAL),
		@Result(column="CATEG_ORDER", 	property="order", 	jdbcType=JdbcType.DECIMAL),
		@Result(column="CATEG_CODE", 	property="code", 	jdbcType=JdbcType.VARCHAR),
		@Result(column="CATEG_NAME", 	property="name", 	jdbcType=JdbcType.VARCHAR),
		@Result(column="CATEG_RATING", 	property="rating", 	jdbcType=JdbcType.VARCHAR)
	})
	List<IndustryCategory> selectIndustryCategories(String useTarget);
	
	/**
	 * @param merchantLocation
	 * @return
	 */
	@InsertProvider(type=CommonQueryProvider.class, method="getInsertQuery")
    int insert(MerchantLocationInterface merchantLocation);

	/**
	 * @param merchantLocation
	 * @return
	 */
	@Insert({
		"insert into MERCH_DELIV_AREA (",
			"MERCHANT_SEQ, DELIV_AREA",
		")",
		"select ",
			"MERCHANT_SEQ, #{delivery.area,jdbcType=VARCHAR}",
		"from",
			"MERCHANT",
		"where",
			"PROVIDER_CODE = #{merchant.providerCode,jdbcType=VARCHAR} and",
			"EXTERNAL_KEY = #{merchant.merchantExternalKey,jdbcType=VARCHAR}"
	})
	int insertDelivery(@Param("merchant") MerchantLocationInterface merchant, @Param("delivery") Delivery delivery);

	/**
	 * @param merchantLocation
	 * @return
	 */
	@UpdateProvider(type=CommonQueryProvider.class, method="getUpdateQuery")
    int update(MerchantLocationInterface merchantLocation);

    /**
     * @param key
     * @return
     */
    @Delete({
        "delete from MERCHANT",
        "where",
			"PROVIDER_CODE = #{providerCode,jdbcType=VARCHAR} and",
			"EXTERNAL_KEY = #{merchantExternalKey,jdbcType=VARCHAR}",
    })
    int deleteByMerchantLocationKey(MerchantLocationKey merchant);

	/**
	 * @param merchant
	 * @return
	 */
    @Delete({
    	"delete from MERCH_DELIV_AREA",
    	"where",
	    	"MERCHANT_SEQ = (",
		    	"select MERCHANT_SEQ from MERCHANT",
		    	"where",
					"PROVIDER_CODE = #{providerCode,jdbcType=VARCHAR} and",
					"EXTERNAL_KEY = #{merchantExternalKey,jdbcType=VARCHAR}",
			")"
    })
	int deleteDeliveriesByMerchantLocationKey(MerchantLocationKey merchant);
}