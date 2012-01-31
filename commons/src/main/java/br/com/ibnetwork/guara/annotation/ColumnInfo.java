package br.com.ibnetwork.guara.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Pulga uses this annotation to inject dependencies into it's components
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnInfo {
	public enum Type {UNDEF, BIT, TINYINT, SMALLINT, INT, BIGINT, FLOAT, DOUBLE, DECIMAL, DATE, DATETIME, TIMESTAMP, VARCHAR};
	boolean primaryKey() default false;
	Type jdbcType() default Type.UNDEF;
	String name() default "";
	boolean ignore() default false;
	boolean nullable() default true;
}

