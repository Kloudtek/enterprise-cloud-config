package com.kloudtek.enterprisecloudconfig;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import javax.annotation.PostConstruct;
import java.util.Properties;

public class CloudConfigPreferencesPlaceholderConfigurator extends PropertyPlaceholderConfigurer {
    private String url;

    public CloudConfigPreferencesPlaceholderConfigurator() {
    }

    public CloudConfigPreferencesPlaceholderConfigurator(String url) {
        this.url = url;
    }

    @PostConstruct
    public void init() {

    }

    @Override
    protected String resolvePlaceholder(String placeholder, Properties props) {
        return super.resolvePlaceholder(placeholder, props);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
