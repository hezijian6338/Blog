package com.my.blog.website.controller.admin;


import com.my.blog.website.exception.TipException;
import com.my.blog.website.model.Bo.RestResponseBo;
import com.my.blog.website.model.Vo.UserVo;
import com.my.blog.website.service.IUserService;
import com.my.blog.website.utils.DateKit;
import com.my.blog.website.utils.EmailUtils;
import com.my.blog.website.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Macbook on 2018/7/11.
 */
@Controller("forgetController")
@RequestMapping("/forget")
public class ForgetController {

    @Resource
    private IUserService userService;

    @GetMapping(value = "")
    public String forget(HttpServletRequest request) {
        request.setAttribute("state", "forget");
        return "admin/forget";
    }

    @GetMapping(value = "/modify")
    public String modify(HttpServletRequest request){
        request.setAttribute("state", "modify");
        return "admin/modify";
    }

    @PostMapping(value = "find")
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

    @PostMapping(value = "modifyPassWord")
    @ResponseBody
    public RestResponseBo modifyPassWord(@RequestParam String oldPassword, @RequestParam String password, HttpServletRequest request,HttpSession session){

        return RestResponseBo.ok();
    }
}
