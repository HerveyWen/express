package com.hervey.bos.web.action;

import com.hervey.bos.constant.Constants;
import com.hervey.bos.utils.MailUtils;
import com.hervey.bos.utils.SmsDemoUtils;
import com.hervey.bos.web.action.common.BaseAction;
import com.hervey.crm.entity.Customer;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户action
 * Created on 2017/12/13.
 *
 * @author hervey
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller("customerAction")
@Scope(value = "prototype")
@Actions
public class CustomerAction extends BaseAction<Customer> {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * @return json
     */
    @Action(value = "customer_sendSms", results = {@Result(type = "json")})
    public String sendSms() {
        // 手机号保存在Customer对象
        // 生成短信验证码（RandomStringUtils.randomNumeric(4);）
        String randomCode = SmsDemoUtils.getNumber();
        // 将短信验证码 保存到session
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute(model.getTelephone(), randomCode);
        session.setMaxInactiveInterval(60);//保存在session中60秒

        Map<String, Object> map = new HashMap<>();
        /**阿里大鱼*/
        try {
            //发送短信验证码
            //SendSmsResponse sendSmsResponse = SmsDemoUtils.sendSms(model.getTelephone(), randomCode);
            //获取是否发送成功状态码
            //String smsCode = sendSmsResponse.getCode();
            String smsCode = "OK";
            // 表示发送成功
            if ("OK".equals(smsCode)) {
                map.put("success", true);
                map.put("telephone", model.getTelephone());
                map.put("checkcode", randomCode);
                map.put("msg", "验证码短信发送成功，清注意查收");
            } else {
                map.put("success", false);
                map.put("telephone", model.getTelephone());
                map.put("checkcode", null);
                map.put("msg", "验证码短信发送失败，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("telephone", model.getTelephone());
            map.put("checkcode", null);
            map.put("msg", "验证码短信发送异常，请稍后再试");
        }
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }


    //获取的验证码参数
    private String checkCode;

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    /**
     * 注册
     *
     * @return success(signup-success.html) | input(/signup.html)
     */
    @Action(value = "customer_register",
            results = {
                    @Result(type = "redirect", location = "/signup-success.html"),
                    @Result(name = "input", type = "redirect", location = "/signup.html")})
    public String register() {
        // 先校验短信验证码，如果不通过，调回注册页面
        // 从session获取 之前生成验证码
        String code = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());

        if (StringUtils.isBlank(code) || !code.equals(checkCode)) {
            System.out.println("短信验证码错误...");
            // 短信验证码错误
            return INPUT;
        }
        WebClient
                .create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/register")
                .type(MediaType.APPLICATION_JSON)
                .post(model);
        System.out.println("客户注册成功！");

        // 发送一封激活邮件
        // 生成激活码（32为的随机生成数字，建议不要生成特殊字符）
        String activecode = UUID.randomUUID().toString();
        // 将激活码保存到redis，设置24小时失效
        redisTemplate.opsForValue().set(model.getTelephone(), activecode, 24, TimeUnit.HOURS);

        // 调用MailUtils发送激活邮件
        // 传递电话和激活码的目的是：在激活的代码中和redis中的数据进行比对。

        String content = "尊敬的客户您好，请于24小时内，进行邮箱账户的绑定，点击下面地址完成绑定:<br/><a href='"
                + MailUtils.activeUrl + "?telephone=" + model.getTelephone()
                + "&activecode=" + activecode + "'>速运快递邮箱绑定地址</a>";
        MailUtils.sendMail("速运快递激活邮件", content, model.getEmail(), "");

        return SUCCESS;
    }

    // 属性驱动，邮件上传递的激活码
    private String activecode;

    public void setActivecode(String activecode) {
        this.activecode = activecode;
    }

    /**
     * 客户注册后邮箱绑定激活功能
     */
    @Action(value = "customer_activeMail")
    public String activeMail() throws Exception {

        // 判断激活码是否有效
        String code = redisTemplate.opsForValue().get(model.getTelephone());
        System.out.println(code);

        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        if (StringUtils.isNotBlank(code) && code.equals(activecode)) {
            // 激活码有效
            // 判断是否重复绑定，即T_CUSTOMER的TYPE字段是否为1
            // 调用CRM webService 查询客户信息，判断是否已经绑定

            Customer customer = WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/customer/telephone/" + model.getTelephone())
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON).get(Customer.class);
            if (customer != null && (customer.getType() == null || customer.getType() != 1)) {
                WebClient.create(Constants.CRM_MANAGEMENT_URL)
                        .path("/services/customerService/customer/updatetype/" + model.getTelephone())
                        .type(MediaType.APPLICATION_JSON)
                        .put(null);

                ServletActionContext.getResponse().getWriter().println("邮箱绑定成功！");
            } else {
                // 已经绑定过
                ServletActionContext.getResponse().getWriter().println("邮箱已经绑定过，无需重复绑定！");
            }
            // 删除redis的激活码
            redisTemplate.delete(model.getTelephone());

        } else {
            // 激活码无效
            ServletActionContext.getResponse().getWriter().println("激活码无效，请登录系统，重新绑定邮箱！");
        }
        return NONE;
    }


}
