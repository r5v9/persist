
// $Id$

package net.sf.persist.tests.oracle;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.SQLException;

import net.sf.persist.tests.common.TestSimple;
import net.sf.persist.tests.framework.BeanMap;
import net.sf.persist.tests.framework.BeanTest;
import net.sf.persist.tests.framework.FieldMap;

import org.junit.Test;

public class TestOracle extends TestSimple {
	
	public String getProperties() {
		return "net/sf/persist/tests/oracle/oracle.properties";
	}
	
	// several tests are inherited from net.sf.persist.tests.common.TestSimple
	
	@Test
	public void testStringTypes() throws SQLException {
		
		Class[] characterTypes = new Class[] {Character.class, char.class, String.class};
		Class[] stringTypes = new Class[] {String.class, char[].class, Character[].class};
		Class[] clobTypes = new Class[] {String.class, char[].class, Character[].class, Reader.class};
		
		// oracle doesn't support queries by clob, long or nclob
		BeanMap beanMap = new BeanMap("StringTypes")
			.addField( new FieldMap("longCol").setTypes(stringTypes).setSize(32).setSupportsQueryByValue(false) )
			.addField( new FieldMap("char1Col").setTypes(characterTypes).setSize(1) )
			.addField( new FieldMap("nchar1Col").setTypes(characterTypes).setSize(1) )
			.addField( new FieldMap("charCol").setTypes(stringTypes).setSize(255) )
			.addField( new FieldMap("ncharCol").setTypes(stringTypes).setSize(255) )
			.addField( new FieldMap("nvarchar2Col").setTypes(stringTypes).setSize(255) )
			.addField( new FieldMap("varchar2Col").setTypes(stringTypes).setSize(255) )
			.addField( new FieldMap("clobCol").setTypes(clobTypes).setSize(16384).setSupportsQueryByValue(false) )
			.addField( new FieldMap("nclobCol").setTypes(clobTypes).setSize(16384).setSupportsQueryByValue(false) );
		
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testNumericTypes() throws SQLException {
		
		Class[] longTypes = new Class[] {Long.class, long.class};
		Class[] doubleTypes = new Class[] {Double.class, double.class, BigDecimal.class};
		Class[] floatTypes = new Class[] {Float.class, float.class, Double.class, double.class, BigDecimal.class};
		
		BeanMap beanMap = new BeanMap("NumericTypes")
			.addField( new FieldMap("numberCol").setTypes(longTypes) )
			.addField( new FieldMap("binaryFloatCol").setTypes(floatTypes).setBoundaries(0, 9999) )
			.addField( new FieldMap("binaryDoubleCol").setTypes(doubleTypes).setBoundaries(0, 9999) );
		
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testDatetimeTypes() throws SQLException {
		
		BeanMap beanMap = new BeanMap("DatetimeTypes")
			.addField( new FieldMap("dateCol").setTypes(java.sql.Date.class) ) // oracle is picky about Date when used in queries
			.addField( new FieldMap("timestampCol").setTypes(java.sql.Timestamp.class, java.util.Date.class) );
		
		BeanTest.test(persist, beanMap);
	}
	
	@Test
	public void testBinaryTypes() throws SQLException {
		
		Class[] binaryTypes = new Class[] { byte[].class, Byte[].class };
		Class[] blobTypes = new Class[] { byte[].class, Byte[].class, InputStream.class };
		
		// by default, oracle can't bind more than 4000 bytes to lob/long columns in a single statement
		// oracle doesn't support queries by long_raw or blob columns
		// only blobs support InputStream types
		BeanMap beanMap = new BeanMap("BinaryTypes")
			.addField( new FieldMap("rawCol").setTypes(binaryTypes).setSize(1024) )
			.addField( new FieldMap("longRawCol").setTypes(binaryTypes).setSize(1024).setSupportsQueryByValue(false) )
			.addField( new FieldMap("blobCol").setTypes(blobTypes).setSize(1024).setSupportsQueryByValue(false) );
		
		BeanTest.test(persist, beanMap);
	}

}
