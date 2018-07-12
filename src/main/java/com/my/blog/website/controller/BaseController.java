package com.my.blog.website.controller;

import com.my.blog.website.model.Vo.ContentVo;
import com.my.blog.website.model.Vo.UserVo;
import com.my.blog.website.service.IUserService;
import com.my.blog.website.utils.TaleUtils;
import com.my.blog.website.utils.MapCache;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 13 on 2017/2/21.
 */
public abstract class BaseController {

    @Resource
    private IUserService userService;

    public static String THEME = "themes/default";

    protected MapCache cache = MapCache.single();

    /**
     * 主页的页面主题
     * @param viewName
     * @return
     */
    public String render(String viewName) {
        return THEME + "/" + viewName;
    }

    public BaseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    public BaseController state(HttpServletRequest request, String state) {
        request.setAttribute("state", state);
        return this;
    }

    public BaseController author(HttpServletRequest request, String author) {
        request.setAttribute("author", author);
        return this;
    }

    public BaseController authorId(HttpServletRequest request, Integer authorId) {
        request.setAttribute("authorId", authorId);
        return this;
    }

    public UserVo getUserById(Integer authorId) {
        UserVo user = userService.queryUserById(authorId);
        return user;
    }

    public BaseController pageInfo(HttpServletRequest request, List<ContentVo> pageInfo) {
        request.setAttribute("pageInfo", pageInfo);
        return this;
    }

    public BaseController keywords(HttpServletRequest request, String keywords) {
        request.setAttribute("keywords", keywords);
        return this;
    }

    /**
     * 获取请求绑定的登录对象
     * @param request
     * @return
     */
    public UserVo user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }

    public Integer getUid(HttpServletRequest request){
        return this.user(request).getUid();
    }

    public String render_404() {
        return "comm/error_404";
    }

}
