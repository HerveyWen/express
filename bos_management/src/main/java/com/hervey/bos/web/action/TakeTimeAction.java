package com.hervey.bos.web.action;

import com.hervey.bos.entity.base.TakeTime;
import com.hervey.bos.service.TakeTimeService;
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
 * 收派时间action
 * <p>
 * Created on 2017/12/11.
 *
 * @author hervey
 */

@Controller
@Scope(value = "prototype")
@ParentPackage(value = "json-default")
@Namespace("/")
@Actions
public class TakeTimeAction extends BaseAction<TakeTime> {


    @Autowired
    private TakeTimeService takeTimeService;

    /**
     * 查询所有收派时间
     *
     * @return List<TakeTime> -> json
     * @throws Exception e
     */
    @Action(value = "taketime_findAll", results = {@Result(type = "json")})
    public String findAll() throws Exception {

        List<TakeTime> takeTimes = takeTimeService.findAll();
        ActionContext.getContext().getValueStack().push(takeTimes);
        return SUCCESS;
    }

    /**
     * 分页查询
     *
     * @return take_time.html
     * @throws Exception e
     */
    @Action(value = "taketime_page", results = {@Result(type = "json")})
    public String page() throws Exception {
        Pageable pageable = new PageRequest(page - 1, rows);

        Page<TakeTime> takeTimes = takeTimeService.findPage(pageable);
        pushPageDataToValueStack(takeTimes);
        return SUCCESS;
    }


    /**
     * 保存一条收派时间记录
     *
     * @return take_time.html
     * @throws Exception e
     */
    @Action(value = "taketime_save", results = {@Result(type = "redirect", location = "/pages/base/take_time.html")})
    public String save() throws Exception {
        takeTimeService.save(model);
        return SUCCESS;
    }
}
