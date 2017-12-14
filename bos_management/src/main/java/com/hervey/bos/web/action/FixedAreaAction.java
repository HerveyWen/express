package com.hervey.bos.web.action;

import com.hervey.bos.constant.Constants;
import com.hervey.bos.entity.base.FixedArea;
import com.hervey.bos.service.FixedAreaService;
import com.hervey.bos.web.action.common.BaseAction;
import com.hervey.crm.entity.Customer;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 定区action
 */

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


    /**
     * 查询未关联定区列表
     *
     * @return
     * @throws Exception
     */
    @Action(value = "fixedArea_findNoAssociationCustomers", results = {@Result(type = "json")})
    public String findNoAssociationCustomers() throws Exception {
        Collection<? extends Customer> customers = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/noassociationcustomers")
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(customers);
        return SUCCESS;
    }

    /**
     * 查询当前定区关联的客户
     *
     * @return
     * @throws Exception
     */
    @Action(value = "fixedArea_findHasAssociationCustomers", results = {@Result(type = "json")})
    public String findHasAssociationCustomers() throws Exception {

        //调用webservice cxf rs
        Collection<? extends Customer> customers = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/associationfixedareacustomers/" + model.getId())
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(customers);
        return SUCCESS;
    }


    private String[] customerIds;

    public void setCustomerIds(String[] customerIds) {
        this.customerIds = customerIds;
    }

    /**
     * 当前定区关联客户保存
     *
     * @return
     * @throws Exception
     */
    @Action(value = "fixedArea_associationCustomersToFixedArea", results = {@Result(type = "redirect", location = "/pages/base/fixed_area.html")})
    public String associationCustomersToFixedArea() throws Exception {
        String customerIds = StringUtils.join(this.customerIds, ",");//将数组转成字符串，以','分割
        String fixedAreaId = model.getId();
        System.out.println(customerIds + "--" + fixedAreaId);
        WebClient.create(
                Constants.CRM_MANAGEMENT_URL + "/services/customerService"
                        + "/associationcustomerstofixedarea?customerIdStr="
                        + customerIds + "&fixedAreaId=" + fixedAreaId).put(null);

        return SUCCESS;
    }

    //快递员id
    private Integer courierId;
    //收派时间id
    private Integer takeTimeId;

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    /**
     * 关联快递员 到定区
     *
     * @return fixed_area.html
     * @throws Exception e
     */
    @Action(value = "fixedArea_associationCourierToFixedArea", results = {@Result(type = "redirect", location = "/pages/base/fixed_area.html")})
    public String associationCourierToFixedArea() throws Exception {
        // 调用业务层， 定区关联快递员
        fixedAreaService.associationCourierToFixedArea(model.getId(), courierId, takeTimeId);
        return SUCCESS;
    }
}
