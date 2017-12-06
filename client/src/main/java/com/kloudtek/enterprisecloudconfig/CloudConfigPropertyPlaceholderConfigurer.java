package com.kloudtek.enterprisecloudconfig;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

public class CloudConfigPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private String application;
    private String profile;
    private String label;
    private String url;
    private Properties properties;

    public CloudConfigPropertyPlaceholderConfigurer() {
    }

    public CloudConfigPropertyPlaceholderConfigurer(String application, String profile, String url) {
        this( application, profile, null, url);
    }

    public CloudConfigPropertyPlaceholderConfigurer(String application, String profile, String label, String url) {
        this.application = application;
        this.profile = profile;
        this.label = label;
        this.url = url;
    }

    private void load() throws IOException {
        CloudConfigClient ccClient = new CloudConfigClient(url);
        properties = ccClient.getProperties(application, profile, label);
    }

    @Override
    protected String resolvePlaceholder(String placeholder, Properties props) {
        if( properties == null ) {
            try {
                load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if( properties.containsKey(placeholder) ) {
            return properties.getProperty(placeholder);
        } else {
            return super.resolvePlaceholder(placeholder, props);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
