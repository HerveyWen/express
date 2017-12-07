package com.hervey.bos.service;

import com.hervey.bos.entity.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List; /**
 * Created on 2017/12/7.
 *
 * @author hervey
 */
public interface AreaService {
    void saveAreas(List<Area> areas);

    Page<Area> findPageByCondition(Pageable pageable, Specification<Area> specification);
}
