
// $Id$

package net.sf.persist.tests.framework;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * Blob implementation backed by a byte array, suitable for sending data to the database only
 * (does not implement mutable methods)
 */
public class BytesBlob implements Blob {
	
	byte[] b;
	
	public BytesBlob(byte[] b) {
		super();
		this.b = b;
	}

	public InputStream getBinaryStream() throws SQLException {
		return new ByteArrayInputStream(b);
	}

	public byte[] getBytes(long pos, int length) throws SQLException {
		byte[] ret = new byte[length];
		for (int i=(int)pos-1; i<pos+length-1; i++) ret[i] = b[i+(int)pos-1];
		return ret;
	}

	public long length() throws SQLException {
		return b.length;
	}

	public long position(byte[] pattern, long start) throws SQLException {
		throw new RuntimeException("position(byte[],long) not implemented");
	}

	public long position(Blob pattern, long start) throws SQLException {
		throw new RuntimeException("position(Blob,long) not implemented");
	}

	public OutputStream setBinaryStream(long pos) throws SQLException {
		throw new RuntimeException("setBinaryStream(long) not implemented");
	}

	public int setBytes(long pos, byte[] bytes) throws SQLException {
		throw new RuntimeException("setBytes(long,byte[]) not implemented");
	}

	public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
		throw new RuntimeException("setBytes(long,byte[],int,int) not implemented");
	}

	public void truncate(long len) throws SQLException {
		throw new RuntimeException("truncate(long) not implemented");
	}

}
