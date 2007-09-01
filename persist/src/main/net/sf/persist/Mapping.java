// $Id$

package net.sf.persist;

import java.lang.reflect.Method;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class Mapping {

	public abstract Method getGetterForColumn(String columnName);

	public abstract Method getSetterForColumn(String columnName);

	// ---------- utility methods ----------

	/**
	 * Factory method to create a Mapping based on a Class. Will return a
	 * NoTableAnnotation if the class has a NoTable annotation set, or
	 * TableAnnotation otherwise.
	 */
	public static final Mapping getMapping(final DatabaseMetaData metaData, final Class objectClass,
			final NameGuesser nameGuesser) {

		// get @NoTable annotation
		final net.sf.persist.annotations.NoTable noTableAnnotation = (net.sf.persist.annotations.NoTable) objectClass
				.getAnnotation(net.sf.persist.annotations.NoTable.class);

		// if @NoTable is set, build a NoTableAnnotation
		if (noTableAnnotation != null) {
			return new NoTableMapping(objectClass, nameGuesser);
		}

		// otherwise, build a TableAnnotation
		else {
			try {
				return new TableMapping(metaData, objectClass, nameGuesser);
			} catch (SQLException e) {
				throw new PersistException(e);
			}
		}
	}

	/**
	 * Returns an array with maps for annotations, getters and setters. Keys in
	 * each map are field names.
	 */
	protected static final Map[] getFieldsMaps(final Class objectClass) {
		final Method[] methods = objectClass.getMethods();

		// create map with all getters and setters

		final Map<String, Method[]> allMethods = new HashMap();
		for (Method method : methods) {
			final String name = method.getName();
			final String suffix = name.substring(3);

			Method[] getterSetter = allMethods.get(suffix);
			if (getterSetter == null) {
				getterSetter = new Method[2];
				allMethods.put(suffix, getterSetter);
			}

			if (name.startsWith("get")) {
				getterSetter[0] = method;
			} else if (name.startsWith("set")) {
				getterSetter[1] = method;
			}
		}

		// assemble annotations, getters and setters maps
		// a field is only taken into consideration if it has a getter and a
		// setter

		final Map<String, net.sf.persist.annotations.Column> annotationsMap = new HashMap();
		final Map<String, Method> gettersMap = new HashMap();
		final Map<String, Method> settersMap = new HashMap();

		for (String suffix : allMethods.keySet()) {

			final Method[] getterSetter = allMethods.get(suffix);

			// only consider fields to have both getters and setters
			if (getterSetter[0] != null && getterSetter[1] != null) {

				// field name (prefix with first char in lower case)
				final String fieldName = suffix.substring(0, 1).toLowerCase() + suffix.substring(1);

				// column annotation
				final net.sf.persist.annotations.Column getterAnnotation = getterSetter[0]
						.getAnnotation(net.sf.persist.annotations.Column.class);
				final net.sf.persist.annotations.Column setterAnnotation = getterSetter[1]
						.getAnnotation(net.sf.persist.annotations.Column.class);

				// if NoColumn is specified, don't use the field
				final net.sf.persist.annotations.NoColumn noPersistGetter = getterSetter[0]
						.getAnnotation(net.sf.persist.annotations.NoColumn.class);

				final net.sf.persist.annotations.NoColumn noPersistSetter = getterSetter[1]
						.getAnnotation(net.sf.persist.annotations.NoColumn.class);

				// check conflicting NoColumn and Column annotations
				if (noPersistGetter != null || noPersistSetter != null) {
					if (getterAnnotation != null || setterAnnotation != null) {
						throw new PersistException("Field [" + fieldName + "] from class [" + objectClass.getName()
								+ "] has conflicting NoColumn and Column annotations");
					}
					continue;
				}

				// assert that getters and setters have valid and compatible
				// types
				if (getterSetter[1].getParameterTypes().length != 1) {
					throw new PersistException("Setter [" + getterSetter[1]
							+ "] should have a single parameter but has " + getterSetter[1].getParameterTypes().length);
				}
				if (getterSetter[0].getReturnType() == void.class) {
					throw new PersistException("Getter [" + getterSetter[0] + "] must have a return parameter");
				}
				if (getterSetter[0].getReturnType() != getterSetter[1].getParameterTypes()[0]) {
					throw new PersistException("Getter [" + getterSetter[0] + "] and setter [" + getterSetter[1]
							+ "] have incompatible types");
				}

				// check for annotations on the getter/setter
				net.sf.persist.annotations.Column annotation = null;

				if (getterAnnotation != null && setterAnnotation != null) {

					// if both getter and setter have annotations, make sure
					// they are equals
					if (!getterAnnotation.equals(setterAnnotation)) {

						final String getterAnn = getterAnnotation.toString().substring(
								getterAnnotation.toString().indexOf('(') + 1,
								getterAnnotation.toString().lastIndexOf(')'));

						final String setterAnn = setterAnnotation.toString().substring(
								setterAnnotation.toString().indexOf('(') + 1,
								setterAnnotation.toString().lastIndexOf(')'));

						throw new PersistException("Annotations for getter [" + getterSetter[0] + "] and setter ["
								+ getterSetter[1] + "] have different annotations [" + getterAnn + "] [" + setterAnn
								+ "]");
					}

					annotation = getterAnnotation;
				} else if (getterAnnotation != null) {
					annotation = getterAnnotation;
				} else if (setterAnnotation != null) {
					annotation = setterAnnotation;
				}

				// make getter and setter accessible
				getterSetter[0].setAccessible(true);
				getterSetter[1].setAccessible(true);

				annotationsMap.put(fieldName, annotation);
				gettersMap.put(fieldName, getterSetter[0]);
				settersMap.put(fieldName, getterSetter[1]);
			}
		}

		return new Map[] { annotationsMap, gettersMap, settersMap };
	}

}
