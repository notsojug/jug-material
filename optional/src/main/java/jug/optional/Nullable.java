package jug.optional;

import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The field may be {@code null}
 * 
 * @author Filippo B.
 *
 */
@Documented
@Retention(value = RUNTIME)
@Target(value = { TYPE_USE, TYPE_PARAMETER })
public @interface Nullable {

}
