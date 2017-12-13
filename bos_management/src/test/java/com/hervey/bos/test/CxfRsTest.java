package com.hervey.bos.test;

import com.hervey.bos.constant.Constants;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

/**
 * Created on 2017/12/10.
 *
 * @author hervey
 */
public class CxfRsTest {

    @Test
    public void test1() {
        WebClient.create(Constants.CRM_MANAGEMENT_URL + "services/customerService/associationcustomerstofixedarea?customerIdStr=" + "1,3,4" + "&fixedAreaId=5")
                .type(MediaType.APPLICATION_JSON)
                .put("aa");
    }
}
