package com.hervey.bos.service;

import com.hervey.bos.entity.base.TakeTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created on 2017/12/11.
 *
 * @author hervey
 */
public interface TakeTimeService {
    List<TakeTime> findAll();

    void save(TakeTime model);

    Page<TakeTime> findPage(Pageable pageable);
}
