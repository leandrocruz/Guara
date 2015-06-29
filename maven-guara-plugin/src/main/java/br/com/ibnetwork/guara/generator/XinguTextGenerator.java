package br.com.ibnetwork.guara.generator;

import java.io.StringWriter;

import br.com.ibnetwork.guara.metadata.Metadata;
import xingu.container.Inject;
import xingu.template.Context;
import xingu.template.TemplateEngine;

public class XinguTextGenerator
    implements Generator
{
	
	@Inject
	private TemplateEngine engine;
	
	public String generate(Metadata meta)
	{
        Context ctx = engine.createContext();
        ctx.put("meta",meta);
        StringWriter sw = new StringWriter();
        engine.merge(meta.getTemplate(), ctx, sw);
        return sw.toString();
	}

}
