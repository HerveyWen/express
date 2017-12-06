package com.hervey.bos.web.action;

import com.hervey.bos.entity.base.Standard;
import com.hervey.bos.service.StandardService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {


    // 接收的取派标准实体参数
    private Standard standard = new Standard();

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
        System.out.println("--->" + standard);
        standardService.save(standard);
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
        List<Standard> standards = standardService.findByName(standard.getName());
        if (standards != null && standards.size() > 0) {
            ActionContext.getContext().getValueStack().push(false);
        } else {
            ActionContext.getContext().getValueStack().push(true);
        }
        return SUCCESS;
    }


    //属性驱动，获取当前页和当前页最多显示的记录数
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Action(value = "standard_page", results = {@Result(type = "json")})
    public String page() {
        // 调用业务层 ，查询数据结果
        Pageable pageable = new PageRequest(page - 1, rows);//page从0开始
        Page<Standard> page = standardService.findPageData(pageable);

        //返回客户端数据 需要 total 和 rows
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotalElements());
        map.put("rows", page.getContent());

        //将map转换为json数据返回 ，使用struts2-json-plugin 插件
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }

    /**
     * 接收取派标准参数
     *
     * @return 取派标准实体
     */
    @Override
    public Standard getModel() {
        return standard;
    }
}
