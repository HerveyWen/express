package com.hervey.bos.service.impl;

import com.hervey.bos.dao.AreaRepository;
import com.hervey.bos.entity.base.Area;
import com.hervey.bos.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created on 2017/12/7.
 *
 * @author hervey
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {

    // 注入DAO
    @Autowired
    private AreaRepository areaRepository;

    /**
     * 批量保存
     *
     * @param areas area列表
     */
    @Override
    public void saveAreas(List<Area> areas) {
        areaRepository.save(areas);
    }

    /**
     * 条件分页查询
     *
     * @param pageable      分页参数
     * @param specification 条件参数
     * @return
     */
    @Override
    public Page<Area> findPageByCondition(Pageable pageable, Specification<Area> specification) {
        return areaRepository.findAll(specification, pageable);
    }
}
