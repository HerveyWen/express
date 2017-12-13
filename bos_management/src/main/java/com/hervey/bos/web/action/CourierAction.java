package com.hervey.bos.web.action;

import com.hervey.bos.entity.base.Courier;
import com.hervey.bos.entity.base.Standard;
import com.hervey.bos.service.CourierService;
import com.hervey.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/12/6.
 *
 * @author hervey
 */
@ParentPackage("json-default")
@Namespace("/")
@Actions
@Controller
@Scope(value = "prototype")
public class CourierAction extends BaseAction<Courier> {

    //快递员服务层注入
    @Autowired
    private CourierService courierService;


    /**
     * 添加快递员信息
     *
     * @return 结果集
     * @throws Exception e
     */
    @Action(value = "courier_save", results = {@Result(type = "redirect", location = "/pages/base/courier.html")})
    public String save() throws Exception {
        courierService.save(model);
        return SUCCESS;
    }


    /**
     * 带有查询条件的分页查询
     *
     * @return List<Courier> -> json
     * @throws Exception e
     */
    @Action(value = "courier_page", results = {@Result(type = "json")})
    public String page() throws Exception {
        // 调用业务层 ，查询数据结果
        Pageable pageable = new PageRequest(page - 1, rows);

        // 定义Specification
        Specification<Courier> specification = new Specification<Courier>() {
            // 完成的条件查询

            /**
             * 传递：
             * 		Root<Courier> root：（连接语句的时候需要字段，获取字段的名称）代表Criteria查询的根对象，Criteria查询的查询根定义了实体类型，能为将来导航获得想要的结果，它与SQL查询中的FROM子句类似
             * 		CriteriaQuery<?> query： （简单的查询可以使用CriteriaQuery()代表一个specific的顶层查询对象，它包含着查询的各个部分，比如：select 、from、where、group by、order by等
             * 		CriteriaBuilder cb：（复杂的查询可以使用CriteriaBuilder构建）用来构建CritiaQuery的构建器对象
             * 返回：Predicate：封装查询条件
             *
             */

            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = null;
                // 两张表的联合查询（快递员表和收派标准表）
                // list表示用来封装多个查询条件
                List<Predicate> list = new ArrayList<>();

                // 快递员工号
                if (StringUtils.isNotBlank(model.getCourierNum())) {
                    // 相当于条件：courierNum = gh001;
                    Predicate p = cb.equal(root.get("courierNum").as(String.class), model.getCourierNum());
                    list.add(p);
                }

                //快递员所属单位
                if (StringUtils.isNotBlank(model.getCompany())) {
                    // 相当于条件：company like %北京%;
                    Predicate p = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
                    list.add(p);
                }

                // 快递员类型
                if (StringUtils.isNotBlank(model.getType())) {
                    // 相当于条件：type like %小件员%;
                    Predicate p = cb.like(root.get("type").as(String.class), "%" + model.getType() + "%");
                    list.add(p);
                }

                // 收派标准（多表）
                if (model.getStandard() != null && model.getStandard().getId() != null) {
                    // 指定连接
                    Join<Courier, Standard> join = root.join("standard", JoinType.INNER);// 相当于inner join t_courier c on
                    Predicate p = cb.equal(join.get("id").as(Integer.class), model.getStandard().getId());
                    list.add(p);
                }

                // 构建条件的组合
                if (list.size() > 0) {
                    Predicate[] predicates = new Predicate[list.size()]; // 构建一个数组，长度条件的长度
                    predicate = cb.and(list.toArray(predicates));// 将查询条件从集合中的值转成数组中的值

                }
                return predicate;

                // 或者使用CriteriaQuery
                // 把Predicate应用到CriteriaQuery中去,因为还可以给CriteriaQuery添加其他的功能，比如排序、分组啥的
                //if(list!=null && list.size()>0){
                //    Predicate [] p = new Predicate[list.size()];
                //    query.where(list.toArray(p));
                //}
                //
                //query.orderBy(cb.desc(root.get("id").as(Integer.class)));
                //return query.getRestriction();

            }
        };

        Page<Courier> courierPage = courierService.findPageByCondition(pageable, specification);
        pushPageDataToValueStack(courierPage);
        return SUCCESS;
    }

    //属性驱动接收，所删除的快递员id（格式：1,2,3）
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     * 快递员假删除
     *
     * @return success
     * @throws Exception e
     */
    @Action(value = "courier_delete", results = {@Result(type = "redirect", location = "/pages/base/courier.html")})
    public String delete() throws Exception {
        if (StringUtils.isNotBlank(ids)) {
            String[] idArr = ids.split(",");
            courierService.delBatch(idArr);
        }
        return SUCCESS;
    }

    /**
     * 快递员
     *
     * @return success
     * @throws Exception e
     */
    @Action(value = "courier_restore", results = {@Result(type = "redirect", location = "/pages/base/courier.html")})
    public String restore() throws Exception {
        if (StringUtils.isNotBlank(ids)) {
            String[] idArr = ids.split(",");
            courierService.restoreBatch(idArr);
        }
        return SUCCESS;
    }

    /**
     * 查询未关联定区的快递员
     *
     * @return List<Courier> -> json
     * @throws Exception e
     */
    @Action(value = "courier_findnoassociation", results = {@Result(type = "json")})
    public String findnoassociation() throws Exception {
        List<Courier> couriers = courierService.findNoAssociation();
        ActionContext.getContext().getValueStack().push(couriers);
        return SUCCESS;
    }

    // 分区ID
    private String fixedAreaId;

    public void setFixedAreaId(String fixedAreaId) {
        this.fixedAreaId = fixedAreaId;
    }

    /**
     * 查询指定分区关联的快递员 列表
     */
    @Action(value = "courier_findAssociationCourier", results = {@Result(type = "json")})
    public String findAssociationCourier() {
        // 调用业务层，查询关联快递员 列表
        List<Courier> couriers = courierService.findAssociationCourier(fixedAreaId);
        ActionContext.getContext().getValueStack().push(couriers);
        return SUCCESS;
    }


}
