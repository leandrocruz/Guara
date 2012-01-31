package br.com.ibnetwork.guara.rundata;

public class PageInfo
{
    public static final String TEMPLATE = "template";

    public static final String SCREEN = "screen";
    
    public static final String LAYOUT = "layout";
    
    public static final String ACTION = "action";
    
    public static final String PIPELINE = "pipeline";

    String screenTemplate;
    
    String layoutTemplate;
    
    String screenName;
    
    String actionName;
    
    public String getLayoutTemplate()
    {
        return layoutTemplate;
    }

    public void setLayoutTemplate(String layoutTemplate)
    {
        this.layoutTemplate = layoutTemplate;
    }

    public String getTemplate()
    {
        return screenTemplate;
    }

    public void setTemplate(String screenTemplate)
    {
        this.screenTemplate = screenTemplate;
    }

    public String getActionName()
    {
        return actionName;
    }
    
    public void setActionName(String actionName)
    {
        this.actionName = actionName;
    }
    
    public String getScreenName()
    {
        return screenName;
    }
    
    public void setScreenName(String screenName)
    {
        this.screenName = screenName;
    }
}
