package kr.okplace.job.common.webcrawl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * @author Superman
 *
 * @param <T>
 */
public class PatternMatchBeanExtractor<T> extends PatternMatcherWrapper implements BeanExtractor<T> {
	
	private Class<? extends T> targetType;
	private String[] properties;
	
	private List<PropertyDescriptor> targetPropertyDescriptors;
	
	/**
	 * @param targetType
	 */
	public void setTargetType(Class<? extends T> targetType) {
		this.targetType = targetType;
	}

	/**
	 * @param properties
	 */
	public void setProperties(String properties) {
		Assert.hasLength(properties, "property[properties] must not be empty!");
		this.properties = properties.split("\\s*,\\s*");
	}

	/* (non-Javadoc)
	 * @see kr.okplace.job.common.webcrawl.BeanExtractor#getNext()
	 */
	@Override
	public T getNext() throws InstantiationException, IllegalAccessException, InvocationTargetException {
		
		if(targetPropertyDescriptors==null) {
			initTargetPropertyDescriptors();
		}

		Map<String, String> matchingGroups = new HashMap<String, String>();
		for(int i=0; i<properties.length; i++) {
			matchingGroups.put(properties[i], getMatchingGroup(i+1));
		}

		return getBean(matchingGroups);
	}

	/**
	 * @param content
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public List<T> getBeanList(String content) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		
		setInput(content);
		
		List<T> beans = new ArrayList<T>();
		while(hasNext()) {
			beans.add(getNext());
		}
		
		return beans;
	}

	/**
	 * @param matchingGroups
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private T getBean(Map<String, String> matchingGroups) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		
		T bean = targetType.newInstance();
		for (PropertyDescriptor descriptor : targetPropertyDescriptors) {
			Object value = matchingGroups.get(descriptor.getName());
			if (value != null) {
				Method writeMethod = descriptor.getWriteMethod();
				if (writeMethod != null) {
					writeMethod.invoke(bean, new Object[] { value });
				}
			}
		}
		
		return bean;
	}

	/**
	 * 
	 */
	private void initTargetPropertyDescriptors() {

		this.targetPropertyDescriptors = new ArrayList<PropertyDescriptor>();
		PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(targetType);
		for(PropertyDescriptor descriptor : descriptors) {
			for(String property: properties) {
				if(descriptor.getName().equals(property))	{
					this.targetPropertyDescriptors.add(descriptor);
					break;
				}
			}
		}
	}
}
