package com.example.first.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class BrowseHelper {

    
    private static final String SEARCH = 
        "https://www.google.com.hk/search?q=";
    private static final int HTTP_STATUS_OK = 200;
    private static byte[] buff = new byte[1024];
    
    public static class ApiException extends Exception {
        public ApiException (String msg)
        {
            super (msg);
        }

        public ApiException (String msg, Throwable thr)
        {
            super (msg, thr);
        }
    }
    
    public static synchronized String downloadFromServer (String parm) throws ClientProtocolException, IOException
        
    {
    	
    	
    	parm = URLEncoder.encode(parm, "gb2312");  
        String url="http://www.baidu.com.cn/s?wd=" + parm + "&cl=3";  
        //String url = SEARCH + parm;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
/*            if (status.getStatusCode() != HTTP_STATUS_OK)
                throw new ApiException("Invalid response from search!" + 
                        status.toString());*/
            HttpEntity entity = response.getEntity();
            InputStream ist = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            int readCount = 0;
            while ((readCount = ist.read(buff)) != -1)
                content.write(buff, 0, readCount);
            return new String (content.toByteArray());
 

    }
}
