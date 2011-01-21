
// $Id$

package net.sf.persist.tests.mysql;

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


public class TestMysql extends TestSimple {
	
	public String getProperties() {
		return "net/sf/persist/tests/mysql/mysql.properties";
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
			.addField( new FieldMap("tinytextCol").setTypes(clobTypes).setSize(255) )
			.addField( new FieldMap("mediumtextCol").setTypes(clobTypes).setSize(1024) )
			.addField( new FieldMap("longtextCol").setTypes(clobTypes).setSize(16384) )
			.addField( new FieldMap("textCol").setTypes(clobTypes).setSize(16384) )
			.addField( new FieldMap("enumCol").setTypes(characterTypes).setSize(1) )
			.addField( new FieldMap("setCol").setTypes(characterTypes).setSize(1) );
		
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
			.addField( new FieldMap("booleanCol").setTypes(booleanTypes) )
			.addField( new FieldMap("tinyintCol").setTypes(byteTypes) )
			.addField( new FieldMap("smallintCol").setTypes(shortTypes) )
			.addField( new FieldMap("mediumintCol").setTypes(shortTypes) )
			.addField( new FieldMap("intCol").setTypes(integerTypes) )
			.addField( new FieldMap("bigintCol").setTypes(longTypes) )
			.addField( new FieldMap("floatCol").setTypes(floatTypes).setBoundaries(0,9999) )
			.addField( new FieldMap("doubleCol").setTypes(doubleTypes).setBoundaries(0,9999) )
			.addField( new FieldMap("decimalCol").setTypes(integerTypes) );
		
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testDatetimeTypes() throws SQLException {
		
		// not testing timestamp here, it doesn't support null values
		BeanMap beanMap = new BeanMap("DatetimeTypes")
			.addField( new FieldMap("dateCol").setTypes(java.sql.Date.class) )
			.addField( new FieldMap("datetimeCol").setTypes(java.sql.Timestamp.class, java.util.Date.class) )
			.addField( new FieldMap("timeCol").setTypes(java.sql.Time.class, java.util.Date.class) )
			.addField( new FieldMap("year2Col").setTypes(Short.class, short.class).setBoundaries(01, 99).setSupportsCompareMapValue(false) )
			.addField( new FieldMap("year4Col").setTypes(Short.class, short.class).setBoundaries(1901, 1999).setSupportsCompareMapValue(false) );
		
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testBinaryTypes() throws SQLException {
		
		Class[] binaryTypes = new Class[] { byte[].class, Byte[].class, InputStream.class, Blob.class };
		
		BeanMap beanMap = new BeanMap("BinaryTypes")
			.addField( new FieldMap("binaryCol").setTypes(binaryTypes).setSize(255) )
			.addField( new FieldMap("varbinaryCol").setTypes(binaryTypes).setSize(255) )
			.addField( new FieldMap("tinyblobCol").setTypes(binaryTypes).setSize(255) )
			.addField( new FieldMap("blobCol").setTypes(binaryTypes).setSize(255) )
			.addField( new FieldMap("mediumblobCol").setTypes(binaryTypes).setSize(255) )
			.addField( new FieldMap("longblobCol").setTypes(binaryTypes).setSize(16384) );
		
		BeanTest.test(persist, beanMap);
	}
	

}
