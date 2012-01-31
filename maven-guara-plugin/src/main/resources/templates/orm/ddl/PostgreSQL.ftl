DROP SEQUENCE ${meta.tableName?lower_case}_id_seq;
DROP TABLE ${meta.tableName} CASCADE;
CREATE SEQUENCE ${meta.tableName?lower_case}_id_seq START 100; 
CREATE TABLE ${meta.tableName} (
<#list meta.beanInfo.properties as property>
	<#if !property.ignoreColumn()>
${"\t"}${property.columnName} ${property.jdbcType}<#if !property.isNullable()> NOT NULL</#if><#if property.isPrimaryKey()> PRIMARY KEY</#if><#if property_has_next>,</#if>
	</#if>
</#list>
);