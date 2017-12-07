package com.hervey.bos.service;

import com.hervey.bos.entity.base.FixedArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface FixedAreaService {


    void save(FixedArea model);

    Page<FixedArea> findPageByCondition(Specification<FixedArea> specification, Pageable page);
}
