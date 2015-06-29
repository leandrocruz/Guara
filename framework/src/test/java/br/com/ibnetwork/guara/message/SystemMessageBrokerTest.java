package br.com.ibnetwork.guara.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Test;

import br.com.ibnetwork.guara.test.GuaraTestCase;
import xingu.container.Inject;


public class SystemMessageBrokerTest 
	extends GuaraTestCase
{
	@Inject
	private SystemMessageBroker sysMsg;
	
	private Locale locale = new Locale("en");
	
	private Object[] args = new Object[]{"leandro"};
    
	@Test
    public void testGetMessageById()
    	throws Exception 
    {
		
		SystemMessage msg = sysMsg.getSystemMessage("userLogin");
		String text = msg.getText(args);
		assertEquals("leandro, bem vindo ao xingu",text);
		assertEquals("info",msg.getType());
    
    	msg = sysMsg.getSystemMessage("userLogin",locale);
    	text = msg.getText(args);
    	assertEquals("leandro, welcome to xingu",text);
    }

	@Test
	public void testAccents()
		throws Exception
	{
		SystemMessage msg = sysMsg.getSystemMessage("userLogout");
		//breaks on maven
		//assertEquals("at? mais leandro",msg);
	}
	
	@Test
	public void testMessageNotFound()
		throws Exception
	{
	    assertFalse(sysMsg.isLenient());
	    try
		{
			SystemMessage msg = sysMsg.getSystemMessage("notFound");	
			fail("should have thrown an exception");
		}
		catch(SystemMessageException e)
		{
			//ignored
		}
	}
}
