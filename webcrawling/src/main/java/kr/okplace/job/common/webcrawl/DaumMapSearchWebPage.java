package kr.okplace.job.common.webcrawl;

import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>http://map.search.daum.net/mapsearch/map.daum?callback=jQuery18107841847746167332_1382000083264&q=%EB%A5%B4%EB%BD%80%EB%AF%B8%EC%97%90&msFlag=S&page=1&sort=0</p>
 *		GET /mapsearch/map.daum?callback=jQuery18107841847746167332_1382000083264&q=%EB%A5%B4%EB%BD%80%EB%AF%B8%EC%97%90&msFlag=S&page=1&sort=0 HTTP/1.1
 *		Host: map.search.daum.net
 * 
 * @author Superman
 */
public class DaumMapSearchWebPage extends AbstractWebPage implements WebPage {

	private static final Log log = LogFactory.getLog(DaumMapSearchWebPage.class);

	/* (non-Javadoc)
	 * @see kr.okplace.job.common.webcrawl.AbstractWebPage#getURLConnection()
	 */
	@Override
	public URLConnection getURLConnection() throws Exception {
		
		URLConnection connection = getURL().openConnection();
		if (log.isDebugEnabled()) {
			log.debug("connection: " + connection.getURL());
		}
	
//		Connection: keep-alive
//		Cache-Control: no-cache
//		Accept: */*
//		Pragma: no-cache
//		User-Agent: Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36
//		Referer: http://map.daum.net/
//		Accept-Encoding: gzip,deflate,sdch
//		Accept-Language: ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4
//		Cookie: SLOGIN=2; uvkey=Ul4qt24t1MwAAEh6voEAAADS; TIARA=8K1Nta1I8Dod1hGE3g_XHNV1ZGcmcXPFfmmNKsJr_89UOJQDrB.b354Qeod4FTLB.NFAw2yltSetmc6rFtgK_g00
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setRequestProperty("Cache-Control", "no-cache");
		connection.setRequestProperty("Accept", "*/*");
		connection.setRequestProperty("Pragma", "no-cache");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36");
		connection.setRequestProperty("Referer", "http://map.daum.net/");
		connection.setRequestProperty("Accept-Encoding", "deflate");
		connection.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
		connection.setRequestProperty("Cookie", "SLOGIN=2; uvkey=Ul4qt24t1MwAAEh6voEAAADS; TIARA=8K1Nta1I8Dod1hGE3g_XHNV1ZGcmcXPFfmmNKsJr_89UOJQDrB.b354Qeod4FTLB.NFAw2yltSetmc6rFtgK_g00");
	
		return connection;
	}

}
