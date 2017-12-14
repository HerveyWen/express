package com.hervey.crm.service.impl;

import com.hervey.crm.dao.CustomerRepository;
import com.hervey.crm.entity.Customer;
import com.hervey.crm.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created on 2017/12/9.
 * <p>
 * 客户service层
 *
 * @author hervey
 */
@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    /**
     * 查询所有未关联客户列表，即字段fixedAreaId is null
     */
    @Transactional(readOnly = true)
    @Override
    public List<Customer> findNoAssociationCustomers() {
        return customerRepository.findByFixedAreaIdIsNull();
    }

    /**
     * 已经关联到指定定区的客户列表，即字段fixedAreaId = ?
     */
    @Transactional(readOnly = true)
    @Override
    public List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId) {
        System.out.println(fixedAreaId);
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }


    /**
     * 将客户关联到定区上 ， 将所有客户id 拼成字符串 1,2,3，使用客户ID更新客户，更新fixedAreaId字段
     */
    @Override
    public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {

        System.out.println("-->" + customerIdStr + " --> " + fixedAreaId);

        // 解除关联动作，将该定区ID的所有用户全部置null
        customerRepository.clearFixedAreaId(fixedAreaId);

        if (StringUtils.isAnyBlank(customerIdStr, fixedAreaId) || "null".equals(customerIdStr))
            return;
        String[] ids = customerIdStr.split(",");
        for (String id : ids) {
            customerRepository.updateFixedAreaId(Integer.parseInt(id), fixedAreaId);
        }


    }

    /**
     * 注册，添加用户到表中
     *
     * @param customer
     */
    @Override
    public void register(Customer customer) {
        customerRepository.save(customer);
    }

    /**
     * 使用电话号查询唯一的客户对象
     *
     * @param telephone telephone
     * @return customer
     */
    @Override
    public Customer findByTelephone(String telephone) {
        return customerRepository.findByTelephone(telephone);
    }

    /**
     * //使用电话号作为条件，更新type的值为1，表示激活
     *
     * @param telephone telephone
     */
    @Override
    public void updateType(String telephone) {
        System.out.println(telephone);
        customerRepository.updateType(telephone);
    }
}
