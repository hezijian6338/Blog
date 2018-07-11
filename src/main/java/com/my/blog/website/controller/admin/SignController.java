package com.my.blog.website.controller.admin;


import com.my.blog.website.exception.TipException;
import com.my.blog.website.model.Bo.RestResponseBo;
import com.my.blog.website.model.Vo.UserVo;
import com.my.blog.website.service.IUserService;
import com.my.blog.website.utils.DateKit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Macbook on 2018/7/11.
 */
@Controller("adminSignController")
@RequestMapping("/sign")
public class SignController {

    @Resource
    private IUserService userService;

    @GetMapping(value = "")
    public String sign(HttpServletRequest request) {
        request.setAttribute("state", "sign");
        return "admin/sign";
    }

    @PostMapping(value = "new")
    @ResponseBody
    public RestResponseBo newUser(HttpServletRequest request, HttpSession session, @RequestParam String username, @RequestParam String password, @RequestParam String email) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            return RestResponseBo.fail("请确认信息输入完整");
        }
        if (password.length() < 6 || password.length() > 14) {
            return RestResponseBo.fail("请输入6-14位密码");
        }
        try {
            int time = DateKit.getCurrentUnixTime();
            UserVo temp = new UserVo();
            temp.setUsername(username);
            temp.setPassword(password);
            temp.setEmail(email);
            temp.setGroupName("visitor");
            temp.setCreated(time);
            temp.setScreenName(username);
            userService.insertUser(temp);

            return RestResponseBo.ok();
        } catch (Exception e){
            String msg = "后台注册失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            }
            return RestResponseBo.fail(msg);
        }
    }
}
