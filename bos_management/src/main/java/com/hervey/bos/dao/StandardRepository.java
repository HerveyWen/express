package com.hervey.bos.dao;

import com.hervey.bos.entity.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on 2017/12/5.
 *
 * @author hervey
 */
public interface StandardRepository extends JpaRepository<Standard, Integer> {
    List<Standard> findByName(String name);
}
