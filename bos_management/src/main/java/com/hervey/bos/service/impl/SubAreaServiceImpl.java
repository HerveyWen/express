package com.hervey.bos.service.impl;

import com.hervey.bos.dao.SubAreaRepository;
import com.hervey.bos.entity.base.SubArea;
import com.hervey.bos.service.SubAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分区service实现
 * <p>
 * Created on 2017/12/11.
 *
 * @author hervey
 */
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

    @Autowired
    private SubAreaRepository subAreaRepository;


    /**
     * 批量保存分区
     *
     * @param subAreas 分区列表
     */
    @Override
    public void saveSubAreas(List<SubArea> subAreas) {
        subAreaRepository.save(subAreas);
    }

    /**
     * 条件分页查询
     *
     * @param pageable      分页参数
     * @param specification 查询条件
     * @return
     */
    @Override
    public Page<SubArea> findPage(Specification<SubArea> specification, Pageable pageable) {
        return subAreaRepository.findAll(specification, pageable);
    }

}
