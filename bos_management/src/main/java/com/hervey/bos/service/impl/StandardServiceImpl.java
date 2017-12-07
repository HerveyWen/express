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
    @Transactional(readOnly = true)
    @Override
    public List<Standard> findByName(String name) {
        return standardRepository.findByName(name);
    }

    /**
     * 分页查询
     *
     * @param pageable 封装好的分页
     * @return 某一页的数据
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Standard> findPageData(Pageable pageable) {
        return standardRepository.findAll(pageable);
    }

    /**
     * 查询所有
     *
     * @return 取派标准List
     */
    @Transactional(readOnly = true)
    @Override
    public List<Standard> findAll() {
        return standardRepository.findAll();
    }

    /**
     * 删除一条或多条记录
     *
     * @param arrId 一条或多条记录的id
     */
    @Override
    public void deleteByIds(String[] arrId) {

        if (arrId != null && arrId.length > 0)
            for (int i = 0; i < arrId.length; i++) {
                standardRepository.delete(Integer.parseInt(arrId[i]));
            }
    }


}
