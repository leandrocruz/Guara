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
	
	<select id="getById" 
			parameterClass="long" 
			resultMap="${meta.referenceName}Result">
		SELECT * FROM ${meta.tableName} WHERE id = #value#
	</select>

	<select id="getAll"
			resultMap="${meta.referenceName}Result">
		SELECT * FROM ${meta.tableName}
	</select>

	<insert id="insert"
			parameterMap="${meta.referenceName}Param">
		<selectKey keyProperty="id"  resultClass="long">
			SELECT nextval('${meta.tableName?lower_case}_id_seq') as value
			<!-- HSQLDB: SELECT TOP 1 NEXT VALUE FOR ${meta.tableName}_ID_SEQ AS value FROM ${meta.tableName} -->
		</selectKey>
		INSERT INTO 
			${meta.tableName} (${meta.beanInfo.joinColumnNames("")})
		VALUES 
			(${meta.beanInfo.joinColumnNames("?")})
	</insert>

	<update id="update"
			parameterClass="${meta.domainClass}">
		UPDATE 
			${meta.tableName} 
		SET
			<#list meta.beanInfo.properties as property>
			    <#if property.name != "id" && !property.ignoreColumn() >
			${property.columnName} = #${property.name}#<#if property_has_next>,</#if>
				</#if>
			</#list>
		WHERE 
			id = #id#
	</update>	
	
	<delete id="delete"
			parameterClass="long">
		DELETE FROM ${meta.tableName} WHERE id = #value#
	</delete>

</sqlMap>