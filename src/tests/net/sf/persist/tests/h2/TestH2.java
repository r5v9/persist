
// $Id$

package net.sf.persist.tests.h2;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import net.sf.persist.tests.common.TestSimple;
import net.sf.persist.tests.framework.BeanMap;
import net.sf.persist.tests.framework.BeanTest;
import net.sf.persist.tests.framework.FieldMap;

import org.junit.Test;


public class TestH2 extends TestSimple {
	
	public String getProperties() {
		return "net/sf/persist/tests/h2/h2.properties";
	}
	
	// several tests are inherited from net.sf.persist.tests.common.TestSimple
	
	@Test
	public void testStringTypes() throws SQLException {
		
		Class[] stringTypes = new Class[] { String.class, char[].class, Character[].class };
		Class[] clobTypes = new Class[] { String.class, char[].class, Character[].class, Reader.class, Clob.class };
		
		// uuid not being tested
		BeanMap beanMap = new BeanMap("StringTypes")
			.addField( new FieldMap("charCol").setTypes(stringTypes).setSize(255) )
			.addField( new FieldMap("varcharCol").setTypes(stringTypes).setSize(255) )
			.addField( new FieldMap("varcharIgnorecaseCol").setTypes(stringTypes).setSize(255) )
			.addField( new FieldMap("clobCol").setTypes(clobTypes).setSize(8192) );
		
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testNumericTypes() throws SQLException {
		
		Class[] integerTypes = new Class[] {Integer.class, int.class};
		Class[] booleanTypes = new Class[] {Boolean.class, boolean.class};
		Class[] byteTypes = new Class[] {Byte.class, byte.class};
		Class[] shortTypes = new Class[] {Short.class, short.class};
		Class[] longTypes = new Class[] {Long.class, long.class};
		Class[] doubleTypes = new Class[] {Double.class, double.class, BigDecimal.class};
		Class[] floatTypes = new Class[] {Float.class, float.class, Double.class, double.class, BigDecimal.class};
		
		BeanMap beanMap = new BeanMap("NumericTypes")
			.addField( new FieldMap("intCol").setTypes(integerTypes) )
			.addField( new FieldMap("booleanCol").setTypes(booleanTypes) )
			.addField( new FieldMap("tinyintCol").setTypes(byteTypes) )
			.addField( new FieldMap("smallintCol").setTypes(shortTypes) )
			.addField( new FieldMap("bigintCol").setTypes(longTypes) )
			.addField( new FieldMap("decimalCol").setTypes(longTypes) )
			.addField( new FieldMap("doubleCol").setTypes(doubleTypes).setBoundaries(0,9999) )
			.addField( new FieldMap("realCol").setTypes(floatTypes).setBoundaries(0,9999) );
		
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testDatetimeTypes() throws SQLException {
		
		BeanMap beanMap = new BeanMap("DatetimeTypes")
			.addField( new FieldMap("timeCol").setTypes(java.sql.Time.class) )
			.addField( new FieldMap("dateCol").setTypes(java.sql.Date.class) )
			.addField( new FieldMap("timestampCol").setTypes(java.sql.Timestamp.class, java.util.Date.class) );
		
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testBinaryTypes() throws SQLException {
		
		Class[] binaryTypes = new Class[] { byte[].class, Byte[].class, InputStream.class, Blob.class };
		Class[] otherTypes = new Class[] { Object.class };
		
		BeanMap beanMap = new BeanMap("BinaryTypes")
			.addField( new FieldMap("binaryCol").setTypes(binaryTypes).setSize(255) )
			.addField( new FieldMap("blobCol").setTypes(binaryTypes).setSize(255) )
			.addField( new FieldMap("otherCol").setTypes(otherTypes).setSize(255) );
		
		BeanTest.test(persist, beanMap);
	}

}