package com.rabbit.extend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.rabbit.common.enums.HandleReceivedDataType;

/*@With(ApiHistoryAction.class)*/
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD, ElementType.TYPE}) 
public @interface ApiHistory {
	HandleReceivedDataType type() default HandleReceivedDataType.NOT_SET_HANDLE_TYPE;
	String createuser() default "";
}
