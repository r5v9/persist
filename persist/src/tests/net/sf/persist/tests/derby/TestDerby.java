
// $Id$

package net.sf.persist.tests.derby;

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


public class TestDerby extends TestSimple {
	
	public String getProperties() {
		return "net/sf/persist/tests/derby/derby.properties";
	}
	
	// several tests are inherited from TestSimple
	
	@Test
	public void testStringTypes() throws SQLException {
		
		Class[] characterTypes = new Class[] {Character.class, char.class, String.class};
		Class[] stringTypes = new Class[] {String.class, char[].class, Character[].class};
		Class[] clobTypes = new Class[] {String.class, char[].class, Character[].class, Reader.class, Clob.class};
		
		BeanMap beanMap = new BeanMap("StringTypes")
			.addField( new FieldMap("charCol").setTypes(characterTypes).setSize(1) )
			.addField( new FieldMap("varcharCol").setTypes(stringTypes).setSize(255) )
			.addField( new FieldMap("longVarcharCol").setTypes(stringTypes).setSize(16384).setSupportsQueryByValue(false) )
			.addField( new FieldMap("clobCol").setTypes(clobTypes).setSize(16384).setSupportsQueryByValue(false));
		
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testNumericTypes() throws SQLException {
		
		Class[] integerTypes = new Class[] {Integer.class, int.class};
		Class[] shortTypes = new Class[] {Short.class, short.class};
		Class[] longTypes = new Class[] {Long.class, long.class};
		Class[] doubleTypes = new Class[] {Double.class, double.class, BigDecimal.class};
		Class[] floatTypes = new Class[] {Float.class, float.class, Double.class, double.class, BigDecimal.class};
		
		BeanMap beanMap = new BeanMap("NumericTypes")
			.addField( new FieldMap("smallintCol").setTypes(shortTypes) )
			.addField( new FieldMap("integerCol").setTypes(integerTypes) )
			.addField( new FieldMap("bigintCol").setTypes(longTypes) )
			.addField( new FieldMap("realCol").setTypes(floatTypes).setBoundaries(0,9999) )
			.addField( new FieldMap("doublePrecisionCol").setTypes(doubleTypes).setBoundaries(0,9999).setSupportsQueryByValue(false) )
			.addField( new FieldMap("floatCol").setTypes(floatTypes).setBoundaries(0,9999) )
			.addField( new FieldMap("decimalCol").setTypes(integerTypes).setBoundaries(0, 9999) )
			.addField( new FieldMap("numericCol").setTypes(integerTypes).setBoundaries(0, 9999) );
		
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testDatetimeTypes() throws SQLException {
		
		BeanMap beanMap = new BeanMap("DatetimeTypes")
			.addField( new FieldMap("dateCol").setTypes(java.sql.Date.class, java.util.Date.class) )
			.addField( new FieldMap("timeCol").setTypes(java.sql.Time.class, java.util.Date.class) )	
			.addField( new FieldMap("timestampCol").setTypes(java.sql.Timestamp.class, java.util.Date.class) );
	
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testBinaryTypes() throws SQLException {
		
		Class[] byteTypes = new Class[] { byte[].class, Byte[].class };
		Class[] binaryTypes = new Class[] { byte[].class, Byte[].class, InputStream.class };
		Class[] blobTypes = new Class[] { byte[].class, Byte[].class, InputStream.class, Blob.class };
		
		BeanMap beanMap = new BeanMap("BinaryTypes")
			.addField( new FieldMap("charBitCol").setTypes(byteTypes).setSize(1) )
			.addField( new FieldMap("varcharBitCol").setTypes(binaryTypes).setSize(255) )
			.addField( new FieldMap("longVarcharBitCol").setTypes(binaryTypes).setSize(16384).setSupportsQueryByValue(false))
			.addField( new FieldMap("blobCol").setTypes(blobTypes).setSize(16384).setSupportsQueryByValue(false) );
		
		BeanTest.test(persist, beanMap);
	}

}
