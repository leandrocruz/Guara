package br.com.ibnetwork.guara;

import java.util.Date;

import br.com.ibnetwork.guara.annotation.ColumnInfo;
import br.com.ibnetwork.guara.annotation.InputInfo;

public class SampleBean
{
	@InputInfo(order=0, label = "")
	private long id;
	
    @ColumnInfo(name="PARENT_ID", jdbcType=ColumnInfo.Type.BIGINT)
    @InputInfo(inputType="select", label="link to Parent")
    private long parentId;

    @InputInfo(label="Name")
    @ColumnInfo(name="NAME", nullable=false)
    private String name;

    @InputInfo(inputType="text", label="a Sample Date", format="dd/MM/yyyy", widgetType="dojo:DropdownDatePicker")
    @ColumnInfo(name="SAMPLE_DATE", jdbcType=ColumnInfo.Type.TIMESTAMP,nullable=false)
    private Date sampleDate;

    @InputInfo(label="the Password",inputType="password", size="8", maxLength="8", order=2)
    private String password;

    @InputInfo(label="some Text",inputType="textarea", order=1)
    private String text;

    @InputInfo(label="A formatted number", format="{0,number,#,##0.00}")
    private long formattedLong;

    @InputInfo(label="A formatted number", format="{0,number,#,##0.00}")
    private double formattedDouble;
    
    @InputInfo(label="Enabled")
    private boolean enabled;

    public long getId(){return id;}
    public void setId(long id){this.id = id;}
	public long getParentId(){return parentId;}
    public void setParentId(long parentId){this.parentId = parentId;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public Date getSampleDate(){return sampleDate;}
    public void setSampleDate(Date sampleDate){this.sampleDate = sampleDate;}
    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}
    public String getText(){return text;}
    public void setText(String text){this.text = text;}
    public long getFormattedLong(){return formattedLong;}
	public void setFormattedLong(long formattedLong){this.formattedLong = formattedLong;}
	public double getFormattedDouble(){return formattedDouble;}
	public void setFormattedDouble(double formattedDouble){this.formattedDouble = formattedDouble;}
	public boolean isEnabled(){return enabled;}
	public void setEnabled(boolean enabled){this.enabled = enabled;}
}