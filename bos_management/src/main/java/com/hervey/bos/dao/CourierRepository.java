package com.hervey.bos.dao;

import com.hervey.bos.entity.base.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 快递员dao层
 * Created on 2017/12/6.
 *
 * @author hervey
 */
public interface CourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {

    //定义JPQL语法
    @Query(value = "update Courier set deltag = '1' where id = ?1")
    @Modifying
    void deleteCourier(Integer id);

    @Query(value = "update Courier set deltag = '' where id = ?1")
    @Modifying
    void restoreCourier(Integer id);
}
