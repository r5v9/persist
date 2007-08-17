
// $Id$

package net.sf.persist.tests.sqlserver;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.SQLException;

import net.sf.persist.tests.common.TestSimple;
import net.sf.persist.tests.framework.BeanMap;
import net.sf.persist.tests.framework.BeanTest;
import net.sf.persist.tests.framework.FieldMap;

import org.junit.Test;


public class TestSqlserver extends TestSimple {
	
	public String getProperties() {
		return "net/sf/persist/tests/sqlserver/sqlserver.properties";
	}
	
	// several tests are inherited from TestSimple
	
	@Test
	public void testStringTypes() throws SQLException {
		
		Class[] characterTypes = new Class[] {Character.class, char.class, String.class};
		Class[] stringTypes = new Class[] {String.class, char[].class, Character[].class};
		Class[] clobTypes = new Class[] {String.class, char[].class, Character[].class, Reader.class};
		
		BeanMap beanMap = new BeanMap("StringTypes")
			.addField( new FieldMap("charCol").setTypes(characterTypes).setSize(1) )
			.addField( new FieldMap("varcharCol").setTypes(stringTypes).setSize(255) )
			.addField( new FieldMap("textCol").setTypes(clobTypes).setSize(4096).setSupportsQueryByValue(false) )
			.addField( new FieldMap("ncharCol").setTypes(characterTypes).setSize(1) )
			.addField( new FieldMap("nvarcharCol").setTypes(stringTypes).setSize(255) )
			.addField( new FieldMap("ntextCol").setTypes(clobTypes).setSize(4096).setSupportsQueryByValue(false) );		

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
			.addField( new FieldMap("bitCol").setTypes(booleanTypes) )
			.addField( new FieldMap("tinyintCol").setTypes(byteTypes) )
			.addField( new FieldMap("smallintCol").setTypes(shortTypes) )
			.addField( new FieldMap("intCol").setTypes(integerTypes) )
			.addField( new FieldMap("bigintCol").setTypes(longTypes) )
			.addField( new FieldMap("smallmoneyCol").setTypes(doubleTypes).setBoundaries(0,9999) )
			.addField( new FieldMap("moneyCol").setTypes(doubleTypes).setBoundaries(0,9999) )
			.addField( new FieldMap("decimalCol").setTypes(integerTypes) )
			.addField( new FieldMap("numericCol").setTypes(integerTypes) )
			.addField( new FieldMap("floatCol").setTypes(floatTypes).setBoundaries(0,9999) )
			.addField( new FieldMap("realCol").setTypes(floatTypes).setBoundaries(0,9999) );
			
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testDatetimeTypes() throws SQLException {
		
		// smalldatetime is problematic to query by value because of the 1-minute rounding;
		// the bean has the current timestamp therefore it won't work properly
		BeanMap beanMap = new BeanMap("DatetimeTypes")
			.addField( new FieldMap("datetimeCol").setTypes(java.sql.Timestamp.class, java.util.Date.class) )
			.addField( new FieldMap("smalldatetimeCol").setTypes(java.sql.Timestamp.class, java.util.Date.class).setSupportsQueryByValue(false) );
		
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testBinaryTypes() throws SQLException {
		
		Class[] binaryTypes = new Class[] { byte[].class, InputStream.class };
		
		BeanMap beanMap = new BeanMap("BinaryTypes")
			.addField( new FieldMap("binaryCol").setTypes(binaryTypes).setSize(255) )
			.addField( new FieldMap("varbinaryCol").setTypes(binaryTypes).setSize(255) )
			.addField( new FieldMap("imageCol").setTypes(binaryTypes).setSize(16384).setSupportsQueryByValue(false) );
		
		BeanTest.test(persist, beanMap);
	}
	

}
