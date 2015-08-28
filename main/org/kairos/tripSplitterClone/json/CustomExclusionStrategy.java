package org.kairos.tripSplitterClone.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.util.Arrays;
import java.util.List;

/**
 * Custom Exclusion Strategy. Used mainly to avoid circular references.
 *
 * Created on 8/22/15 by
 *
 * @author AxelCollardBovy.
 * 
 */
public class CustomExclusionStrategy implements ExclusionStrategy {

	/**
	 * The class for which this strategy applies to.
	 */
	private final Class<?> clazz;

	/**
	 * Whole class exclusion flag.
	 */
	private final Boolean excludeClass;

	/**
	 * Excluded field.
	 */
	private final List<String> excludedFields;

	/**
	 * Constructor with fields.
	 * 
	 * @param clazz
	 * @param excludeClass
	 * @param excludedFields
	 */
	public CustomExclusionStrategy(Class<?> clazz, Boolean excludeClass,
			String... excludedFields) {
		super();
		this.clazz = clazz;
		this.excludeClass = excludeClass;
		this.excludedFields = Arrays.asList(excludedFields);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gson.ExclusionStrategy#shouldSkipClass(java.lang.Class)
	 */
	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return this.excludeClass && clazz.equals(this.clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gson.ExclusionStrategy#shouldSkipField(com.google.gson.
	 * FieldAttributes)
	 */
	@Override
	public boolean shouldSkipField(FieldAttributes field) {
		return this.clazz.equals(field.getDeclaringClass())
				&& this.excludedFields.contains(field.getName());
	}

}
