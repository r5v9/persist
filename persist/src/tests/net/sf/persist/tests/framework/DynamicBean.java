
// $Id$

package net.sf.persist.tests.framework;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

/**
 * Set of helpers to build and manipulate beans at runtime.
 */
public class DynamicBean {
	
	public static Class createBeanClass(BeanMap beanMap, boolean noTable) {

		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass("net.sf.persist.tests.generated." 
				+ beanMap.getClassName() + (noTable ? "NoTable" : ""));

		if (noTable) {	
			ClassFile cf = cc.getClassFile();
			ConstPool cp = cf.getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
			Annotation a;
			try {
				a = new Annotation(cp, pool.get("net.sf.persist.annotations.NoTable"));
			} catch (NotFoundException e) {
				throw new RuntimeException(e);
			}
			attr.setAnnotation(a);
			cf.addAttribute(attr);
			cf.setVersionToJava5();
		}
		
		try {

			for (FieldMap fieldMap : beanMap.getFields()) {

				String fieldName = fieldMap.getFieldName();
				Class fieldType = fieldMap.getTypes().get(0);

				String fieldNameU = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				String fieldTypeName = fieldType.getCanonicalName();

				String getterCode = "public " + fieldTypeName + " get" + fieldNameU + "() { return " + fieldName + "; }";
				String setterCode = "public void set" + fieldNameU + "(" + fieldTypeName + " " + fieldName + ") { this." + fieldName + "=" + fieldName + "; }";

				CtField cf = new CtField(pool.get(fieldTypeName), fieldName, cc);
				cc.addField(cf);

				CtMethod cm = CtNewMethod.make(getterCode, cc);
				cc.addMethod(cm);

				cm = CtNewMethod.make(setterCode, cc);
				cc.addMethod(cm);
			}

			String toStringCode = "public String toString() { return net.sf.persist.tests.framework.DynamicBean.toString(this); }";
			CtMethod cm = CtNewMethod.make(toStringCode, cc);
			cc.addMethod(cm);

			String equalsCode = "public boolean equals(Object obj) { return net.sf.persist.tests.framework.DynamicBean.compareBeans(this,obj); }";
			cm = CtNewMethod.make(equalsCode, cc);
			cc.addMethod(cm);

			return cc.toClass();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object createInstance(Class cls, BeanMap beanMap, boolean useNulls) {
		Object obj = null;
		try {
			obj = cls.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (Field field : cls.getDeclaredFields()) {
			FieldMap fieldMap = beanMap.getField(field.getName());
			setRandomValue(obj, field, fieldMap, useNulls);
		}
		return obj;
	}

	private static void setRandomValue(Object obj, Field field, FieldMap fieldMap, boolean useNull) {

		Class fieldType = field.getType();
		int size = fieldMap.getSize();
		double min = fieldMap.getMin();
		double max = fieldMap.getMax();

		Object value = null;

		if (fieldType == Boolean.class)
			value = useNull ? null : new Boolean(randomBoolean());
		else if (fieldType == boolean.class)
			value = useNull ? false : randomBoolean();
		else if (fieldType == Byte.class)
			value = useNull ? null : new Byte(randomByte((byte)min, (byte)max));
		else if (fieldType == byte.class)
			value = useNull ? (byte) 0 : randomByte((byte)min, (byte)max);
		else if (fieldType == Byte[].class)
			value = useNull ? null : randomByteObjArray(size);
		else if (fieldType == byte[].class)
			value = useNull ? null : randomByteArray(size);
		else if (fieldType == Short.class)
			value = useNull ? null : new Short(randomShort((short)min, (short)max));
		else if (fieldType == short.class)
			value = useNull ? (short) 0 : randomShort((short)min, (short)max);
		else if (fieldType == Integer.class)
			value = useNull ? null : new Integer(randomInt((int)min, (int)max));
		else if (fieldType == int.class)
			value = useNull ? (int) 0 : randomInt((int)min, (int)max);
		else if (fieldType == Long.class)
			value = useNull ? null : new Long(randomLong((long)min, (long)max));
		else if (fieldType == long.class)
			value = useNull ? (long) 0 : randomLong((long)min, (long)max);
		else if (fieldType == Float.class)
			value = useNull ? null : new Float(randomFloat((float)min, (float)max));
		else if (fieldType == float.class)
			value = useNull ? (float) 0 : randomFloat((float)min, (float)max);
		else if (fieldType == Double.class)
			value = useNull ? null : new Double(randomDouble(min, max));
		else if (fieldType == double.class)
			value = useNull ? (double) 0 : randomDouble(min, max);
		else if (fieldType == Character.class)
			value = useNull ? null : new Character(randomChar());
		else if (fieldType == char.class)
			value = useNull ? ' ' : randomChar();
		else if (fieldType == Character[].class)
			value = useNull ? null : randomCharObjArray(size);
		else if (fieldType == char[].class)
			value = useNull ? null : randomCharArray(size);
		else if (fieldType == String.class)
			value = useNull ? null : randomString(size);
		else if (fieldType == BigDecimal.class)
			value = useNull ? null : new BigDecimal(randomLong((long)min, (long)max));
		else if (fieldType == java.io.Reader.class)
			value = useNull ? null : new StringReader(randomString(size));
		else if (fieldType == java.io.InputStream.class)
			value = useNull ? null : new ByteArrayInputStream(randomByteArray(size));
		else if (fieldType == java.util.Date.class)
			value = useNull ? null : new java.util.Date(randomTimestamp());
		else if (fieldType == java.sql.Date.class)
			value = useNull ? null : new java.sql.Date(randomTimestamp());
		else if (fieldType == java.sql.Time.class)
			value = useNull ? null : new java.sql.Time(randomTimestamp());
		else if (fieldType == java.sql.Timestamp.class)
			value = useNull ? null : new java.sql.Timestamp(randomTimestamp());
		else if (fieldType == java.sql.Blob.class)
			value = useNull ? null : new BytesBlob(randomByteArray(size));
		else if (fieldType == java.sql.Clob.class)
			value = useNull ? null : new StringClob(randomString(size));
		else {
			if (useNull)
				value = null;
			else {
				Map m = new HashMap();
				m.put(randomString(3), randomString(32));
				m.put(randomString(3), randomString(32));
				m.put(randomString(3), randomString(32));
				value = m;
			}
		}

		try {
			String fieldName = field.getName();
			Field f = obj.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(obj, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static Object getFieldValue(Object obj, String fieldName) {
		try {
			Field f = obj.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			return f.get(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String toString(Object obj) {
		if (obj == null)
			return "null";
		StringBuffer sb = new StringBuffer();
		sb.append("{ ");
		for (Field field : obj.getClass().getDeclaredFields()) {
			String fieldName = field.getName();

			Object value = getFieldValue(obj, fieldName);
			String s = value == null ? "null" : value.toString();
			if (s.length() > 32)
				s = s.substring(0, 32) + "...";

			sb.append(fieldName + "=" + s + ", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" }");
		return sb.toString();
	}
	
	/**
	 * Returns true if the field is a primitive number type (byte, short, int, etc.) and its value is zero,
	 * or if the field is an object and its value is null
	 */
	public static boolean isNull(Class cls, Object obj) {
		
		if (obj==null) return true;
		
		if (cls==boolean.class || cls==Boolean.class)
			return ((Boolean)obj).booleanValue()==false;
		else if (cls==byte.class || cls==Byte.class || cls==short.class || cls==Short.class 
				|| cls==int.class || cls==Integer.class || cls==long.class || cls==Long.class 
				|| cls==float.class || cls==Float.class  || cls==double.class || cls==Double.class 
				|| cls==BigDecimal.class) {
			
			// first cast to Number
			Number n = (Number) obj;
			return n.longValue()==0;
		}
		else 
			return false;
	}

	public static boolean compareBeans(Object o1, Object o2) {

		if (o1 == null && o2 == null)
			return true;
		if (o1 == o2)
			return true;
		if (o1 == null && o2 != null)
			return false;
		if (o1 != null && o2 == null)
			return false;
		if (o1.getClass() != o2.getClass())
			return false;

		try {

			for (Field field : o1.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object v1 = field.get(o1);
				Object v2 = field.get(o2);
				if (!compareValues(v1, v2))
					return false;
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return true;
	}
	
	
	public static boolean compareBeansFromDifferentClasses(Object o1, Object o2) {

		if (o1 == null && o2 == null)
			return true;
		if (o1 == o2)
			return true;
		if (o1 == null && o2 != null)
			return false;
		if (o1 != null && o2 == null)
			return false;

		try {

			for (Field f1 : o1.getClass().getDeclaredFields()) {
				f1.setAccessible(true);
				Object v1 = f1.get(o1);
				
				Field f2;
				try {
					f2 = o2.getClass().getDeclaredField(f1.getName());
				} catch (NoSuchFieldException e) {
					return false;
				}
				f2.setAccessible(true);
				Object v2 = f2.get(o2);
				
				if (!compareValues(v1, v2))
					return false;
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return true;
	}

	
	

	/**
	 * Compare values trying to convert types if they are found to be compatible
	 */
	public static boolean compareValues(Object v1, Object v2) {

		if (v1 == null && v2 == null)
			return true;
		if (v1 == v2)
			return true;
		if (v1 == null && v2 != null)
			return false;
		if (v1 != null && v2 == null)
			return false;

		// try to convert v2 into v1 type
		v2 = convertToType(v1.getClass(), v2);

		if (v1.getClass() != v2.getClass())
			return false;

		Class type = v1.getClass();

		try {

			if (type == Boolean.class || type == boolean.class) {
				if (!((Boolean) v1).equals((Boolean) v2))
					return false;
			} else if (type == Byte.class || type == byte.class) {
				if (!((Byte) v1).equals((Byte) v2))
					return false;
			} else if (type == Byte[].class) {
				if (!Arrays.equals((Byte[]) v1, (Byte[]) v2))
					return false;
			} else if (type == byte[].class) {
				if (!Arrays.equals((byte[]) v1, (byte[]) v2))
					return false;
			} else if (type == Short.class || type == short.class) {
				if (!((Short) v1).equals((Short) v2))
					return false;
			} else if (type == Integer.class || type == int.class) {
				if (!((Integer) v1).equals((Integer) v2))
					return false;
			} else if (type == Long.class || type == long.class) {
				if (!((Long) v1).equals((Long) v2))
					return false;
			} else if (type == Float.class || type == float.class) {
				Float v1f = (Float) v1;
				Float v2f = (Float) v2;
				if (Float.floatToIntBits(v1f) != Float.floatToIntBits(v2f))
					return false;
			} else if (type == Double.class || type == double.class) {
				Double v1d = (Double) v1;
				Double v2d = (Double) v2;
				if (Double.doubleToLongBits(v1d) != Double.doubleToLongBits(v2d))
					return false;
			} else if (type == Character.class || type == char.class) {
				if (!((Character) v1).equals((Character) v2))
					return false;
			} else if (type == Character[].class) {
				if (!Arrays.equals((Character[]) v1, (Character[]) v2))
					return false;
			} else if (type == char[].class) {
				if (!Arrays.equals((char[]) v1, (char[]) v2))
					return false;
			} else if (type == String.class) {
				if (!((String) v1).equals((String) v2))
					return false;
			} else if (type == BigDecimal.class) {
				if (!((BigDecimal) v1).equals((BigDecimal) v2))
					return false;
			} else if (type == Reader.class) {
				Reader r1 = (Reader) v1;
				Reader r2 = (Reader) v2;
				if (!compareReaders(r1,r2))
					return false;
			} else if (v1  == InputStream.class) {
				InputStream i1 = (InputStream) v1;
				InputStream i2 = (InputStream) v2;
				if (!compareInputStreams(i1,i2))
					return false;
			} else if (v1 instanceof Clob) {
				Clob c1 = (Clob) v1;
				Clob c2 = (Clob) v2;
				if (!compareReaders(c1.getCharacterStream(), c2.getCharacterStream()))
					return false;
			} else if (v1 instanceof Blob) {
				Blob b1 = (Blob) v1;
				Blob b2 = (Blob) v2;
				if (!compareInputStreams(b1.getBinaryStream(), b2.getBinaryStream()))
					return false;
			} else if (type == java.util.Date.class) {
				java.util.Date d1 = (java.util.Date) v1;
				java.util.Date d2 = (java.util.Date) v2;
				if (!d1.toString().substring(0,19).equals(d2.toString().substring(0,19)))
					return false;
			} else if (type == java.sql.Date.class) {
				java.sql.Date d1 = (java.sql.Date) v1;
				java.sql.Date d2 = (java.sql.Date) v2;
				if (!d1.toString().equals(d2.toString()))
					return false;
			} else if (type == java.sql.Time.class) {
				java.sql.Time d1 = (java.sql.Time) v1;
				java.sql.Time d2 = (java.sql.Time) v2;
				if (!d1.toString().equals(d2.toString()))
					return false;
			} else if (type == java.sql.Timestamp.class) {
				java.sql.Timestamp d1 = (java.sql.Timestamp) v1;
				java.sql.Timestamp d2 = (java.sql.Timestamp) v2;
				// quick fix for smalldatetimes is to compare up to 15, instead of 19
				if (!d1.toString().substring(0,15).equals(d2.toString().substring(0,15)))
					return false;
			} else if (!v1.equals(v2))
				return false;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return true;
	}
	
	/**
	 * Try to convert the provided object to the provided class. 
	 * The following groups of types allow for conversion among their types:
	 * { String, char, Character, char[], Character[], Reader }
	 * { byte[], Byte[], InputStream }
	 * { boolean, Boolean }
	 * { byte, Byte, short, Short, int, Integer, long, Long, float, Float, double, Double, BigDecimal }
	 * { java.sql.Timestamp, java.sql.Date, java.sql.Time, java.util.Date }
	 */
	public static Object convertToType(Class cls, Object value) {
		
		if (cls == value.getClass()) return value;
		
		// if cls implements Clob or Blob, upcast
		for (Class iface : cls.getInterfaces()) {
			if (iface==Clob.class) {
				cls = Clob.class;
				break;
			}
			else if (iface==Blob.class) {
				cls = Blob.class;
				break;
			}
		}
		
		Class clsValue = value.getClass();

		if (cls==String.class || cls==char.class || cls==Character.class 
				|| cls==char[].class || cls==Character[].class || cls==Reader.class || cls==Clob.class) {
			
			// first convert it to string
			
			String s = null;
			
			if (clsValue==String.class) {
				s = (String) value;
			}
			else if (clsValue==char.class || clsValue==Character.class) {
				s = "" + value;
			}
			else if (clsValue==char[].class) {
				s = String.copyValueOf((char[])value);
			}
			else if (clsValue==Character[].class) { 
				Character[] a = (Character[])value; 
				char[] ac = new char[a.length];
				for (int i=0; i<a.length; i++) ac[i] = a[i];
				s = String.copyValueOf(ac);
			}
			else if (value instanceof Reader) {
				Reader r2 = (Reader)value;
				s = readReader(r2);
			}
			else if (value instanceof Clob) {
				Clob c2 = (Clob)value;
				try {
					s = readReader(c2.getCharacterStream());
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
			else {
				return value;
			}
			
			// now convert it to the target type
			
			if (cls==String.class) {
				return s;
			}
			else if (cls==char.class) {
				if (s.length()==1) return s.charAt(0);
				else return value;
			}
			else if (cls==Character.class) {
				if (s.length()==1) return new Character(s.charAt(0));
				else return value;
			}
			else if (cls==char[].class) {
				return s.toCharArray();
			}
			else if (cls==Character[].class) {
				Character[] ret = new Character[s.length()];
				for (int i=0; i<s.length(); i++) ret[i] = new Character(s.charAt(i));
				return ret;
			}
			else if (cls==Reader.class) {
				return new StringReader(s);
			}
			else if (cls==Clob.class) {
				return new StringClob(s);
			}
		}
		
		else if (cls==byte[].class || cls==Byte[].class || cls==InputStream.class || cls==Blob.class) {
			
			// first convert to byte[]
			
			byte[] a = null;
			if (clsValue==byte[].class) {
				a = (byte[]) value;
			}
			else if (clsValue==Byte[].class) {
				Byte[] ba = (Byte[]) value;
				a = new byte[ba.length];
				for (int i=0; i<ba.length; i++) a[i]=ba[i];
			}
			else if (value instanceof InputStream) {
				InputStream is = (InputStream) value;
				a = readInputStream(is);
			}
			else if (value instanceof Blob) {
				Blob b = (Blob) value;
				try {
					a = readInputStream(b.getBinaryStream());
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
			else return value;
			
			// now convert to target class
			
			if (cls==byte[].class) {
				return a;
			}
			else if (cls==Byte[].class) {
				Byte[] ba = new Byte[a.length];
				for (int i=0; i<a.length; i++) ba[i]=a[i];
				return ba;
			}
			else if (cls==InputStream.class) {
				return new ByteArrayInputStream(a);
			}
			else if (cls==Blob.class) {
				return new BytesBlob(a);
			}
		}
		
		else if (clsValue==java.util.Date.class) {
			java.util.Date d = (java.util.Date)value;
			if (cls==java.sql.Date.class) {
				return new java.sql.Date(d.getTime());
			}
			if (cls==java.sql.Time.class) {
				return new java.sql.Time(d.getTime());
			}
			if (cls==java.sql.Timestamp.class) {
				return new java.sql.Timestamp(d.getTime());
			}
			else return value;
		}
		
		else if (cls==java.util.Date.class) {
			if (clsValue==java.sql.Date.class) {
				return new java.util.Date(((java.sql.Date)value).getTime());
			}
			if (clsValue==java.sql.Time.class) {
				return new java.util.Date(((java.util.Date)value).getTime());
			}
			if (clsValue==java.sql.Timestamp.class) {
				return new java.util.Date(((java.util.Date)value).getTime());
			}
			else return value;
		}
		
		else if (clsValue==boolean.class || clsValue==Boolean.class) {
			Boolean b = (Boolean) value;
			if (cls==boolean.class) return b.booleanValue();
			else if (cls==Boolean.class) return b;
			else return value;
		}
		
		else if (clsValue==byte.class || clsValue==Byte.class || clsValue==short.class || clsValue==Short.class 
				|| clsValue==int.class || clsValue==Integer.class || clsValue==long.class || clsValue==Long.class 
				|| clsValue==float.class || clsValue==Float.class  || clsValue==double.class || clsValue==Double.class 
				|| clsValue==BigDecimal.class) {
			
			// first cast to Number
			Number n = (Number) value;
			
			if (cls==byte.class) return n.byteValue();
			else if (cls==Byte.class) return new Byte(n.byteValue());
			else if (cls==short.class) return n.shortValue();
			else if (cls==Short.class) return new Short(n.shortValue());
			else if (cls==int.class) return n.intValue();
			else if (cls==Integer.class) return new Integer(n.intValue());
			else if (cls==long.class) return n.longValue();
			else if (cls==Long.class) return new Long(n.longValue());
			else if (cls==float.class) return n.floatValue();
			else if (cls==Float.class) return new Float(n.floatValue());
			else if (cls==double.class) return n.doubleValue();
			else if (cls==Double.class) return new Double(n.doubleValue());
			else if (cls==BigDecimal.class) return BigDecimal.valueOf(n.doubleValue());
			else return value;
		}
		
		return value;
	}
	
	public static byte[] readInputStream(InputStream is) {
		// assumes no more than 64KB of data
		try {
			byte[] buf = new byte[65535];
			int n = is.read(buf);
			if (n<0) n=0;
			byte[] ret = new byte[n];
			for (int i=0; i<n; i++) ret[i] = buf[i];
			return ret;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String readReader(Reader r) {
		char[] buf = new char[65535];
		try {
			int n = r.read(buf);
			return new String(buf, 0, n);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static boolean compareReaders(Reader r1, Reader r2) {
		char[] buf1 = new char[65535];
		char[] buf2 = new char[65535];
		try {
			int n1 = r1.read(buf1);
			int n2 = r2.read(buf2);
			return (n1 == n2 && Arrays.equals(buf1, buf2));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static boolean compareInputStreams(InputStream i1, InputStream i2) {
		byte[] buf1 = new byte[65535];
		byte[] buf2 = new byte[65535];
		try {
			int n1 = i1.read(buf1);
			int n2 = i2.read(buf2);
			return (n1 == n2 && Arrays.equals(buf1, buf2));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// ---------- create random values ----------

	public static boolean randomBoolean() {
		return Math.random() > 0.5;
	}

	public static byte randomByte(byte min, byte max) {
		if (min==-1 && max==-1) {
			min = Byte.MIN_VALUE;
			max = Byte.MAX_VALUE;
		}
		return (byte) (min+Math.random()*(max-min));
	}

	public static byte[] randomByteArray(int size) {
		byte[] a = new byte[size];
		for (int i = 0; i < size; i++)
			a[i] = randomByte(Byte.MIN_VALUE, Byte.MAX_VALUE);
		return a;
	}

	public static Byte[] randomByteObjArray(int size) {
		Byte[] a = new Byte[size];
		for (int i = 0; i < size; i++)
			a[i] = new Byte(randomByte(Byte.MIN_VALUE, Byte.MAX_VALUE));
		return a;
	}

	public static short randomShort(short min, short max) {
		if (min==-1 && max==-1) {
			min = Short.MIN_VALUE;
			max = Short.MAX_VALUE;
		}
		return (short) (min+Math.random()*(max-min));
	}

	public static int randomInt(int min, int max) {
		if (min==-1 && max==-1) {
			min = Integer.MIN_VALUE;
			max = Integer.MAX_VALUE;
		}
		return (int) (min+Math.random()*(max-min));
	}

	public static long randomLong(long min, long max) {
		if (min==-1 && max==-1) {
			min = Long.MIN_VALUE;
			max = Long.MAX_VALUE;
		}
		return (long) (min+Math.random()*(max-min));
	}

	public static float randomFloat(float min, float max) {
		if (min==-1 && max==-1) {
			min = Float.MIN_VALUE;
			max = Float.MAX_VALUE;
		}
		//double f = (int) (Math.random() * 10);
		double f = 0;
		return (float) ( ((long)(min+Math.random()*(max-min))) + f/10 );
	}

	public static double randomDouble(double min, double max) {
		if (min==-1 && max==-1) {
			min = Double.MIN_VALUE;
			max = Double.MAX_VALUE;
		}
		double f = (int) (Math.random() * 10);
		return ((long)(min+Math.random()*(max-min))) + f/10;
	}

	public static char randomChar() {
		byte[] b = new byte[] { (byte) (97 + (byte) (Math.random() * 26)) };
		return new String(b).charAt(0);
	}

	public static char[] randomCharArray(int size) {
		return randomString(size).toCharArray();
	}

	public static Character[] randomCharObjArray(int size) {
		Character[] a = new Character[size];
		int i = 0;
		for (char c : randomString(size).toCharArray()) {
			a[i] = new Character(c);
			i++;
		}
		return a;
	}

	public static String randomString(int size) {
		byte b[] = new byte[size];
		for (int i = 0; i < size; i++) {
			b[i] = (byte) (97 + (byte) (Math.random() * 26));
		}
		return new String(b);
	}
	
	public static long randomTimestamp() {
		return (long) (Math.random() * System.currentTimeMillis());
	}

}
