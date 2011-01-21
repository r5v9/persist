// $Id$

package net.sf.persist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Iterator backed by a ResultSet.
 */
public final class ResultSetIterator implements Iterator {

	public static final int TYPE_OBJECT = 1;
	public static final int TYPE_MAP = 2;

	private final ResultSet resultSet;
	private final Persist persist;
	private final Class objectClass;
	private final int type;
	private boolean hasNext = false;

	public ResultSetIterator(final Persist persist, final Class objectClass, final ResultSet resultSet, final int type) {

		if (type != TYPE_OBJECT && type != TYPE_MAP) {
			throw new RuntimeSQLException("Invalid ResultSetIterator type: " + type);
		}

		this.persist = persist;
		this.objectClass = objectClass;
		this.resultSet = resultSet;
		this.type = type;

		try {
			hasNext = resultSet.next();
		} catch (SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	public boolean hasNext() {
		return hasNext;
	}

	public Object next() {
		try {
			final Object ret;
			if (type == TYPE_OBJECT) {
				ret = persist.loadObject(objectClass, resultSet);
			} else if (type == TYPE_MAP) {
				ret = Persist.loadMap(resultSet);
			} else {
				ret = null;
			}

			hasNext = resultSet.next();
			return ret;
		} catch (SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	public void remove() {
		try {
			this.resultSet.deleteRow();
		} catch (SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

}
