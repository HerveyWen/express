package com.hervey.bos.service.impl;

import com.hervey.bos.dao.TakeTimeRepository;
import com.hervey.bos.entity.base.TakeTime;
import com.hervey.bos.service.TakeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收派时间service
 * <p>
 * Created on 2017/12/11.
 *
 * @author hervey
 */
@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {

    @Autowired
    private TakeTimeRepository takeTimeRepository;


    /**
     * 查询所有收派时间
     *
     * @return 收派时间列表
     */
    @Transactional(readOnly = true)
    @Override
    public List<TakeTime> findAll() {
        return takeTimeRepository.findAll();
    }

    /**
     * 添加一行收派时间记录
     *
     * @param model taketime
     */
    @Override
    public void save(TakeTime model) {
        takeTimeRepository.save(model);
    }

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 一页数据
     */
    @Override
    public Page<TakeTime> findPage(Pageable pageable) {
        return takeTimeRepository.findAll(pageable);
    }
}
