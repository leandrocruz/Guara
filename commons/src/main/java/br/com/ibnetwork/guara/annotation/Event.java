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
public @interface Event {
	public enum Type {CHANGE, RESET, SELECT, SUBMIT, KEYDOWN, KEYUP, KEYPRESS, CLICK, DBCLICK, MOUSEUP, MOUSEDOWN, MOUSEENTER, MOUSELEAVE, MOUSEMOVE, MOUSEOVER, MOUSEOUT, FOCUS, BLUR};
	public enum RunAt {SERVER, CLIENT};
	Type type();
	RunAt runAt() default RunAt.SERVER;
	String action();
	String method() default "";
}

