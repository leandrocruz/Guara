package br.com.ibnetwork.guara.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InputInfo {
	
	/*
	 * Labeling and Ordering
	 */
	String label();
	String labelLong() default "";
	int order() default -1;
	
	/*
	 * Fields Groups: 
	 */
	String group() default "";
	int groupOrder() default -1;
	
	boolean includeOnDetail() default true;
	boolean includeOnListing() default true; 
	
	String inputType() default "text";
	String widgetType() default "";

	String format() default "";
	String mask() default ""; /* used my iMask */
	
	String size() default "32";
	String maxLength() default "";
	String rows() default "5";
	String cols() default "30";
	
	public enum ReferenceType {UNDEF};
	Class referenceLoader() default Object.class;
	ReferenceType refenceType() default ReferenceType.UNDEF;
	
	/*
	 * When a field is frozen, it's value can't be changed after the id property is greater than ZERO
	 */
	boolean freeze() default false;

}
