package br.com.ibnetwork.guara.myapp.om;

import java.util.Date;
import java.util.List;

import br.com.ibnetwork.guara.annotation.ColumnInfo;
import br.com.ibnetwork.guara.annotation.InputInfo;

public class OtherBean
    extends BeanSupport
{
    @ColumnInfo(name="PARENT_ID", jdbcType=ColumnInfo.Type.BIGINT)
    @InputInfo(inputType="select", order=1, label="link to Parent")
    private long parentId;

    @InputInfo(label="Name", order=2)
    @ColumnInfo(name="NAME", nullable=false)
    private String name;

    @InputInfo(inputType="text", label="a Sample Date", format="dd/MM/yyyy", widgetType="dojo:DropdownDatePicker")
    @ColumnInfo(name="SAMPLE_DATE", jdbcType=ColumnInfo.Type.TIMESTAMP,nullable=false)
    private Date sampleDate;

    @InputInfo(includeOnDetail=false, includeOnListing=false, label="")
    @ColumnInfo(ignore=true)
    private PojoA pojoA;

    @ColumnInfo(ignore=true)
    @InputInfo(includeOnDetail=false, includeOnListing=false, label="")
    private List<PojoB> listOfPojos;

    @InputInfo(label="the Password",inputType="password", size="8", maxLength="8")
    private String password;

    @InputInfo(label="some Text",inputType="textarea")
    private String text;

    @InputInfo(label="A formatted number", format="{0,number,#,##0.00}")
    private long formattedLong;

    @InputInfo(label="A formatted number", format="{0,number,#,##0.00}")
    private double formattedDouble;
    
    @InputInfo(label="Enabled", format="{}")
    private boolean enabled;

    public long getParentId(){return parentId;}
    public void setParentId(long parentId){this.parentId = parentId;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public Date getSampleDate(){return sampleDate;}
    public void setSampleDate(Date sampleDate){this.sampleDate = sampleDate;}

    public PojoA getPojoA(){return pojoA;}
    public void setPojoA(PojoA pojoA){this.pojoA = pojoA;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

    public String getText(){return text;}
    public void setText(String text){this.text = text;}

    public List<PojoB> getListOfPojos(){return listOfPojos;}
    public void setListOfPojos(List<PojoB> listOfPojos){this.listOfPojos = listOfPojos;}
	
    public long getFormattedLong(){return formattedLong;}
	public void setFormattedLong(long formattedLong){this.formattedLong = formattedLong;}
	
	public double getFormattedDouble(){return formattedDouble;}
	public void setFormattedDouble(double formattedDouble){this.formattedDouble = formattedDouble;}
	
	public boolean isEnabled(){return enabled;}
	public void setEnabled(boolean enabled){this.enabled = enabled;}
}
