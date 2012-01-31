package br.com.ibnetwork.guara.app.modules.actions;

import br.com.ibnetwork.guara.app.test.SampleBean;
import br.com.ibnetwork.guara.rundata.RunData;

public class TestSingleCrud
    extends SingleCrudControlSupport
{
	private SampleBean sampleBean = new SampleBean();
	
	@Override
    protected SampleBean createFromRequest(RunData data)
        throws Exception
    {
	    return sampleBean;
    }

	@Override
    protected Class getBeanClass()
    {
	    return SampleBean.class;
    }

	

}
