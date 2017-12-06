package com.hervey.bos.service;

import com.hervey.bos.entity.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created on 2017/12/5.
 *
 * @author hervey
 */
public interface StandardService {
    void save(Standard standard);

    List<Standard> findByName(String name);

    Page<Standard> findPageData(Pageable pageable);
}
