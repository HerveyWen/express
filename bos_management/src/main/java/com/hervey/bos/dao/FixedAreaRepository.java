package com.hervey.bos.dao;

import com.hervey.bos.entity.base.FixedArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FixedAreaRepository extends JpaRepository<FixedArea, String>, JpaSpecificationExecutor<FixedArea> {


}
