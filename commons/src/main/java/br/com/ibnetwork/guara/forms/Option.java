package br.com.ibnetwork.guara.forms;

import br.com.ibnetwork.guara.forms.impl.OptionImpl;

public interface Option
{
	String getValue();
	
	String getDisplayInCombo();
	
	boolean isSelected();
	
	static Option DEFAULT = new OptionImpl("0","--");
}
