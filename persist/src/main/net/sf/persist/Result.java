// $Id$

package net.sf.persist;

import java.util.List;

/**
 * Represents a result from an insert operation with auto-generated keys.
 */
public final class Result {

	private final int rowsModified;
	private final List<String> generatedKeys;

	public Result(final int rowsModified, final List generatedKeys) {
		this.rowsModified = rowsModified;
		this.generatedKeys = generatedKeys;
	}

	public int getRowsModified() {
		return rowsModified;
	}

	public List<String> getGeneratedKeys() {
		return generatedKeys;
	}

}
