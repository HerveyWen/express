package com.hervey.bos.service;

import com.hervey.bos.entity.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification; /**
 * Created on 2017/12/6.
 *
 * @author hervey
 */
public interface CourierService {
    void save(Courier courier);

    Page<Courier> findPageData(Pageable pageable);

    Page<Courier> findPageByCondition(Pageable pageable, Specification<Courier> specification);

    void delBatch(String[] idArr);

    void restoreBatch(String[] idArr);
}
