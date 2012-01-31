package br.com.ibnetwork.guara.rundata;

import br.com.ibnetwork.guara.modules.Module;

public class Outcome
{
    public static final String ERROR_CODE = "ERROR";
    
    public static final String UNKNOWN_CODE = "UNKNOWN";
    
    public static final String SUCCESS_CODE = "SUCCESS";
    
    public static Outcome UNKNOWN = new Outcome(UNKNOWN_CODE,null,null);
    
    private String code;
    
    private Module source;
    
    private String method;
    
    private Outcome(String code, Module source, String method)
    {
        this.code = code;
        this.source = source;
        this.method = method; 
    }
    
    public String getCode() 
    {
        return code;
    }
    
    public Module getSource()
    {
        return source;
    }

    public String getMethod()
    {
        return method;
    }

    public static Outcome unknown(String code, Module source) {
        return new Outcome(code,source,null);
    }

    public static Outcome unknown(String code, Module source, String method) {
        return new Outcome(code,source,method);
    }

    public static Outcome error(Module source) {
        return new Outcome(ERROR_CODE,source,null);
    }

    public static Outcome error(Module source, String method) {
        return new Outcome(ERROR_CODE,source,method);
    }

    public static Outcome success(Module source) {
        return new Outcome(SUCCESS_CODE,source,null);
    }

    public static Outcome success(Module source, String method) {
        return new Outcome(SUCCESS_CODE,source,method);
    }
    
    public boolean equals(Object other)
    {
        if(other == null) return false;
        if(this == other) return true;
        if(!(other instanceof Outcome)) return false;
        Outcome obj = (Outcome) other;
        return obj.getSource() == source
            && (code == null ? obj.getCode() == null : code.equals(obj.getCode())) 
            && (method == null ? obj.getMethod() == null : method.equals(obj.getMethod())); 
    }
    
    public String toString()
    {
        return "Outcome: code("+code+") source("+source+") method("+method+")";
    }
}
