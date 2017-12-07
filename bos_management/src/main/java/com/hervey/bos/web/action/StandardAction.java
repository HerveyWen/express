package com.hervey.bos.web.action;

import com.hervey.bos.entity.base.Standard;
import com.hervey.bos.service.StandardService;
import com.hervey.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created on 2017/12/5.
 *
 * @author hervey
 */

@ParentPackage("json-default")
@Namespace("/")
@Actions
@Controller
@Scope(value = "prototype")
public class StandardAction extends BaseAction<Standard> {


    //业务处理对象
    @Autowired
    private StandardService standardService;

    /**
     * 添加取派标准
     *
     * @return 结果集状态码
     * @throws Exception e
     */
    @Action(value = "standard_save", results = {@Result(type = "redirect", location = "/pages/base/standard.html")})
    public String save() throws Exception {
        standardService.save(model);
        return SUCCESS;
    }

    /**
     * 校验取派标准名称
     *
     * @return success
     * @throws Exception e
     */
    @Action(value = "standard_validate", results = {@Result(type = "json")})
    public String validateName() throws Exception {
        List<Standard> standards = standardService.findByName(model.getName());
        if (standards != null && standards.size() > 0) {
            ActionContext.getContext().getValueStack().push(false);
        } else {
            ActionContext.getContext().getValueStack().push(true);
        }
        return SUCCESS;
    }

    /**
     * 分页查询
     *
     * @return success
     */
    @Action(value = "standard_page", results = {@Result(type = "json")})
    public String page() throws Exception {
        // 调用业务层 ，查询数据结果
        Pageable pageable = new PageRequest(page - 1, rows);//page从0开始
        Page<Standard> page = standardService.findPageData(pageable);
        pushPageDataToValueStack(page);
        return SUCCESS;
    }

    /**
     * 查询所有
     *
     * @return success
     * @throws Exception e
     */
    @Action(value = "standard_findAll", results = {@Result(type = "json")})
    public String findAll() throws Exception {
        List<Standard> standards = standardService.findAll();
        ActionContext.getContext().getValueStack().push(standards);
        return SUCCESS;
    }

    //接收参数ids
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     * 批量删除
     *
     * @return success
     * @throws Exception e
     */
    @Action(value = "standard_deleteByIds", results = {@Result(type = "redirect", location = "/pages/base/standard.html")})
    public String deleteByIds() throws Exception {
        //将ids("1,2,3,4")拆分成字符串数组
        String[] arrId = ids.split(",");

        standardService.deleteByIds(arrId);

        return SUCCESS;
    }
}
