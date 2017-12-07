package com.hervey.bos.service.impl;

import com.hervey.bos.dao.FixedAreaRepository;
import com.hervey.bos.entity.base.FixedArea;
import com.hervey.bos.service.FixedAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

    @Autowired
    private FixedAreaRepository fixedAreaRepository;

    @Override
    public void save(FixedArea model) {
        fixedAreaRepository.save(model);
    }

    @Override
    public Page<FixedArea> findPageByCondition(Specification<FixedArea> specification, Pageable page) {
        return fixedAreaRepository.findAll(specification, page);
    }
}
