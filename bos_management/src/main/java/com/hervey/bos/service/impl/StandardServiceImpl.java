package com.hervey.bos.service.impl;

import com.hervey.bos.dao.StandardRepository;
import com.hervey.bos.entity.base.Standard;
import com.hervey.bos.service.StandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created on 2017/12/5.
 *
 * @author hervey
 */
@Service
@Transactional
public class StandardServiceImpl implements StandardService {


    /**
     * spring data jpa
     */
    @Autowired
    private StandardRepository standardRepository;

    /**
     * 添加取派标准
     *
     * @param standard 取派标准实体
     */
    @Override
    public void save(Standard standard) {
        standardRepository.save(standard);
    }

    /**
     * 根据名称查询取派标准列表
     *
     * @param name 取派标准名称
     * @return 取派标准列表
     */
    @Override
    public List<Standard> findByName(String name) {
        return standardRepository.findByName(name);
    }

    @Override
    public Page<Standard> findPageData(Pageable pageable) {
        return standardRepository.findAll(pageable);
    }
}
