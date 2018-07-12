package com.my.blog.website.controller;

import com.github.pagehelper.PageInfo;
import com.my.blog.website.constant.WebConst;
import com.my.blog.website.dto.ErrorCode;
import com.my.blog.website.dto.MetaDto;
import com.my.blog.website.dto.Types;
import com.my.blog.website.model.Bo.ArchiveBo;
import com.my.blog.website.model.Bo.CommentBo;
import com.my.blog.website.model.Bo.RestResponseBo;
import com.my.blog.website.model.Vo.CommentVo;
import com.my.blog.website.model.Vo.ContentVo;
import com.my.blog.website.model.Vo.MetaVo;
import com.my.blog.website.model.Vo.UserVo;
import com.my.blog.website.service.*;
import com.my.blog.website.utils.IPKit;
import com.my.blog.website.utils.PatternKit;
import com.my.blog.website.utils.TaleUtils;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.List;

/**
 * 首页
 * Created by Administrator on 2017/3/8 008.
 */
@Controller
public class IndexController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private IContentService contentService;

    @Resource
    private ICommentService commentService;

    @Resource
    private IMetaService metaService;

    @Resource
    private ISiteService siteService;

    @Resource
    private IUserService userService;

    /**
     * 首页
     * <p>
     * 访问地址修改为un(username)/用户博客名字
     *
     * @return
     */
    @GetMapping(value = "un/{username}")
    public String index(HttpServletRequest request, @PathVariable String username, @RequestParam(value = "limit", defaultValue = "12") int limit) {

//        个人博客接入口,(最后的参数传入地址栏上填写的用户名)
        return this.index(request, 1, limit, username);
    }

    @GetMapping(value = "/")
    public String index(HttpServletRequest request, @RequestParam(value = "limit", defaultValue = "12") int limit) {

//        游客接入口,(游客以0为登陆名称登陆)
        return this.index(request, 1, limit, "0");
    }

    /**
     * 首页分页
     *
     * @param request request
     * @param p       第几页
     * @param limit   每页大小
     * @return 主页
     */
    @GetMapping(value = "/{username}/page/{p}")
    public String index(HttpServletRequest request, @PathVariable int p, @RequestParam(value = "limit", defaultValue = "12") int limit, @PathVariable String username) {
//        @PathVariable 能够获取URL地址上用{}注明的参数值，传递到函数当中↑----@RequestParam 则是由前端页面带参数传递值过来获取的↑

//        P为当前显示第几页参数;若P小于0,则显示1,若大于0,则显示P;若P大于页面设置最大页数,则显示1,若小于,则正常显示P.
        p = p < 0 || p > WebConst.MAX_PAGE ? 1 : p;

//        初始化用户的uid为0，则不根据作者查询文章
        int uid = 0;

//        检查当前操作者是以博客主的身份登陆,还是以游客的身份登陆
        if (!username.equals("0")) {

//            根据用户的博客名字来获取该用户的信息
            UserVo user = userService.queryUserByUsername(username);

//            根据返回的用户信息，取得该用户的id
            uid = user.getUid();

//            给前端传值当前用户的id
            this.authorId(request, uid);
        } else {

//            如果是以公共的身份访问，即游客的身份访问，则设置该参数为true，前端不显示右上角三个私人按钮
            request.setAttribute("public", true);
        }

        this.state(request, "index");

//        发送参数给数据逻辑层（Service）进行处理,返回页面所需要的信息/对象（ContentVo）,也就是文章
        PageInfo<ContentVo> articles = contentService.getContents(uid, p, limit);

//        通过request请求把Controller中的结果返回给前台页面进行显示（没想明白为啥不用Model进行传递，而要用传统的Req,Res）
        request.setAttribute("articles", articles);


//        若显示第几页的参数P,不是显示第一页,则添加显示当前页
//        这里的this.title其实也是通过Req传递到前端页面,只不过这里把通用型方法抽离出来,写在了BaseController这个父类当中,我们通过继承来实现方法,同时实现了代码的小量解耦
        if (p > 1) {
            this.title(request, "第" + p + "页");
        }

//        这个也是抽离函数,把跳转前端页面路径封装为（/themes/index.html）
        return this.render("index");
    }

    /**
     * 文章页
     *
     * @param request 请求
     * @param cid     文章主键
     * @return
     */
    @GetMapping(value = {"article/{cid}", "article/{cid}.html"})
    public String getArticle(HttpServletRequest request, @PathVariable String cid) {
//        @PathVariable 能够获取URL地址上用{}注明的参数值，传递到函数当中↑

//        根据cid获取某一篇文章的详细内容
        ContentVo contents = contentService.getContents(cid);

//        如果获取的文章对象是为空的或者是属于草稿状态的文章就来一波报错
        if (null == contents || "draft".equals(contents.getStatus())) {
            return this.render_404();
        }

//        用抽离的一个方法根据作者的id来获取该作者的相关信息
        UserVo user = this.getUserById(contents.getAuthorId());

        contents.setCommentsNum(commentService.countComment(contents.getCid()));

//        返回获取的值给前端使用
        this.author(request, user.getUsername());
        request.setAttribute("authorId", user.getUid());
        request.setAttribute("article", contents);
        request.setAttribute("is_post", true);
        this.state(request, "post");

//        加载文章的评论
        completeArticle(request, contents);

//        检查同一个ip地址是否在2小时内访问同一文章（先不理）
        if (!checkHitsFrequency(request, cid)) {
            updateArticleHit(contents.getCid(), contents.getHits());
        }

//        也是一个抽离的方法,重定向前端路径为"/themes/default/post"
        return this.render("post");


    }

    /**
     * 文章页(预览)
     *
     * @param request 请求
     * @param cid     文章主键
     * @return
     */
    @GetMapping(value = {"article/{cid}/preview", "article/{cid}.html"})
    public String articlePreview(HttpServletRequest request, @PathVariable String cid) {
        ContentVo contents = contentService.getContents(cid);
        if (null == contents) {
            return this.render_404();
        }
        String author_name = userService.queryUserById(contents.getAuthorId()).getUsername();
        contents.setCommentsNum(commentService.countComment(contents.getCid()));

        this.author(request, author_name);
        request.setAttribute("article", contents);
        request.setAttribute("is_post", true);
        completeArticle(request, contents);
        if (!checkHitsFrequency(request, cid)) {
            updateArticleHit(contents.getCid(), contents.getHits());
        }
        return this.render("post");


    }

    /**
     * 抽取公共方法
     *
     * @param request
     * @param contents
     */
    private void completeArticle(HttpServletRequest request, ContentVo contents) {
//        是否允许进行评论
        if (contents.getAllowComment()) {

//            从request中获取cp的值
            String cp = request.getParameter("cp");

//            没看懂这里具体用处
            if (StringUtils.isBlank(cp)) {
                cp = "1";
            }
            request.setAttribute("cp", cp);

//            加载文章评论
            PageInfo<CommentBo> commentsPaginator = commentService.getComments(contents.getCid(), Integer.parseInt(cp), 6);

//            把获取的评论值返回给前端
            request.setAttribute("comments", commentsPaginator);
        }
    }

    /**
     * 注销
     *
     * @param session
     * @param response
     */
    @RequestMapping("logout")
    public void logout(HttpSession session, HttpServletResponse response) {
        TaleUtils.logout(session, response);
    }

    /**
     * 评论操作
     */
    @PostMapping(value = "comment")
    @ResponseBody
    public RestResponseBo comment(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam Integer cid, @RequestParam Integer coid,
                                  @RequestParam String author, @RequestParam String mail,
                                  @RequestParam String url, @RequestParam String text, @RequestParam String _csrf_token) {

        String ref = request.getHeader("Referer");
        if (StringUtils.isBlank(ref) || StringUtils.isBlank(_csrf_token)) {
            return RestResponseBo.fail(ErrorCode.BAD_REQUEST);
        }

        String token = cache.hget(Types.CSRF_TOKEN.getType(), _csrf_token);
        if (StringUtils.isBlank(token)) {
            return RestResponseBo.fail(ErrorCode.BAD_REQUEST);
        }

        if (null == cid || StringUtils.isBlank(text)) {
            return RestResponseBo.fail("请输入完整后评论");
        }

        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return RestResponseBo.fail("姓名过长");
        }

        if (StringUtils.isNotBlank(mail) && !TaleUtils.isEmail(mail)) {
            return RestResponseBo.fail("请输入正确的邮箱格式");
        }

        if (StringUtils.isNotBlank(url) && !PatternKit.isURL(url)) {
            return RestResponseBo.fail("请输入正确的URL格式");
        }

        if (text.length() > 200) {
            return RestResponseBo.fail("请输入200个字符以内的评论");
        }

        String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return RestResponseBo.fail("您发表评论太快了，请过会再试");
        }

        author = TaleUtils.cleanXSS(author);
        text = TaleUtils.cleanXSS(text);

        author = EmojiParser.parseToAliases(author);
        text = EmojiParser.parseToAliases(text);

        CommentVo comments = new CommentVo();
        comments.setAuthor(author);
        comments.setCid(cid);
        comments.setIp(request.getRemoteAddr());
        comments.setUrl(url);
        comments.setContent(text);
        comments.setMail(mail);
        comments.setParent(coid);
        try {
            String result = commentService.insertComment(comments);
            cookie("tale_remember_author", URLEncoder.encode(author, "UTF-8"), 7 * 24 * 60 * 60, response);
            cookie("tale_remember_mail", URLEncoder.encode(mail, "UTF-8"), 7 * 24 * 60 * 60, response);
            if (StringUtils.isNotBlank(url)) {
                cookie("tale_remember_url", URLEncoder.encode(url, "UTF-8"), 7 * 24 * 60 * 60, response);
            }
            // 设置对每个文章1分钟可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY.getType(), val, 1, 60);
            if (!WebConst.SUCCESS_RESULT.equals(result)) {
                return RestResponseBo.fail(result);
            }
            return RestResponseBo.ok();
        } catch (Exception e) {
            String msg = "评论发布失败";
            LOGGER.error(msg, e);
            return RestResponseBo.fail(msg);
        }
    }


    /**
     * 分类页
     *
     * @return
     */
    @GetMapping(value = "category/{keyword}")
    public String categories(HttpServletRequest request, @PathVariable String keyword, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.categories(request, keyword, 1, limit);
    }

    @GetMapping(value = "category/{keyword}/{page}")
    public String categories(HttpServletRequest request, @PathVariable String keyword,
                             @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {

//        检查显示页数参数是否符合规范
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;

//        根据需要的分类查询中转查询参数
        MetaDto metaDto = metaService.getMeta(Types.CATEGORY.getType(), keyword);

//        如果查找不到对应的信息则返回错误页面
        if (null == metaDto) {
            return this.render_404();
        }

//        利用上面查询到中转查询参数来查询对应的文章信息
        PageInfo<ContentVo> contentsPaginator = contentService.getArticles(metaDto.getMid(), page, limit);

        request.setAttribute("articles", contentsPaginator);
        request.setAttribute("meta", metaDto);
        request.setAttribute("type", "分类");
        request.setAttribute("keyword", keyword);

        return this.render("page-category");
    }


    /**
     * 归档页
     *
     * @return
     */
    @GetMapping(value = "archives")
    public String archives(HttpServletRequest request) {

//        告诉前端页面现在是处于游客页面,所以请求的地址是不带具体作者的id
        request.setAttribute("public", true);
        return this.archives(request, 0);
    }

    @GetMapping(value = "archives/{author_id}")
    public String archives(HttpServletRequest request, @PathVariable Integer author_id) {
//        由于该页面是不需要对外提供的链接,所有地址链接中直接使用了id信息,而不是作者名称⬆️

//        根据作者的id,来查找对应的归档信息,其实是和查询对应的文章是一样的道理,只是归档显示的具体信息会有所不同,所以调用的方法有所区别
        List<ArchiveBo> archives = siteService.getArchives(author_id);

//        相当于现在处于的函数是公共函数,能够处理带id和不带id的情况,当id值为0是对应的情况就是不带id的情况,即是游客状态,此处就是做这个判断
        if (author_id != 0) {

//            其实可以写成一句话来完成整个传值调用,但是太过冗长,难以理解,所以还是分开描述写
            UserVo user = this.getUserById(author_id);
            this.author(request, user.getUsername());
        }

//        返回状态用给前端,便于前端进行判断按钮的显示和状态判断
        this.state(request, "archives");
        this.authorId(request, author_id);
        request.setAttribute("archives", archives);
        return this.render("archives");
    }


    /**
     * 友链页
     *
     * @return
     */
    @GetMapping(value = "links/{author_id}")
    public String links(HttpServletRequest request, @PathVariable Integer author_id) {
        List<MetaVo> links = metaService.getMetas(Types.LINK.getType(),author_id);

//        根据当前URL地址获取到的author_id,去查询数据库获取当前的用户对象
        UserVo user = this.getUserById(author_id);

//        通过抽离的方式,进行对用户名称返回到前端
        this.author(request, user.getUsername());

        this.authorId(request, author_id);

//        返回友链的数据到前端
        request.setAttribute("links", links);

        this.state(request, "links");

//        通过抽离的方式,重定向前端页面的位置
        return this.render("links");
    }

    /**
     * 自定义页面,如关于的页面
     */
    @GetMapping(value = "/{pagename}")
    public String page(@PathVariable String pagename, HttpServletRequest request) {
        ContentVo contents = contentService.getContents(pagename);
        if (null == contents) {
            return this.render_404();
        }
        if (contents.getAllowComment()) {
            String cp = request.getParameter("cp");
            if (StringUtils.isBlank(cp)) {
                cp = "1";
            }
            PageInfo<CommentBo> commentsPaginator = commentService.getComments(contents.getCid(), Integer.parseInt(cp), 6);
            request.setAttribute("comments", commentsPaginator);
        }
        UserVo user = this.getUserById(contents.getAuthorId());
        request.setAttribute("article", contents);
        this.authorId(request, contents.getAuthorId());
        this.author(request, user.getUsername());
        this.state(request, "about");
        if (!checkHitsFrequency(request, String.valueOf(contents.getCid()))) {
            updateArticleHit(contents.getCid(), contents.getHits());
        }
        return this.render("page");
    }


    /**
     * 搜索页
     *
     * @param keyword
     * @return
     */
    @GetMapping(value = "search/{keyword}")
    public String search(HttpServletRequest request, @PathVariable String keyword, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.search(request, keyword, 1, limit);
    }

    @GetMapping(value = "search/{keyword}/{page}")
    public String search(HttpServletRequest request, @PathVariable String keyword, @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
        PageInfo<ContentVo> articles = contentService.getArticles(keyword, page, limit);
        request.setAttribute("articles", articles);
        request.setAttribute("type", "搜索");
        request.setAttribute("keyword", keyword);
        return this.render("page-category");
    }

    /**
     * 更新文章的点击率
     *
     * @param cid
     * @param chits
     */
    private void updateArticleHit(Integer cid, Integer chits) {

//        检查当前缓存的点击次数
        Integer hits = cache.hget("article" + cid, "hits");

//        检查数据库传过来的点击次数
        if (chits == null) {
            chits = 0;
        }

//        缓存的点击次数为空的话就赋值为一次,如果不为空就加一次
        hits = null == hits ? 1 : hits + 1;

//        可以设置当前ip地址的用户在一定时间内点击多少次才开始算一次点击,然后存进数据库(防止刷点击数的意思)
        if (hits >= WebConst.HIT_EXCEED) {

//            新建一个文章的对象
            ContentVo temp = new ContentVo();

//            设定文章的id
            temp.setCid(cid);

//            文章现在应该有的点击数加上缓存的点击数
            temp.setHits(chits + hits);

//            对数据库进行更新操作
            contentService.updateContentByCid(temp);

//            重置缓存,重新计算点击次数
            cache.hset("article" + cid, "hits", 1);
        } else {

//            如果点击次数没达到指定的次数,则继续累加,此处为更新缓存次数
            cache.hset("article" + cid, "hits", hits);
        }
    }

    /**
     * 标签页
     *
     * @param name
     * @return
     */
    @GetMapping(value = "tag/{name}")
    public String tags(HttpServletRequest request, @PathVariable String name, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.tags(request, name, 1, limit);
    }

    /**
     * 标签分页
     *
     * @param request
     * @param name
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "tag/{name}/{page}")
    public String tags(HttpServletRequest request, @PathVariable String name, @PathVariable int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {

        page = page < 0 || page > WebConst.MAX_PAGE ? 1 : page;
//        对于空格的特殊处理
        name = name.replaceAll("\\+", " ");
        MetaDto metaDto = metaService.getMeta(Types.TAG.getType(), name);
        if (null == metaDto) {
            return this.render_404();
        }

        PageInfo<ContentVo> contentsPaginator = contentService.getArticles(metaDto.getMid(), page, limit);
        request.setAttribute("articles", contentsPaginator);
        request.setAttribute("meta", metaDto);
        request.setAttribute("type", "标签");
        request.setAttribute("keyword", name);
        request.setAttribute("public", true);

        return this.render("page-category");
    }

    /**
     * 设置cookie
     *
     * @param name
     * @param value
     * @param maxAge
     * @param response
     */
    private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

    /**
     * 检查同一个ip地址是否在2小时内访问同一文章
     *
     * @param request
     * @param cid
     * @return
     */
    private boolean checkHitsFrequency(HttpServletRequest request, String cid) {
        String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
        Integer count = cache.hget(Types.HITS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return true;
        }
        cache.hset(Types.HITS_FREQUENCY.getType(), val, 1, WebConst.HITS_LIMIT_TIME);
        return false;
    }

}
