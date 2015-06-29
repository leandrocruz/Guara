package br.com.ibnetwork.guara.app.modules;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.guara.app.file.FileRepository;
import br.com.ibnetwork.guara.message.SystemMessage;
import br.com.ibnetwork.guara.message.SystemMessageBroker;
import br.com.ibnetwork.guara.modules.ModuleSupport;
import br.com.ibnetwork.guara.rundata.RunData;
import br.com.ibnetwork.guara.view.TemplateUtils;
import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.store.ObjectStore;
import xingu.store.PersistentBean;
import br.com.ibnetwork.xingu.template.Context;
import br.com.ibnetwork.xingu.template.TemplateEngine;
import xingu.validator.BeanValidator;
import xingu.validator.ValidatorContext;

public abstract class GuaraModuleSupport
	extends ModuleSupport
{
	protected Log log = LogFactory.getLog(this.getClass());

	@Inject
    protected ObjectStore store;
	
	@Inject
    protected SystemMessageBroker sysMsg;
	
	@Inject
    protected Factory factory;
	
	@Inject
	protected BeanValidator validator;
	
	@Inject
	protected FileRepository repository;
	
    @Inject
    private TemplateEngine templateEngine;


	protected final ValidatorContext runValidator(RunData data, Object bean)
		throws Exception
	{
		ValidatorContext ctx = new ValidatorContext();
		validator.validate(bean, ctx);
		validateComplex(data, bean, ctx);
		return ctx;
	}

	protected void validateComplex(RunData data, Object bean, ValidatorContext context)
		throws Exception
    {
		//allow for complex validations of beans
    }

	protected Result createResult(PersistentBean bean, ValidatorContext vCtx)
	{
		return createResult(bean, vCtx,-1);
	}
	
	protected Result createResult(PersistentBean bean, ValidatorContext vCtx, int index)
    {
		Result result = (Result) factory.create(Result.class);
		result.setBean(bean);
		result.setValidatorContext(vCtx);
		result.setIndex(index);
	    return result;
    }
	
	protected SystemMessage getSystemMessage(String msgId)
	{
		return sysMsg.getSystemMessage(msgId);
	}

	protected SystemMessage getSystemMessage(String msgId, Locale locale)
	{
		return sysMsg.getSystemMessage(msgId, locale);
	}
	
    protected String render(Context ctx, String templateName)
    {
        return TemplateUtils.renderTemplate(templateEngine, "UTF-8", ctx, templateName);
    }
	
}
