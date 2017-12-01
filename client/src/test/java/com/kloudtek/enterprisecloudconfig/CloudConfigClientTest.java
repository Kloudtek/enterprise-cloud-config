package com.kloudtek.enterprisecloudconfig;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CloudConfigClientTest {
    public static final String ARRVAL1 = "froo~gogo~0/1";
    public static final String ARRVAL2 = "froo~gogo~1/1";
    @LocalServerPort
    private int port;

    @Test
    public void testGetConfig() throws IOException {
        CloudConfigClient client = new CloudConfigClient("http://localhost:" + port);
        Properties properties = client.getProperties("testclient", "prof", null);
        Assert.assertEquals(5,properties.size());
        Assert.assertEquals("cat",properties.getProperty("dog"));
        Assert.assertEquals("bar",properties.getProperty("foo"));
        Assert.assertEquals("gah",properties.getProperty("agh.bla"));
        Assert.assertNotNull(properties.getProperty("froo.gogo[0]"));
        boolean naturalOrder = properties.getProperty("froo.gogo[0]").equals(ARRVAL1) ;
        Assert.assertEquals(naturalOrder ? ARRVAL1: ARRVAL2, properties.getProperty("froo.gogo[0]"));
        Assert.assertEquals(naturalOrder ? ARRVAL2: ARRVAL1, properties.getProperty("froo.gogo[1]"));
    }
}