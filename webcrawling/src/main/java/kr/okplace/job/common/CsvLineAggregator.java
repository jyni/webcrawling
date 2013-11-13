package kr.okplace.job.common;

import org.springframework.batch.item.file.transform.ExtractorLineAggregator;

public class CsvLineAggregator<T> extends ExtractorLineAggregator<T> {

	private static final String DELIMITER = ",";
	private static final String DOUBLE_QUOTE = "\"";
	private static final String QUOTE_ESCAPE = "\\\"";
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.transform.ExtractorLineAggregator#doAggregate(java.lang.Object[])
	 */
	@Override
	protected String doAggregate(Object[] fields) {

		if(fields==null) {
			return null;
		}

		if(fields.length==0) {
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		String s;
		for(Object o: fields) {
			
			sb.append(DELIMITER);
			
			s = o.toString().trim();
			if(s.contains(DOUBLE_QUOTE)) {
				sb.append('"').append(escape(s)).append('"');
			}
			else if(s.contains(DELIMITER) || s.contains("\r") || s.contains("\n")) {
				sb.append('"').append(s).append('"');
			}
			else {
				sb.append(s);
			}
		}

		return sb.substring(1);
	}

	private String escape(String s) {
		return s.replaceAll(DOUBLE_QUOTE, QUOTE_ESCAPE);
	}
}
