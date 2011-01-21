// $Id$

package net.sf.persist;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Default NameGuesser implementation.
 */
public final class DefaultNameGuesser implements NameGuesser {

	/**
	 * Given a field or class name in the form CompoundName (for classes) or
	 * compoundName (for fields) will return a set of guessed names such as
	 * [compound_name, compound_names, compoundname, compoundnames].
	 */
	public Set<String> guessColumn(final String fieldOrClassName) {

		final String nameUnderscore = fieldOrClassName.replaceAll("([A-Z])", "_$1").toLowerCase();
		final String nameLowercase = fieldOrClassName.toLowerCase(Locale.ENGLISH);

		final Set<String> names = new LinkedHashSet();
		names.add(nameUnderscore);
		names.add(nameLowercase);
		names.add(nameUnderscore + "s");
		names.add(nameLowercase + "s");
		return names;
	}

}
