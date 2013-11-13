package kr.okplace.job.common;

import kr.okplace.benefit.domain.BenefitInterface;
import kr.okplace.benefit.domain.MerchantLocationBenefitInterface;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;

/**
 * <h1></h1>
 * <p></p>
 * 
 * @author Superman
 */
public interface MerchantLocationBenefitInterfaceMapper {

	public static final String[] INSERT_QUERY_HEADER = {
		"declare",
			"locationKey LOCATION.LOCATION_SEQ%TYPE;",
			"merchantKey MERCHANT.MERCHANT_SEQ%TYPE;",
			"benefitKey BENEFIT.BENEFIT_SEQ%TYPE;",
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
	    	" current_timestamp, 'BATCH_WORKER', current_timestamp, 'BATCH_WORKER'",
		");",
			
			"select ",
				" m.MERCHANT_SEQ into merchantKey",
			"from",
				" MERCHANT m",
			"where",
				"PROVIDER_CODE = #{providerCode,jdbcType=VARCHAR} and",
				"EXTERNAL_KEY = #{merchantExternalKey,jdbcType=VARCHAR};"
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
    		"merge into BENEFIT using dual on (",
    			"PROVIDER_BENEFIT_SEQ = #{providerBenefitKey,jdbcType=DECIMAL} and",
				"EXTERNAL_KEY = #{benefitExternalKey,jdbcType=VARCHAR}",
			")",
			"when matched then",
		    "update set",
		    	" BENEFIT_NAME = #{benefitName,jdbcType=VARCHAR}, ",
		    	" ISSUE_DATE = #{issueDate,jdbcType=DATE}, ",
		    	" EXPIRE_DATE = #{expireDate,jdbcType=DATE}, ",
		    	" POTENT_DEGREE = #{potentDegree,jdbcType=DECIMAL}, ",
		    	" BENEFIT_RATE = #{benefitRate,jdbcType=DECIMAL}, ",
		    	" BENEFIT_AMOUNT = #{benefitAmount,jdbcType=DECIMAL}, ",
		    	" NORMAL_PRICE = #{normalPrice,jdbcType=DECIMAL}, ",
		    	" DISCOUNT_PRICE = #{discountPrice,jdbcType=DECIMAL}, ",
		    	" BENEFIT_BRIEF = #{benefitBrief,jdbcType=VARCHAR},",
		    	" USE_CONDITIONS = #{useConditions,jdbcType=VARCHAR}, ",
		    	" LINKAGE = #{linkage,jdbcType=VARCHAR}, ",
		    	" BENEFIT_DETAIL = #{benefitDetail,jdbcType=VARCHAR}, ",
		    	" SIDE_INFO = #{sideInformation,jdbcType=VARCHAR}, ",
		    	" UPDATE_DATE = sysdate ",
			"when not matched then",
			"insert (",
		    	" BENEFIT_SEQ, PROVIDER_BENEFIT_SEQ, EXTERNAL_KEY, STATUS, ",
		    	" BENEFIT_NAME, ISSUE_DATE, EXPIRE_DATE, ",
		    	" POTENT_DEGREE, BENEFIT_RATE, BENEFIT_AMOUNT, NORMAL_PRICE, DISCOUNT_PRICE, ",
		    	" BENEFIT_BRIEF, USE_CONDITIONS, LINKAGE, BENEFIT_DETAIL, SIDE_INFO, ",
		    	" CREATE_DATE, UPDATE_DATE ",
			")",
			"values (",
		    	" BENEFIT_SEQ.nextval, #{providerBenefitKey,jdbcType=DECIMAL}, #{benefitExternalKey,jdbcType=VARCHAR}, '1', ",
		    	" #{benefitName,jdbcType=VARCHAR}, #{issueDate,jdbcType=DATE}, #{expireDate,jdbcType=DATE}, ",
		    	" #{potentDegree,jdbcType=DECIMAL}, #{benefitRate,jdbcType=DECIMAL}, ",
		    	" #{benefitAmount,jdbcType=DECIMAL}, #{normalPrice,jdbcType=DECIMAL}, #{discountPrice,jdbcType=DECIMAL}, ",
		    	" #{benefitBrief,jdbcType=VARCHAR}, #{useConditions,jdbcType=VARCHAR}, ",
		    	" #{linkage,jdbcType=VARCHAR}, #{benefitDetail,jdbcType=VARCHAR}, #{sideInformation,jdbcType=VARCHAR}, ",
		    	" sysdate, sysdate ",
			");",
			
			"select",
	    		" b.BENEFIT_SEQ into benefitKey ",
			"from",
				" BENEFIT b",
			"where",
				" b.PROVIDER_BENEFIT_SEQ = #{providerBenefitKey,jdbcType=DECIMAL} and",
				" b.EXTERNAL_KEY = #{benefitExternalKey,jdbcType=VARCHAR};",

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
	
			"select count(1) into tvalue from MERCHANT_BENEFIT",
			"where",
				"MERCHANT_SEQ = merchantKey and",
				"BENEFIT_SEQ = benefitKey and",
				"rownum = 1;",
				
			"if tvalue = 0 then",
				"insert into MERCHANT_BENEFIT (",
			     	" BENEFIT_SEQ, MERCHANT_SEQ",
				")",
			    "values (",
			    	" benefitKey, merchantKey ",
				");",
	    	"end if;",
	    	
	     "END;"
	};
	
	public class CommonQueryProvider {

		public String getInsertQuery(MerchantLocationBenefitInterface merchantLocationBenefit) {
			
			String query = "";
			for(String s: INSERT_QUERY_HEADER) {
				query = query.concat(s).concat("\n");
			}

			String poiId = merchantLocationBenefit.getPoiId();
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
	 * @param merchantLocationBenefit
	 * @return
	 */
	@InsertProvider(type=CommonQueryProvider.class, method="getInsertQuery")
    int insert(MerchantLocationBenefitInterface merchantLocationBenefit);

    /**
     * @param key
     * @return
     */
    @Delete({
      	"BEGIN",
	        "delete from MERCHANT z",
	        "where",
		        "z.MERCHANT_SEQ not in (select MERCHANT_SEQ from DISPLAY) and exists (",
	        		"select mb.MERCHANT_SEQ",
	        		"from",
		        		"MERCHANT_BENEFIT mb",
		        		"inner join (",
	                    	"select",
	                    		"MERCHANT_SEQ, count(MERCHANT_SEQ) BENEFIT_COUNT",
	                    	"from",
	                    		"MERCHANT_BENEFIT",
	                    	"group by",
	                    		"MERCHANT_SEQ",
		        		") bc on (",
	        				"bc.MERCHANT_SEQ = mb.MERCHANT_SEQ and",
	        				"bc.BENEFIT_COUNT = 1",
	        			")",
		        		"inner join BENEFIT b on (",
		        			"b.BENEFIT_SEQ = mb.BENEFIT_SEQ and",
		        			"b.PROVIDER_BENEFIT_SEQ = #{providerBenefitKey,jdbcType=DECIMAL}",
		        		")",
		        	"where",
		        		"mb.MERCHANT_SEQ = z.MERCHANT_SEQ and",
						"b.EXTERNAL_KEY = #{benefitExternalKey,jdbcType=VARCHAR}",
	        	");",
	
	        "delete from BENEFIT z",
	        "where",
				"z.EXTERNAL_KEY = #{benefitExternalKey,jdbcType=VARCHAR} and",
		        "z.BENEFIT_SEQ not in (select BENEFIT_SEQ from DISPLAY) and exists (",
		    		"select distinct",
		    			"pv.PROVIDER_BENEFIT_SEQ",
		    		"from",
		        		"PROVIDER_BENEFIT pv",
		        	"where",
	        			"pv.PROVIDER_BENEFIT_SEQ = z.PROVIDER_BENEFIT_SEQ and",
	        			"pv.PROVIDER_BENEFIT_SEQ = #{providerBenefitKey,jdbcType=DECIMAL}",
				");",
		"END;"
    })
    int deleteByBenefitInterface(BenefitInterface benefit);
}