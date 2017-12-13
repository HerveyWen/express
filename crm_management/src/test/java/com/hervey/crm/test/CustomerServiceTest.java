package com.hervey.crm.test;

import com.hervey.crm.service.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created on 2017/12/9.
 *
 * @author hervey
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;


    @Test
    public void testFindNoAssociationCustomers() {
        System.out.println(customerService.findNoAssociationCustomers());
    }

    @Test
    public void testFindHasAssociationFixedAreaCustomers() {
        System.out.println(customerService
                .findHasAssociationFixedAreaCustomers("dq001"));
    }

    @Test
    public void testAssociationCustomersToFixedArea() {
        //customerService.associationCustomersToFixedArea("10001,10002", "dq001");
    }


}
