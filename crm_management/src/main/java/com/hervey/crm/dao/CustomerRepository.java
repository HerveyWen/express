package com.hervey.crm.dao;

import com.hervey.crm.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 客户dao层
 * Created on 2017/12/6.
 *
 * @author hervey
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {

    List<Customer> findByFixedAreaIdIsNull();

    List<Customer> findByFixedAreaId(String fixedAreaId);

    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?1")
    @Modifying
    void clearFixedAreaId(String fixedAreaId);

    @Query("update Customer set fixedAreaId = ?2 where id = ?1")
    @Modifying
    void updateFixedAreaId(Integer customerId, String fixedAreaId);

    Customer findByTelephone(String telephone);

    @Query("update Customer set type=1 where telephone= ?1")
    @Modifying
    void updateType(String telephone);
}
