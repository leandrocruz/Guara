package br.com.ibnetwork.guara.myapp.om;

import br.com.ibnetwork.guara.annotation.ColumnInfo;
import br.com.ibnetwork.guara.annotation.InputInfo;

public class SingleField
{
	@InputInfo(inputType="text", label="a Sample Date", format="dd/MM/yyyy")
	@ColumnInfo(name="SAMPLE_DATE", jdbcType=ColumnInfo.Type.TIMESTAMP)
	private String field;
}
