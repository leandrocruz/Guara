package br.com.ibnetwork.guara.injector;

import br.com.ibnetwork.guara.parameters.Parameters;

public interface Injector
{
	String ROLE = Injector.class.getName();

	void inject(Object obj, Parameters params)
		throws Exception;
}
