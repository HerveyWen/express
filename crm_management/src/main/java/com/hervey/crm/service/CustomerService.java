package com.hervey.crm.service;

import com.hervey.crm.entity.Customer;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created on 2017/12/9.
 *
 * @author hervey
 */
public interface CustomerService {

    // 查询所有未关联客户列表
    @Path("/noassociationcustomers")
    @GET
    @Produces({"application/xml", "application/json"})
    List<Customer> findNoAssociationCustomers();


    // 已经关联到指定定区的客户列表
    @Path("/associationfixedareacustomers/{fixedareaid}")
    @GET
    @Consumes(value = {"application/json", "application/xml"})
    @Produces({"application/xml", "application/json"})
    List<Customer> findHasAssociationFixedAreaCustomers(@PathParam("fixedareaid") String fixedAreaId);

    // 将客户关联到定区上 ， 将所有客户id 拼成字符串 1,2,3，使用@QueryParam实现
    @Path("/associationcustomerstofixedarea")
    @PUT
    @Consumes(value = {"application/json", "application/xml"})
    void associationCustomersToFixedArea(@QueryParam("customerIdStr") String customerIdStr, @QueryParam("fixedAreaId") String fixedAreaId);

    //注册
    @Path("/register")
    @POST
    @Consumes(value = {"application/json", "application/xml"})
    void register(Customer customer);


    //使用电话号查询唯一的客户对象
    @Path("/customer/telephone/{telephone}")
    @GET
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Customer findByTelephone(@PathParam("telephone") String telephone);

    //使用电话号作为条件，更新type的值为1，表示激活
    @Path("/customer/updatetype/{telephone}")
    @PUT
    @Consumes({"application/xml", "application/json"})
    public void updateType(@PathParam("telephone") String telephone);

}
