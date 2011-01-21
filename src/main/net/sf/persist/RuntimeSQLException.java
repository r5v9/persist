// $Id$

package net.sf.persist;

@SuppressWarnings("serial")
public final class RuntimeSQLException extends RuntimeException {

	public RuntimeSQLException(final Throwable cause) {
		super(cause);
	}

	public RuntimeSQLException(final String message) {
		super(message);
	}

	public RuntimeSQLException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
