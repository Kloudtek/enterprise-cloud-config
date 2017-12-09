package com.kloudtek.enterprisecloudconfig;

import com.fasterxml.jackson.jr.ob.JSON;
import com.kloudtek.util.URLBuilder;
import com.kloudtek.util.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("unchecked")
public class CloudConfigClient implements Closeable {
    private String url;
    private CloseableHttpClient httpClient;

    public CloudConfigClient(String url) {
        this.url = url;
        httpClient = HttpClients.createMinimal();
    }

    public void close() throws IOException {
        httpClient.close();
    }

    public Properties getProperties(String application, String profile) throws IOException {
        return getProperties(application, profile, null);
    }

    public Properties getProperties(String application, String profile, String label) throws IOException {
        Properties properties = new Properties();
        InputStream is = get(new URLBuilder(url).path("/" + application + "-" + profile + ".json").toUri());
        if (is == null) {
            return properties;
        }
        convertToProperties(null, JSON.std.anyFrom(is), properties);
        return properties;
    }

    private void convertToProperties(String key, Object value, Properties properties) {
        if (value instanceof Map) {
            for (Map.Entry<String, Object> e : ((Map<String, Object>) value).entrySet()) {
                convertToProperties(key != null ? key + "." + e.getKey() : e.getKey(), e.getValue(), properties);
            }
        } else if (value instanceof Collection) {
            int nb = 0;
            for (Object arrayObj : ((Collection) value)) {
                String arrKey = (key != null ? key : "") + "[" + nb + "]";
                convertToProperties(arrKey,arrayObj,properties);
                nb++;
            }
        } else if( value != null ){
            properties.put(key,value.toString());
        }
    }

    private InputStream get(URI uri) throws IOException {
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(get);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode > 299) {
            String message = "server returned status code " + statusCode;
            if (response.getEntity() != null) {
                message = message + ": " + IOUtils.toString(response.getEntity().getContent());
            }
            throw new IOException(message);
        }
        if (response.getEntity() != null) {
            return response.getEntity().getContent();
        } else {
            return null;
        }
    }
}
