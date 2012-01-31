package br.com.ibnetwork.guara.metadata.impl;


public class AjaxScreen
    extends EditScreen
{
	public void init()
		throws Exception
	{
		super.init();
		template = "/templates/view/freemarker/Ajax.ftl";
		outputFile = outputDirectory+"/Ajax.ftl";
	}
}
