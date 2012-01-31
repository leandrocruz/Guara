package br.com.ibnetwork.guara.parameters;

import java.math.BigDecimal;
import java.text.Format;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface ValueParser
{
    Set<String> keySet();

    void clear();

    String getEncoding();

    Map<String, String[]> getValueMap();
    
    Map<String, String> getProperties(String prefix, boolean stripPrefixFromKey);

    /*
     * String
     */
    
    void add(String key, String value);
    
    void add(String key, String[] value);

    void setString(String key, String value);
    
    void setStrings(String key, String[] values);

    String get(String key);

    String getString(String key, String defaultValue);

    String getString(String key);
    
    String getTrimmed(String key);

    String[] getStrings(String key);
    
    String[] getStringsTrimmed(String key);

    String[] getStrings(String key, String[] defaultValue);

    /*
     * Boolean
     */
    
    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean defaultValue);

    Boolean getBooleanObject(String key);

    Boolean getBooleanObject(String key, Boolean defaultValue);
    
    Boolean[] getBooleanObjects(String key);

    /*
     * Integer
     */
    
    int getInt(String key);

    int getInt(String key, int defaultValue);

    int[] getInts(String key);

    Integer getIntObject(String key, Integer defaultValue);

    Integer getIntObject(String key);

    Integer[] getIntObjects(String key);

    /*
     * Long
     */
    
    long getLong(String key);

    long getLong(String key, long defaultValue);

    long[] getLongs(String key);

    Long[] getLongObjects(String key);

    Long getLongObject(String key);

    Long getLongObject(String key, Long defaultValue);

    /*
     * Float
     */
    
    float getFloat(String key, float defaultValue);

    float getFloat(String key);

    float[] getFloats(String key);

    Float getFloatObject(String key, Float defaultValue);

    Float getFloatObject(String key);

    Float[] getFloatObjects(String key);

    /*
     * Double
     */
    
    double getDouble(String key);

    double getDouble(String key, double defaultValue);

    double[] getDoubles(String key);

    Double getDoubleObject(String key, Double defaultValue);

    Double getDoubleObject(String key);

    Double[] getDoubleObjects(String key);

    /*
     * BigDecimal
     */
    
    BigDecimal getBigDecimal(String key);

    BigDecimal getBigDecimal(String key, BigDecimal defaultValue);

    BigDecimal[] getBigDecimals(String key);
    
    /*
     * Date
     */
    Date getDate(String key);
    
    Date getDate(String key, String formatPattern);

    Date getDate(String key, Format format);

    Date getDate(String key, Format format, Date defaultValue);
    
    Date getDate(String key, String formatPattern, Date defaultValue);

    /*
     * Object
     */
    
    Object getObject(String key);

    Object[] getObjects(String key);

}