package com.hervey.bos.service.impl;

import com.hervey.bos.dao.CourierRepository;
import com.hervey.bos.dao.FixedAreaRepository;
import com.hervey.bos.dao.TakeTimeRepository;
import com.hervey.bos.entity.base.Courier;
import com.hervey.bos.entity.base.FixedArea;
import com.hervey.bos.entity.base.TakeTime;
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
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private TakeTimeRepository takeTimeRepository;

    @Override
    public void save(FixedArea model) {
        fixedAreaRepository.save(model);
    }

    @Override
    public Page<FixedArea> findPageByCondition(Specification<FixedArea> specification, Pageable page) {
        return fixedAreaRepository.findAll(specification, page);
    }

    /**
     * 关联快递员 到定区
     *
     * @param id         定区id
     * @param courierId  快递员id
     * @param takeTimeId 收派时间id
     */
    @Override
    public void associationCourierToFixedArea(String id, Integer courierId, Integer takeTimeId) {
        FixedArea fixedArea = fixedAreaRepository.findOne(id);
        Courier courier = courierRepository.findOne(courierId);
        TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);

        // 快递员 关联到 定区上（多对多的数据库结构，由定区一端维护关联关系）
        fixedArea.getCouriers().add(courier);
        // 将收派标准 关联到 快递员上（一对多的数据库结构，由快递员一端维护关联关系）
        courier.setTakeTime(takeTime);

        /**快照更新*/
    }
}
