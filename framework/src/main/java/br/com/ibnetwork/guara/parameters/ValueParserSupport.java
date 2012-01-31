package br.com.ibnetwork.guara.parameters;

import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.ibnetwork.xingu.utils.ArrayUtils;

public class ValueParserSupport 
	implements ValueParser
{
    protected String encoding;

    protected Log logger = LogFactory.getLog(this.getClass());
    
    protected Map<String, String[]> map = new HashMap<String, String[]>();
    
    protected Map<String, Format> formats = new HashMap<String, Format>();

    public ValueParserSupport(String encoding)
    {
        this.encoding = encoding;
        formats.put("dd/MM/yyyy", new SimpleDateFormat("dd/MM/yyyy"));
    }
    
    private void logParseError(String key, Object value, Throwable t)
    {
        logger.error("Error trying to format ["+key+"] value["+value+"]",t);
    }

    public String getEncoding()
    {
        return encoding;
    }

	public Map<String, String[]> getValueMap()
    {
	    return map;
    }

    public Set<String> keySet()
    {
        return map.keySet();
    }

    public void clear()
    {
        map.clear();
    }

    /*
     * String
     */
    
    public String getString(String key)
    {
        String[] value = map.get(key);
        return value != null ? value[0] : null;
    }

    public String get(String key)
    {
        return getString(key);
    }

    public String getTrimmed(String key)
    {
        String value = get(key);
        return StringUtils.trimToNull(value);
    }
    
    public String getString(String key, String defaultValue)
    {
        String value = getString(key);
        return value != null ? value : defaultValue;
    }
    
    public String[] getStrings(String key)
    {
        return map.get(key);
    }

    public String[] getStringsTrimmed(String key)
    {
        String[] array = map.get(key);
        if(array == null)
        {
            return null;
        }
        List<String> tmp = new ArrayList<String>(array.length);
        for (int i = 0; i < array.length; i++)
        {
            String value = array[i];
            value = StringUtils.trimToNull(value);
            if(value != null)
            {
                tmp.add(value);
            }
        }
        String[] result = ArrayUtils.toStringArray(tmp.toArray());
        return result;
    }

    public String[] getStrings(String key, String[] defaultValue)
    {
        String[] value = getStrings(key);
        return value != null ? value : defaultValue;
    }

    public void setString(String key, String value)
    {
        map.put(key, new String[]{value});
    }

    public void setStrings(String key, String[] values)
    {
        map.put(key, values);
    }
    public void add(String key, String value)
    {
        add(key, new String[]{value});
    }

    public void add(String key, String[] value)
    {
        String[] values = map.get(key);
        if(values != null)
        {
            int size = values.length + value.length;
            String[] newValues = new String[size];
            System.arraycopy(values,0,newValues,0,values.length);
            System.arraycopy(value,0,newValues,values.length,value.length);
            value = newValues;
        }
        map.put(key,value);
    }
    
    /*
     * Boolean
     */
    
    public Boolean getBooleanObject(String key)
    {
        String value = getTrimmed(key);
        if(value == null)
        {
            return null;
        }
        if (value.equals("1") || value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("yes")
                || value.equalsIgnoreCase("on"))
        {
            return Boolean.TRUE;
        }
        return null;
    }

    public Boolean getBooleanObject(String key, Boolean defaultValue)
    {
        Boolean value = getBooleanObject(key);
        return value != null ? value : defaultValue;
    }
    
    public boolean getBoolean(String key)
    {
        return getBoolean(key, false);
    }
    
    public boolean getBoolean(String key, boolean defaultValue)
    {
        Boolean value = getBooleanObject(key);
        return value != null ? value.booleanValue() : defaultValue;
    }

    public Boolean[] getBooleanObjects(String key)
    {
        String[] value = getStringsTrimmed(key);
        if(value == null)
        {
            return null;
        }
        Boolean[] result = new Boolean[value.length];
        for (int i = 0; i < value.length; i++)
        {
            String v = value[i];
            if (v.equals("1") || v.equalsIgnoreCase("true")
                    || v.equalsIgnoreCase("yes")
                    || v.equalsIgnoreCase("on"))
            {
                result[i] = Boolean.TRUE;
            }
            else
            {
                result[i] = Boolean.FALSE;
            }
        }
        return result;
    }
    
    /*
     * Integer
     */
    
    public int getInt(String key)
    {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue)
    {
        Integer value = getIntObject(key);
        return value != null ? value.intValue() : defaultValue;
    }

    public int[] getInts(String key)
    {
        String[] value = getStringsTrimmed(key);
        if(value == null)
        {
            return null;
        }
        int[] result = new int[value.length];
        for (int i = 0; i < value.length; i++)
        {
            try
            {
                result[i] = Integer.parseInt(value[i]);
            }
            catch (NumberFormatException e)
            {
                logParseError(key,value,e);
            }
        }
        return result;        
    }

    public Integer getIntObject(String key, Integer defaultValue)
    {
        Integer value = getIntObject(key);
        return value != null ? value : defaultValue;        
    }

    public Integer getIntObject(String key)
    {
        String value = getTrimmed(key);
        if(value == null)
        {
            return null;
        }
        try
        {
            return new Integer(value);
        }
        catch(NumberFormatException e)
        {
            logParseError(key, value, e);
        }
        return null;
    }

    public Integer[] getIntObjects(String key)
    {
        String[] value = getStringsTrimmed(key);
        if(value == null)
        {
            return null;
        }
        Integer[] result = new Integer[value.length];
        for (int i = 0; i < value.length; i++)
        {
            try
            {
                result[i] = new Integer(value[i]);
            }
            catch (NumberFormatException e)
            {
                logParseError(key,value,e);
            }
        }
        return result;
    }

    /*
     * Long
     */
    
    public long getLong(String key)
    {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue)
    {
        Long value = getLongObject(key);
        return value != null ? value.longValue() : defaultValue;
    }

    public long[] getLongs(String key)
    {
        String[] value = getStringsTrimmed(key);
        if(value == null)
        {
            return null;
        }
        long[] result = new long[value.length];
        for (int i = 0; i < value.length; i++)
        {
            try
            {
                result[i] = Long.parseLong(value[i]);
            }
            catch (NumberFormatException e)
            {
                logParseError(key,value,e);
            }
        }
        return result;        
    }

    public Long getLongObject(String key, Long defaultValue)
    {
        Long value = getLongObject(key);
        return value != null ? value : defaultValue;        
    }

    public Long getLongObject(String key)
    {
        String value = getTrimmed(key);
        if(value == null)
        {
            return null;
        }
        try
        {
            return new Long(value);
        }
        catch(NumberFormatException e)
        {
            logParseError(key, value, e);
        }
        return null;
    }

    public Long[] getLongObjects(String key)
    {
        String[] value = getStringsTrimmed(key);
        if(value == null)
        {
            return null;
        }
        Long[] result = new Long[value.length];
        for (int i = 0; i < value.length; i++)
        {
            try
            {
                result[i] = new Long(value[i]);
            }
            catch (NumberFormatException e)
            {
                logParseError(key,value,e);
            }
        }
        return result;
    }

    /*
     * Float
     */

    public Float getFloatObject(String key)
    {
        String value = getTrimmed(key);
        if(value == null)
        {
            return null;
        }
        try
        {
            return new Float(value);
        }
        catch(NumberFormatException e)
        {
            logParseError(key,value,e);
        }
        return null;
    }
    
    public Float getFloatObject(String key, Float defaultValue)
    {
        Float value = getFloatObject(key);
        return value != null ? value : defaultValue;
    }

    public float getFloat(String key)
    {
        return getFloat(key, 0.0f);
    }

    public float getFloat(String key, float defaultValue)
    {
        Float value = getFloatObject(key);
        return value != null ? value.floatValue() : defaultValue;
    }

    public float[] getFloats(String key)
    {
        String[] value = getStrings(key);
        if(value == null)
        {
            return null;
        }
        float[] result = new float[value.length];
        for (int i = 0; i < value.length; i++)
        {
            try
            {
                result[i] = Float.parseFloat(value[i]);
            }
            catch (NumberFormatException e)
            {
                logParseError(key,value,e);
            }
        }
        return result;
    }

    public Float[] getFloatObjects(String key)
    {
        String[] value = getStringsTrimmed(key);
        if(value == null)
        {
            return null;
        }
        Float[] result = new Float[value.length];
        for (int i = 0; i < value.length; i++)
        {
            try
            {
                result[i] = new Float(value[i]);
            }
            catch (NumberFormatException e)
            {
                logParseError(key,value,e);
            }
        }
        return result;
    }

    /*
     * Double
     */

    public Double getDoubleObject(String key)
    {
        String value = getTrimmed(key);
        if(value == null)
        {
            return null;
        }
        try
        {
            return new Double(value);
        }
        catch(NumberFormatException e)
        {
            logParseError(key,value,e);
        }
        return null;
    }
    
    public Double getDoubleObject(String key, Double defaultValue)
    {
        Double value = getDoubleObject(key);
        return value != null ? value : defaultValue;
    }

    public double getDouble(String key)
    {
        return getDouble(key, 0.0);
    }

    public double getDouble(String key, double defaultValue)
    {
        Double value = getDoubleObject(key);
        return value != null ? value.doubleValue() : defaultValue;
    }

    public double[] getDoubles(String key)
    {
        String[] value = getStrings(key);
        if(value == null)
        {
            return null;
        }
        double[] result = new double[value.length];
        for (int i = 0; i < value.length; i++)
        {
            try
            {
                result[i] = Double.parseDouble(value[i]);
            }
            catch (NumberFormatException e)
            {
                logParseError(key,value,e);
            }
        }
        return result;
    }

    public Double[] getDoubleObjects(String key)
    {
        String[] value = getStrings(key);
        if(value == null)
        {
            return null;
        }
        Double[] result = new Double[value.length];
        for (int i = 0; i < value.length; i++)
        {
            try
            {
                result[i] = new Double(value[i]);
            }
            catch (NumberFormatException e)
            {
                logParseError(key,value,e);
            }
        }
        return result;
    }

    /*
     * BigDecimal
     */

    public BigDecimal getBigDecimal(String key)
    {
        String value = getTrimmed(key);
        if(value == null)
        {
            return null;
        }
        try
        {
            return new BigDecimal(value);
        }
        catch(NumberFormatException e)
        {
            logParseError(key,value,e);
        }
        return null;
    }
    
    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue)
    {
        BigDecimal value = getBigDecimal(key);
        return value != null ? value : defaultValue;
    }

    public BigDecimal[] getBigDecimals(String key)
    {
        String[] value = getStrings(key);
        if(value == null)
        {
            return null;
        }
        BigDecimal[] result = new BigDecimal[value.length];
        for (int i = 0; i < value.length; i++)
        {
            try
            {
                result[i] = new BigDecimal(value[i]);
            }
            catch (NumberFormatException e)
            {
                logParseError(key,value,e);
            }
        }
        return result;
    }

    /*
     * Date
     */

    public Date getDate(String key)
    {
    	Format format = formats.get("dd/MM/yyyy");
        return getDate(key, format);
    }
    
    public Date getDate(String key, Format format)
    {
        return getDate(key, format, null);
    }

    public Date getDate(String key, String formatPattern)
    {
    	return getDate(key,formatPattern,null);
    }

	public Date getDate(String key, String formatPattern, Date defaultValue)
    {
    	Format format = formats.get(formatPattern);
    	if(format == null)
    	{
    		format = new SimpleDateFormat(formatPattern);
    		formats.put(formatPattern, format);
    	}
	    return getDate(key, format, defaultValue);
    }
	
    public Date getDate(String key, Format format, Date defaultValue)
    {
        Date result = defaultValue;
        String value = getString(key);
        if (value == null)
        {
            return null;
        }
        try
        {
            result = (Date) format.parseObject(value);
        }
        catch (ParseException e)
        {
            logger.error("Error parsing Date ["+key+"]", e);
        }
        return result;
    }

    
    /*
     * Object
     */
    
    public Object getObject(String key)
    {
        return getString(key);
    }

    public Object[] getObjects(String key)
    {
        return getStrings(key);
    }

    @Override
    public Map<String, String> getProperties(String prefix, boolean stripPrefixFromKey)
    {
        int length = prefix.length();
        Map<String, String> result = new HashMap<String, String>();
        Set<String> keys = keySet();
        for (String key : keys)
        {
            if(key.startsWith(prefix))
            {
                String value = get(key);
                String propertyName = key;
                if(stripPrefixFromKey)
                {
                    propertyName = key.substring(length);
                }
                result.put(propertyName, value);
            }
        }
        return result;
    }
}
