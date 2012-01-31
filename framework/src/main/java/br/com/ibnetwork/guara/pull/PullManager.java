package br.com.ibnetwork.guara.pull;

import java.util.List;

public interface PullManager
{
    String SCOPE_GLOBAL = "global";
    
    String SCOPE_REQUEST = "request";
    
    String SCOPE_SESSION = "session";
    
    List<ApplicationToolHandler> getAllToolHandlers(String scope);

    ApplicationTool toolByName(String name)
        throws Exception;
}
