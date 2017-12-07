package com.hervey.bos.web.action.common;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.springframework.data.domain.Page;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2017/12/7.
 *
 * @author hervey
 */
public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

    // 模型驱动
    protected T model;

    @Override
    public T getModel() {
        return model;
    }


    // 构造器 完成model实例化
    public BaseAction() {
        // 构造子类Action对象 ，获取继承父类型的泛型，然后实例化
        // AreaAction extends BaseAction<Area>
        // 需要获取BaseAction<Area>中的Area，然后实例化
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        // 获取类型第一个泛型参数
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        try {
            model = modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("模型构造失败...");
        }
    }

    // 接收分页查询参数
    protected int page;
    protected int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    // 将分页查询结果数据，压入值栈的方法
    protected void pushPageDataToValueStack(Page<T> pageData) {

        //返回客户端数据 需要 total 和 rows
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());

        //将map转换为json数据返回 ，使用struts2-json-plugin 插件
        ActionContext.getContext().getValueStack().push(result);
    }


}
