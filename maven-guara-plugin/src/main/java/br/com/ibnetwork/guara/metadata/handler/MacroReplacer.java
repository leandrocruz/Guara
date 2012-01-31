package br.com.ibnetwork.guara.metadata.handler;

public class MacroReplacer
    extends ReplaceOnFile
{
	protected boolean checkEnd(String line)
    {
	    return !replaced && start >=0 && line.indexOf("</#macro>") >= 0;
    }

	protected boolean checkStart(String line)
    {
	    return !replaced && line.indexOf("<#macro "+meta.getReferenceName()+"Macro") >= 0;
    }
}
