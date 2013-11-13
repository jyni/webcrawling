package kr.okplace.job.support;

import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.Assert;

public class Jaxb2MarshallerStaxEventItemReader<T> extends StaxEventItemReader<T> implements InitializingBean {

	private Class<?> classToBeBound;

	public Class<?> getClassToBeBound() {
		return classToBeBound;
	}

	public void setClassToBeBound(Class<?> classToBeBound) {
		this.classToBeBound = classToBeBound;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		Assert.notNull(classToBeBound, "Property[classToBeBound] required!");

		Jaxb2Marshaller unMarshaller = new Jaxb2Marshaller();
		unMarshaller.setClassesToBeBound(classToBeBound);
		super.setUnmarshaller(unMarshaller);
		
		super.afterPropertiesSet();
	}
}
