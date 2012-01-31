CREATE SEQUENCE ${meta.tableName}_ID_SEQ AS BIGINT START WITH 100 
<@compress single_line=true>
CREATE TABLE ${meta.tableName}(
	<#list meta.beanInfo.properties as property>
		<#if !property.ignoreColumn()>
			${property.columnName} ${property.jdbcType}
			<#if !property.isNullable()>NOT NULL</#if>
			<#if property.isPrimaryKey()>PRIMARY KEY</#if>
			<#if property_has_next>,</#if>
		</#if>
	</#list>
)
</@compress>

CREATE USER SA PASSWORD "" ADMIN
<#list [1,2,3] as index>
	<@compress single_line=true>
		INSERT INTO ${meta.tableName} VALUES(${index},
		<#list meta.beanInfo.properties as property>
			<#if !property.ignoreColumn() && property.name != "id">
				<#if property.sampleValue??>
					${property.sampleValue}
				<#else>
					NULL
				</#if>
				<#if property_has_next>,</#if>
			</#if>
		</#list>
	</@compress>
)
</#list>
