
// $Id$

package net.sf.persist.tests.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds data related to a field from a bean, including metadata to test it
 */
public class FieldMap {
	
	private String fieldName;
	
	/**
	 * Some databases don't allow for queries using fields of some types.
	 * For instance, Oracle does not allow queries on blobs
	 */
	private boolean supportsQueryByValue = true;
	
	/**
	 * Some types, such as MySQL year2 and year4 are handled using short's,
	 * but when read as a map they are returned as java.sql.Date objects. It's
	 * quite hard to perform automatic conversions among these types, therefore
	 * it's advisable to turn map comparison off for them and use manual testing.
	 */
	private boolean supportsCompareMapValue = true;

	/**
	 * Used for string types
	 */
	private int size=250;
	
	/**
	 * Used for numeric types
	 */
	private double min=-1;
	private double max=-1;
	
	private List<Class> types = new ArrayList();
	
	public FieldMap(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFieldName() { return fieldName; }
	public FieldMap setFieldName(String fieldName) { this.fieldName = fieldName; return this; }
	
	public List<Class> getTypes() { return types; }
	public FieldMap setTypes(Class...types) { 
		this.types = new ArrayList();
		for (Class type : types) this.types.add(type);
		return this;
	}

	public int getSize() { return size; }
	public FieldMap setSize(int size) { this.size = size; return this; }
	
	public boolean isSupportsQueryByValue() { return supportsQueryByValue; }
	public FieldMap setSupportsQueryByValue(boolean supportsQueryByValue) { 
		this.supportsQueryByValue = supportsQueryByValue; 
		return this;
	}

	public double getMin() { return min; }
	public double getMax() { return max; }
	public FieldMap setBoundaries(double min, double max) {
		this.min = min;
		this.max = max;
		return this;
	}

	public boolean isSupportsCompareMapValue() { return supportsCompareMapValue; }
	public FieldMap setSupportsCompareMapValue(boolean supportsCompareMapValue) {
		this.supportsCompareMapValue = supportsCompareMapValue;
		return this;
	}

}
