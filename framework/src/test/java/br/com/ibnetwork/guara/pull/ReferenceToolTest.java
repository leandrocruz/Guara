package br.com.ibnetwork.guara.pull;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import br.com.ibnetwork.guara.forms.Option;
import br.com.ibnetwork.guara.pull.tools.ReferenceTool;
import br.com.ibnetwork.guara.test.GuaraTestCase;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;

public class ReferenceToolTest
    extends GuaraTestCase
{
	@Inject
	private Factory factory;

	@Test
	public void testCallLoader()
		throws Exception
	{
		ReferenceTool tool = (ReferenceTool) factory.create(ReferenceTool.class);
		List<Option> options = tool.load(SampleLoader.class.getName());
		assertEquals(1,options.size());
		Option op = options.get(0);
		assertEquals("1", op.getValue());
		assertEquals("First", op.getDisplayInCombo());
	}
}
