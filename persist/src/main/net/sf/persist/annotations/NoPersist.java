
// $Id$

package net.sf.persist.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets the related field to not be handled by the Persist engine.
 * Must be added to a getter or a setter of the field being mapped.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NoPersist {	
}
