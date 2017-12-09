package com.kloudtek.enterprisecloudconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

public class CloudConfigPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements EnvironmentAware {
    private String application;
    private String label;
    private String url;
    private String profiles;
    private String defaultProfile = "default";
    private Properties properties;
    private Environment environment;

    public CloudConfigPropertyPlaceholderConfigurer() {
        setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_OVERRIDE);
    }

    public CloudConfigPropertyPlaceholderConfigurer(String application, String url) {
        this( application, null, url);
    }

    public CloudConfigPropertyPlaceholderConfigurer(String application, String label, String url) {
        this.application = application;
        this.label = label;
        this.url = url;
        setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_OVERRIDE);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    protected synchronized void load() throws IOException {
        if( properties == null ) {
            if( profiles == null ) {
                if( environment.getActiveProfiles().length > 0 ) {
                    profiles = String.join(",",environment.getActiveProfiles());
                } else {
                    profiles = defaultProfile;
                }
            }
            CloudConfigClient ccClient = new CloudConfigClient(url);
            properties = ccClient.getProperties(application, profiles, label);
        }
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getProfiles() {
        return profiles;
    }

    public void setProfiles(String profiles) {
        this.profiles = profiles;
    }

    public String getDefaultProfile() {
        return defaultProfile;
    }

    public void setDefaultProfile(String defaultProfile) {
        this.defaultProfile = defaultProfile;
    }
}
