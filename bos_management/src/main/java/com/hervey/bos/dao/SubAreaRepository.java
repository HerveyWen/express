package com.hervey.bos.dao;

import com.hervey.bos.entity.base.SubArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 分区dao
 * <p>
 * Created on 2017/12/11.
 *
 * @author hervey
 */
public interface SubAreaRepository extends JpaRepository<SubArea, String>, JpaSpecificationExecutor<SubArea> {
}
