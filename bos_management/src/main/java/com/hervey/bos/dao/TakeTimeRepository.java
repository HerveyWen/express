package com.hervey.bos.dao;

import com.hervey.bos.entity.base.TakeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 收派时间dao
 * Created on 2017/12/11.
 *
 * @author hervey
 */
public interface TakeTimeRepository extends JpaRepository<TakeTime, Integer>, JpaSpecificationExecutor<TakeTime> {
}
