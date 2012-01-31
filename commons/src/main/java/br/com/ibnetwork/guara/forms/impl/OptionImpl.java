package br.com.ibnetwork.guara.forms.impl;

import br.com.ibnetwork.guara.forms.Option;

public class OptionImpl
    implements Option
{
	protected String value;
	
	protected String display;

	protected boolean selected;
	
	public OptionImpl(long value, String display)
	{
		this.value = Long.toString(value);
		this.display = display;
	}

	public OptionImpl(long value, String display, boolean selected)
	{
		this.value = Long.toString(value);
		this.display = display;
		this.selected = selected;
	}

	public OptionImpl(String value, String display)
	{
		this.value = value;
		this.display = display;
	}

	public OptionImpl(String value, String display, boolean selected)
	{
		this.value = value;
		this.display = display;
		this.selected = selected;
	}
	
	public String getDisplayInCombo()
	{
		return display;
	}

	public String getValue()
	{
		return value;
	}

	public boolean isSelected()
	{
		return selected;
	}
}
