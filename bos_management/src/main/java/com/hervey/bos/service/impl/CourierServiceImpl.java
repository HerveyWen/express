package com.hervey.bos.service.impl;

import com.hervey.bos.dao.CourierRepository;
import com.hervey.bos.entity.base.Courier;
import com.hervey.bos.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 2017/12/6.
 *
 * @author hervey
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Courier> findPageData(Pageable pageable) {
        return courierRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Courier> findPageByCondition(Pageable pageable, Specification<Courier> specification) {
        return courierRepository.findAll(specification, pageable);
    }

    @Override
    public void delBatch(String[] idArr) {
        if (idArr != null && idArr.length > 0)
            for (String id : idArr) {
                courierRepository.deleteCourier(Integer.parseInt(id));
            }
    }

    @Override
    public void restoreBatch(String[] idArr) {
        if (idArr != null && idArr.length > 0)
            for (String id : idArr) {
                courierRepository.restoreCourier(Integer.parseInt(id));
            }
    }
}
