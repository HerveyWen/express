package com.hervey.bos.web.action;

import com.hervey.bos.entity.base.FixedArea;
import com.hervey.bos.service.FixedAreaService;
import com.hervey.bos.web.action.common.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@ParentPackage(value = "json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {

    @Autowired
    private FixedAreaService fixedAreaService;

    @Action(value = "fixedArea_save", results = {@Result(type = "redirect", location = "/pages/base/fixed_area.html")})
    public String save() throws Exception {
        fixedAreaService.save(model);
        return SUCCESS;
    }

    /**
     * 带有查询条件的分页查询
     *
     * @return List -> json
     * @throws Exception e
     */
    @Action(value = "fixedArea_page", results = {@Result(type = "json")})
    public String page() throws Exception {
        Pageable pageable = new PageRequest(page - 1, rows);

        // 定义Specification
        Specification<FixedArea> specification = new Specification<FixedArea>() {
            @Override
            public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = null;
                // list表示用来封装多个查询条件
                List<Predicate> list = new ArrayList<>();
                if (StringUtils.isNotBlank(model.getId())) {
                    Predicate id = cb.equal(root.get("id").as(String.class), model.getId());
                    list.add(id);
                }

                if (StringUtils.isNotBlank(model.getCompany())) {
                    Predicate company = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
                    list.add(company);
                }
                // 构建条件的组合
                if (list.size() > 0) {
                    Predicate[] predicates = new Predicate[list.size()]; // 构建一个数组，长度条件的长度
                    predicate = cb.and(list.toArray(predicates));// 将查询条件从集合中的值转成数组中的值

                }
                return predicate;
            }
        };

        Page<FixedArea> pageData = fixedAreaService.findPageByCondition(specification, pageable);
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }


}
