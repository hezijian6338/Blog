package com.my.blog.website.controller.admin;


import com.github.pagehelper.PageInfo;
import com.my.blog.website.constant.WebConst;
import com.my.blog.website.controller.BaseController;
import com.my.blog.website.dto.LogActions;
import com.my.blog.website.dto.Types;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.model.Bo.RestResponseBo;
import com.my.blog.website.model.Vo.ContentVo;
import com.my.blog.website.model.Vo.ContentVoExample;
import com.my.blog.website.model.Vo.MetaVo;
import com.my.blog.website.model.Vo.UserVo;
import com.my.blog.website.service.IContentService;
import com.my.blog.website.service.ILogService;
import com.my.blog.website.service.IMetaService;
import com.my.blog.website.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by 13 on 2017/2/21.
 */
@Controller
@RequestMapping("/admin/article")
@Transactional(rollbackFor = TipException.class)
public class ArticleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    @Resource
    private IContentService contentsService;

    @Resource
    private IMetaService metasService;

    @Resource
    private ILogService logService;

    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit, HttpServletRequest request) {

//        获得当前的session值
        HttpSession session = request.getSession();

//        从session里面获得当前登陆的信息(登陆者的id)
        Integer id = (Integer) session.getAttribute("userId");

//        创建一个简单对象类
        ContentVoExample contentVoExample = new ContentVoExample();

//        存放根据创建时间倒序的SQL语句
        contentVoExample.setOrderByClause("created desc");

        if(!(TaleUtils.getLoginUser(request).getGroupName().equals("admin"))) {
//        存放查找文章类型和根据用户id来筛选的SQL语句
            contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andAuthorIdEqualTo(id);

//        根据这个简单对象类的信息去调用service的执行方法,获取符合要求的文章的列表
            PageInfo<ContentVo> contentsPaginator = contentsService.getArticlesWithpage(contentVoExample, page, limit);

            //        把获取到的文章信息返回给前端
            request.setAttribute("articles", contentsPaginator);

        }else{
            contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType());

            PageInfo<ContentVo> contentsPaginator = contentsService.getArticlesWithpage(contentVoExample, page, limit);

            //        把获取到的文章信息返回给前端
            request.setAttribute("articles", contentsPaginator);
        }

//        定位前端的页面
        return "admin/article_list";
    }

    @GetMapping(value = "/publish")
    public String newArticle(HttpServletRequest request) {

//        定位到发布文章的页面,加载对应的控件
        List<MetaVo> categories = metasService.getMetas(Types.CATEGORY.getType());

//        返回给前端
        request.setAttribute("categories", categories);

//        定位到前端页面
        return "admin/article_edit";
    }

    @GetMapping(value = "/{cid}")
    public String editArticle(@PathVariable String cid, HttpServletRequest request) {
//        编辑具体某一篇文章⬆️

//        根据文章的id来获取这篇文章的具体信息
        ContentVo contents = contentsService.getContents(cid);

//        返回当前文章的具体信息给前端
        request.setAttribute("contents", contents);

//        分类信息
        List<MetaVo> categories = metasService.getMetas(Types.CATEGORY.getType());

//        返回参数给前端
        request.setAttribute("categories", categories);
        request.setAttribute("active", "article");

//        定向前端页面
        return "admin/article_edit";
    }

    @PostMapping(value = "/publish")
    @ResponseBody
    public RestResponseBo publishArticle(ContentVo contents, HttpServletRequest request) {
//        发布文章⬆️

//        获取当前用户对象
        UserVo users = this.user(request);

//        设置这篇文章的作者
        contents.setAuthorId(users.getUid());

//        设置文章的类型
        contents.setType(Types.ARTICLE.getType());

//        如果分类信息为空就填充为默认分类
        if (StringUtils.isBlank(contents.getCategories())) {
            contents.setCategories("默认分类");
        }

//        发布文章,并返回发布结果
        String result = contentsService.publish(contents);

//        判断结果是否正确发布
        if (!WebConst.SUCCESS_RESULT.equals(result)) {
            return RestResponseBo.fail(result);
        }

//        发布成功返回状态
        return RestResponseBo.ok();
    }

    @PostMapping(value = "/modify")
    @ResponseBody
    public RestResponseBo modifyArticle(ContentVo contents, HttpServletRequest request) {
        UserVo users = this.user(request);
        contents.setAuthorId(users.getUid());
        contents.setType(Types.ARTICLE.getType());
        String result = contentsService.updateArticle(contents);
        if (!WebConst.SUCCESS_RESULT.equals(result)) {
            return RestResponseBo.fail(result);
        }
        return RestResponseBo.ok();
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public RestResponseBo delete(@RequestParam int cid, HttpServletRequest request) {
        String result = contentsService.deleteByCid(cid);
        logService.insertLog(LogActions.DEL_ARTICLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        if (!WebConst.SUCCESS_RESULT.equals(result)) {
            return RestResponseBo.fail(result);
        }
        return RestResponseBo.ok();
    }
}
