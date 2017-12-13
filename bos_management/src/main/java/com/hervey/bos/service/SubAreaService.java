package com.hervey.bos.service;

import com.hervey.bos.entity.base.SubArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created on 2017/12/11.
 *
 * @author hervey
 */
public interface SubAreaService {


    void saveSubAreas(List<SubArea> subAreas);

    Page<SubArea> findPage(Specification<SubArea> specification, Pageable pageable);
}
