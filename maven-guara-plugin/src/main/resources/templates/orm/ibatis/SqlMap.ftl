<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE sqlMap 
	PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN"  
	"http://ibatis.apache.org/dtd/sql-map-2.dtd">

<sqlMap namespace="${meta.referenceName}">
	<resultMap	id="${meta.referenceName}Result" 
				class="${meta.domainClass}">
	<#list meta.beanInfo.properties as property>
		<#if !property.ignoreColumn()>
		<result property="${property.name}" column="${property.columnName}" jdbcType="${property.jdbcType}" <#if property.nullValue??>nullValue="${property.nullValue}"</#if>/>
		</#if>
	</#list>
	</resultMap>

	<parameterMap 	id="${meta.referenceName}Param" 
					class="${meta.domainClass}">
	<#list meta.beanInfo.properties as property>
		<#if !property.ignoreColumn()>
		<parameter property="${property.name}" jdbcType="${property.jdbcType}" <#if property.nullValue??>nullValue="${property.nullValue}"</#if>/>
		</#if>
	</#list>
	</parameterMap>
	
	<!-- Put your statements here -->
	
</sqlMap>