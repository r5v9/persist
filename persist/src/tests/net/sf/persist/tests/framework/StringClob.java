
// $Id$

package net.sf.persist.tests.framework;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Clob implementation backed by a String, suitable for sending data to the database only
 * (does not implement mutable methods)
 */
public class StringClob implements Clob {
	
	private String s;
	
	public StringClob(String s) {
		this.s = s;
	}
	
	//not in the interface
	public String getString() {
		return s;
	}

	public InputStream getAsciiStream() throws SQLException {
		return new ByteArrayInputStream(s.getBytes());
	}

	public Reader getCharacterStream() throws SQLException {
		return new StringReader(s);
	}

	public String getSubString(long pos, int length) throws SQLException {
		return s.substring((int)pos-1, (int)(pos+length)-1);
	}

	public long length() throws SQLException {
		return s.length();
	}

	public long position(String searchstr, long start) throws SQLException {
		return s.indexOf(searchstr, (int)start);
	}

	public long position(Clob searchstr, long start) throws SQLException {
		throw new RuntimeException("position(Clob,long) not implemented");
	}

	public OutputStream setAsciiStream(long pos) throws SQLException {
		throw new RuntimeException("setAsciiStream(long) not implemented");
	}

	public Writer setCharacterStream(long pos) throws SQLException {
		throw new RuntimeException("setCharacterStream(long) not implemented");
	}

	public int setString(long pos, String str) throws SQLException {
		throw new RuntimeException("setString(lon, String) not implemented");
	}

	public int setString(long pos, String str, int offset, int len) throws SQLException {
		throw new RuntimeException("setString(long,int,int) not implemented");
	}

	public void truncate(long len) throws SQLException {
		throw new RuntimeException("truncate(long) not implemented");
	}

}
