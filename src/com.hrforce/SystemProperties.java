package com.hrforce;

import com.sun.jersey.api.client.Client;

import java.io.*;
import java.net.URL;
import java.util.Properties;


public class SystemProperties {
    private static String FILE_NAME = "/system.properties"; //文件名
    private static final Properties conf_PROPERTIES;
    static
    {
        conf_PROPERTIES = new Properties();
        //InputStream stream = SystemProperties.class.getResourceAsStream(FILE_NAME);
        InputStreamReader streamReader;
        try {
            streamReader = new InputStreamReader(Client.class.getClassLoader().getResourceAsStream(FILE_NAME), "UTF-8");
            {
                try {
                    conf_PROPERTIES.load( streamReader );
                }
                catch ( Exception e ) {
                }
                finally {
                    try {
                        streamReader.close();
                    }
                    catch ( IOException ioe ) {
                    }
                }
            }
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    public static Properties getProperties()
    {
        return conf_PROPERTIES;
    }
    public static void save()
    {
        try {
            URL url = SystemProperties.class.getResource( FILE_NAME);
            String filePath = java.net.URLDecoder.decode( url.getFile(), "utf-8" );
            FileOutputStream fout = new FileOutputStream( filePath );
            DataOutputStream out = new DataOutputStream( fout );
            conf_PROPERTIES.store( out, "系统保存" );
        }
        catch ( FileNotFoundException ex ) {
        }
        catch ( UnsupportedEncodingException ex ) {
        }
        catch ( IOException ex ) {
        }
    }
    public static String get( String key )
    {
        return conf_PROPERTIES.getProperty( key );
    }
    public static int getIntValue( String key )
    {
        return getIntValue( key, 0 );
    }
    public static int getIntValue( String key, int defaultValue )
    {
        String str = conf_PROPERTIES.getProperty( key );
        int value = 0;
        try {
            value = Integer.parseInt( str );
        }
        catch ( NumberFormatException ex ) {
            value = defaultValue;
        }
        return value;
    }
}
