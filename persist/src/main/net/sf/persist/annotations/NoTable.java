// $Id$

package net.sf.persist.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets the related class to not be mapped to a table. A class with this
 * annotation can be used to hold data from a query, but cannot be used to be
 * inserted, updated or deleted, or read by primary keys.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NoTable {

}
