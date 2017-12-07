package com.hervey.bos.dao;

import com.hervey.bos.entity.base.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 区域dao层
 * Created on 2017/12/6.
 *
 * @author hervey
 */
public interface AreaRepository extends JpaRepository<Area, Integer>, JpaSpecificationExecutor<Area> {

}
