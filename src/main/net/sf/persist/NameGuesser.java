// $Id$

package net.sf.persist;

import java.util.Set;

/**
 * Defines how a class or field name should be mapped to a table or column.
 */
public interface NameGuesser {

	/**
	 * Given a field or class name, return a Set with corresponding guessed
	 * names in the database.
	 */
	public Set<String> guessColumn(String fieldOrClassName);

}
