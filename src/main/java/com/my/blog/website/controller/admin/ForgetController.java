package com.my.blog.website.controller.admin;


import com.my.blog.website.exception.TipException;
import com.my.blog.website.model.Bo.RestResponseBo;
import com.my.blog.website.model.Vo.UserVo;
import com.my.blog.website.service.IUserService;
import com.my.blog.website.utils.EmailUtils;
import com.my.blog.website.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Macbook on 2018/7/11.
 */
@Controller("forgetController")
@RequestMapping("/pwd")
public class ForgetController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForgetController.class);

    @Resource
    private IUserService userService;

    @GetMapping(value = "")
    public String forget(HttpServletRequest request) {
        request.setAttribute("state", "forget");
        return "admin/pwd";
    }

    @GetMapping(value = "/newPWD/{email}")
    public String modify(HttpServletRequest request, @PathVariable String email){
        HttpSession session = request.getSession();
        session.setAttribute("email", email);
        return "admin/newPWD";
    }

    @PostMapping(value = "oldPWD")
    @ResponseBody
    public RestResponseBo findPassword(HttpServletRequest request, HttpSession session, @RequestParam String username, @RequestParam String email) {
        if(StringUtils.isBlank(email) || StringUtils.isBlank(username)){
            return RestResponseBo.fail("请确认信息输入完整");
        }
        if(!(TaleUtils.isEmail(email))){
            return RestResponseBo.fail("请输入正确的邮箱");
        }
        try {
            userService.findPassword(username,email);
            EmailUtils.WriteLetter(email);
            return RestResponseBo.ok();
        } catch (Exception e){
            String msg = "找回密码失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            }
            return RestResponseBo.fail(msg);
        }
    }

    @PostMapping(value = "newPassWord")
    @ResponseBody
    public RestResponseBo modifyPassWord(@RequestParam String idAccount,@RequestParam String password1,@RequestParam String password2,HttpServletRequest request,HttpSession session){
        if(!(password1.equals(password2))){
            return RestResponseBo.fail("请输入相同的密码");
        }
        if (password1.length() < 6 || password1.length() > 14) {
            return RestResponseBo.fail("请输入6-14位密码");
        }
        if(password1.isEmpty() || idAccount.isEmpty()){
            return RestResponseBo.fail("密码或者账户不能为空");
        }
        try{
            String email = (String) session.getAttribute("email");
            email = email + ".com";
            UserVo userVo = userService.findPassword(idAccount,email);
            String pwd = TaleUtils.MD5encode(idAccount + password1);
            userVo.setPassword(pwd);
            userService.updateByUid(userVo);
            return RestResponseBo.ok();
        }catch(Exception e){
            String msg = "密码修改失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
    }
}
