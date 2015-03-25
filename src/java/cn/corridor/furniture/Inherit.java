package cn.corridor.furniture;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate that a field will inherit from parent if not explicitly specified in json. 
 * Useless for vanilla types. (Which will always have a value)
 * @author WeathFolD
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inherit {

}
