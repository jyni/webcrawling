package kr.okplace.job.common;

import java.util.Date;

import kr.okplace.benefit.domain.BenefitKey;
import kr.okplace.benefit.domain.MerchantLocationBenefit;
import kr.okplace.benefit.domain.ProviderBenefit;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <h1></h1>
 * <p></p>
 * 
 * @author Superman
 */
public interface MerchantLocationBenefitMapper {

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
			"m.PROVIDER_CODE = #{providerCode,jdbcType=VARCHAR} and",
			"m.EXTERNAL_KEY = #{merchantExternalKey,jdbcType=VARCHAR}",
		";",
	};
	
	public static final String[] INSERT_QUERY_POI = {
    		"merge into LOCATION using dual on (",
    			"POI_ID = #{location.poiId,jdbcType=VARCHAR}",
    		")",
    		"when not matched then",
		    "insert (",
		    	" LOCATION_SEQ, POI_ID, XPOS, YPOS",
		    ")",
		    "values (",
		    	" LOCATION_SEQ.nextval, #{location.poiId,jdbcType=VARCHAR}, ",
		    	" #{location.centerX,jdbcType=VARCHAR}, #{location.centerY,jdbcType=VARCHAR}",
			");",
				
			"select ",
				" l.LOCATION_SEQ into locationKey",
			"from",
				" LOCATION l",
			"where",
				" l.POI_ID = #{location.poiId,jdbcType=VARCHAR} and",
				" rownum = 1;"
	};
	
	public static final String[] INSERT_QUERY_NON_POI = {
		    "insert into LOCATION (",
		    	" LOCATION_SEQ, XPOS, YPOS",
		    ")",
		    "values (",
		    	" LOCATION_SEQ.nextval, #{location.centerX,jdbcType=VARCHAR}, #{location.centerY,jdbcType=VARCHAR}",
			");",
			
			"select LOCATION_SEQ.currval into locationKey from dual;"
	};
	
	public static final String[] INSERT_QUERY_TAIL = {
    		"merge into BENEFIT using dual on (",
    			"PROVIDER_BENEFIT_SEQ = #{providerBenefitKey,jdbcType=DECIMAL} and",
				"EXTERNAL_KEY = #{benefit.externalKey,jdbcType=VARCHAR}",
			")",
			"when matched then",
		    "update set",
		    	" BENEFIT_NAME = #{benefit.benefitName,jdbcType=VARCHAR}, ",
		    	" ISSUE_DATE = #{benefit.issueDate,jdbcType=DATE}, ",
		    	" EXPIRE_DATE = #{benefit.expireDate,jdbcType=DATE}, ",
		    	" POTENT_DEGREE = #{benefit.potentDegree,jdbcType=DECIMAL}, ",
		    	" BENEFIT_RATE = #{benefit.benefitRate,jdbcType=DECIMAL}, ",
		    	" BENEFIT_AMOUNT = #{benefit.benefitAmount,jdbcType=DECIMAL}, ",
		    	" NORMAL_PRICE = #{benefit.normalPrice,jdbcType=DECIMAL}, ",
		    	" DISCOUNT_PRICE = #{benefit.discountPrice,jdbcType=DECIMAL}, ",
		    	" BENEFIT_BRIEF = #{benefit.benefitBrief,jdbcType=VARCHAR},",
		    	" USE_CONDITIONS = #{benefit.useConditions,jdbcType=VARCHAR}, ",
		    	" LINKAGE = #{benefit.linkage,jdbcType=VARCHAR}, ",
		    	" BENEFIT_DETAIL = #{benefit.benefitDetail,jdbcType=VARCHAR}, ",
		    	" SIDE_INFO = #{benefit.sideInformation,jdbcType=VARCHAR}, ",
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
		    	" BENEFIT_SEQ.nextval, #{providerBenefitKey,jdbcType=DECIMAL}, #{benefit.externalKey,jdbcType=VARCHAR}, '1', ",
		    	" #{benefit.benefitName,jdbcType=VARCHAR}, #{benefit.issueDate,jdbcType=DATE}, #{benefit.expireDate,jdbcType=DATE}, ",
		    	" #{benefit.potentDegree,jdbcType=DECIMAL}, #{benefit.benefitRate,jdbcType=DECIMAL}, ",
		    	" #{benefit.benefitAmount,jdbcType=DECIMAL}, #{benefit.normalPrice,jdbcType=DECIMAL}, #{benefit.discountPrice,jdbcType=DECIMAL}, ",
		    	" #{benefit.benefitBrief,jdbcType=VARCHAR}, #{benefit.useConditions,jdbcType=VARCHAR}, ",
		    	" #{benefit.linkage,jdbcType=VARCHAR}, #{benefit.benefitDetail,jdbcType=VARCHAR}, #{benefit.sideInformation,jdbcType=VARCHAR}, ",
		    	" sysdate, sysdate ",
			");",
			
			"select",
	    		" b.BENEFIT_SEQ into benefitKey ",
			"from",
				" BENEFIT b",
			"where",
				" b.PROVIDER_BENEFIT_SEQ = #{providerBenefitKey,jdbcType=DECIMAL} and",
				" b.EXTERNAL_KEY = #{benefit.externalKey,jdbcType=VARCHAR};",

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

		public String getInsertQuery(MerchantLocationBenefit benefit) {
			
			String query = "";
			for(String s: INSERT_QUERY_HEADER) {
				query = query.concat(s).concat("\n");
			}

			String poiId = benefit.getLocation().getPoiId();
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
	 * @param providerBenefit
	 * @return
	 */
	@Select({
	 	"select PROVIDER_BENEFIT_SEQ",
	 	"from",
	 		"PROVIDER_BENEFIT",
	 	"where",
	 		"CATEG_DEPTH1 = #{categoryDepth1,jdbcType=VARCHAR} and",
	 		"CATEG_DEPTH2 = #{categoryDepth2,jdbcType=VARCHAR} and",
	 		"nvl(CATEG_DEPTH3, '000') = nvl(#{categoryDepth3,jdbcType=VARCHAR}, '000')"
	})
	Integer selectProviderBenefitKyByBenefitCategoryPair(ProviderBenefit providerBenefit);
	
	/**
	 * @param providerBenefitKey
	 * @return
	 */
	@Select({
		"select max(UPDATE_DATE) from BENEFIT",
		"where",
			" PROVIDER_BENEFIT_SEQ = #{providerBenefitKey,jdbcType=DECIMAL}"
	})
	Date selectLastUpdateDate(Integer providerBenefitKey);
	
	/**
	 * @param benefit
	 * @return
	 */
	@InsertProvider(type=CommonQueryProvider.class, method="getInsertQuery")
	int insert(MerchantLocationBenefit benefit);
	
	/**
	 * @param benefit
	 * @return
	 */
	@Update({
    	"BEGIN",
		     "update MERCHANT set",
			     "MERCHANT_NAME = #{merchant.merchantName,jdbcType=VARCHAR}, ",
			     "PHONE = #{merchant.phone,jdbcType=VARCHAR}",
			 "where",
				 "EXTERNAL_KEY = #{merchant.externalKey,jdbcType=VARCHAR};",
	
		     "update LOCATION set",
			     "XPOS = #{location.centerX,jdbcType=VARCHAR}, ",
			     "YPOS = #{location.centerY,jdbcType=VARCHAR}",
		     "where",
				 "POI_ID = #{location.poiId,jdbcType=VARCHAR};",
	
		     "update BENEFIT set",
		    	" BENEFIT_NAME = #{benefit.benefitName,jdbcType=VARCHAR},",
		    	" POTENT_DEGREE = #{benefit.potentDegree,jdbcType=DECIMAL},",
		    	" BENEFIT_RATE = #{benefit.benefitRate,jdbcType=DECIMAL}, ",
		    	" BENEFIT_AMOUNT = #{benefit.benefitAmount,jdbcType=DECIMAL},",
		    	" NORMAL_PRICE = #{benefit.normalPrice,jdbcType=DECIMAL},",
		    	" DISCOUNT_PRICE = #{benefit.discountPrice,jdbcType=DECIMAL}, ",
		    	" BENEFIT_BRIEF = #{benefit.benefitBrief,jdbcType=VARCHAR},",
		    	" USE_CONDITIONS = #{benefit.useConditions,jdbcType=VARCHAR}, ",
		    	" LINKAGE = #{benefit.linkage,jdbcType=VARCHAR},",
		    	" UPDATE_DATE = sysdate ",
		     "where",
				"PROVIDER_BENEFIT_SEQ = #{providerBenefitKey,jdbcType=DECIMAL} and",
				"EXTERNAL_KEY = #{benefit.externalKey,jdbcType=VARCHAR};",
	     "END;"
	})
	int update(MerchantLocationBenefit benefit);

	/**
	 * @param benefit
	 * @return
	 */
	@Delete({
		"begin",
			"delete from LOCATION where POI_ID = #{location.poiId,jdbcType=VARCHAR};",
			"delete from MERCHANT where EXTERNAL_KEY = #{merchant.externalKey,jdbcType=VARCHAR};",
			"delete from BENEFIT",
			"where",
				"PROVIDER_BENEFIT_SEQ = #{providerBenefitKey,jdbcType=DECIMAL} and",
				"EXTERNAL_KEY = #{benefit.externalKey,jdbcType=VARCHAR};",
		"end;"
	})
	int deleteByMerchantLocationBenefitKey(MerchantLocationBenefit benefit);

	/**
	 * @param providerBenefit
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
		        			"b.BENEFIT_SEQ = mb.BENEFIT_SEQ",
		        		")",
		        		"inner join PROVIDER_BENEFIT pv on (",
		        			"pv.PROVIDER_BENEFIT_SEQ = b.PROVIDER_BENEFIT_SEQ and",
					 		"pv.CATEG_DEPTH1 = #{categoryDepth1,jdbcType=VARCHAR} and",
					 		"pv.CATEG_DEPTH2 = #{categoryDepth2,jdbcType=VARCHAR} and",
					 		"nvl(CATEG_DEPTH3, '000') = nvl(#{categoryDepth3,jdbcType=VARCHAR}, '000')",
		        		")",
		        	"where",
		        		"mb.MERCHANT_SEQ = z.MERCHANT_SEQ",
	        	");",

	        "delete from BENEFIT z",
	        "where",
		        "z.BENEFIT_SEQ not in (select BENEFIT_SEQ from DISPLAY) and exists (",
		    		"select distinct",
		    			"pv.PROVIDER_BENEFIT_SEQ",
		    		"from",
		        		"PROVIDER_BENEFIT pv",
		        	"where",
	        			"pv.PROVIDER_BENEFIT_SEQ = z.PROVIDER_BENEFIT_SEQ and",
				 		"pv.CATEG_DEPTH1 = #{categoryDepth1,jdbcType=VARCHAR} and",
				 		"pv.CATEG_DEPTH2 = #{categoryDepth2,jdbcType=VARCHAR} and",
				 		"nvl(CATEG_DEPTH3, '000') = nvl(#{categoryDepth3,jdbcType=VARCHAR}, '000')",
				");",

	        "delete from LOCATION l",
	        "where",
	        	"not exists (",
		    		"select l.LOCATION_SEQ ",
		    		"from",
		    			"MERCHANT_LOCATION ml",
		    		"where",
		    			"ml.LOCATION_SEQ = l.LOCATION_SEQ",
		    	");",
   	    "END;"
    })
    int deleteByProviderBenefit(ProviderBenefit providerBenefit);

	/**
	 * @param providerBenefitKey
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
		        		"mb.MERCHANT_SEQ = z.MERCHANT_SEQ",
	        	");",
	
	        "delete from BENEFIT z",
	        "where",
		        "z.BENEFIT_SEQ not in (select BENEFIT_SEQ from DISPLAY) and exists (",
		    		"select distinct",
		    			"pv.PROVIDER_BENEFIT_SEQ",
		    		"from",
		        		"PROVIDER_BENEFIT pv",
		        	"where",
	        			"pv.PROVIDER_BENEFIT_SEQ = z.PROVIDER_BENEFIT_SEQ and",
	        			"pv.PROVIDER_BENEFIT_SEQ = #{providerBenefitKey,jdbcType=DECIMAL}",
				");",
	
	        "delete from LOCATION l",
	        "where",
	        	"not exists (",
		    		"select l.LOCATION_SEQ ",
		    		"from",
		    			"MERCHANT_LOCATION ml",
		    		"where",
		    			"ml.LOCATION_SEQ = l.LOCATION_SEQ",
		    	");",
	    "END;"
    })
    int deleteByProviderBenefitKey(Integer providerBenefitKey);

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
						"b.EXTERNAL_KEY = #{externalKey,jdbcType=VARCHAR}",
	        	");",
	
	        "delete from BENEFIT z",
	        "where",
				"z.EXTERNAL_KEY = #{externalKey,jdbcType=VARCHAR} and",
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
    int deleteByBenefitKey(BenefitKey key);
    
    /**
     * @return
     */
    @Delete({
      	"BEGIN",
	        "delete",
	        "from MERCHANT m",
	        "where not exists (",
	    		"select mb.MERCHANT_SEQ ",
	    		"from MERCHANT_BENEFIT mb",
	    		"where",
	    			"mb.MERCHANT_SEQ = m.MERCHANT_SEQ",
	    	");",
	      	
	        "delete",
	        "from LOCATION l",
	        "where not exists (",
	    		"select l.LOCATION_SEQ ",
	    		"from MERCHANT_LOCATION ml",
	    		"where",
	    			"ml.LOCATION_SEQ = l.LOCATION_SEQ",
	    	");",
	    "END;"
    })
    int deleteOrphanedMerchant();
    /**
     * @return
     */
    @Delete({
      	"BEGIN",
	        "delete",
	        "from BENEFIT b",
	        "where not exists (",
	    		"select d.BENEFIT_SEQ ",
	    		"from DISPLAY d",
	    		"where",
	    			"d.BENEFIT_SEQ = b.BENEFIT_SEQ",
	    		")",	      	
	    	"and b.EXPIRE_DATE &lt; TO_DATE(TO_CHAR(SYSDATE,'YYYYMMDD')||'000000','YYYYMMDDHH24MISS');",
	    "END;"
    })
	void deleteBenefitExpireDataTasklet();
}