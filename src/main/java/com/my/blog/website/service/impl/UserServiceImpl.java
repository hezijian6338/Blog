package com.my.blog.website.service.impl;

import com.my.blog.website.dao.UserVoMapper;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.model.Vo.UserVo;
import com.my.blog.website.service.IUserService;
import com.my.blog.website.utils.TaleUtils;
import com.my.blog.website.model.Vo.UserVoExample;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by BlueT on 2017/3/3.
 */
@Service
public class UserServiceImpl implements IUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserVoMapper userDao;

    @Override
    @Transactional
    public Integer insertUser(UserVo userVo) {
        Integer uid = null;
        if (StringUtils.isNotBlank(userVo.getUsername()) && StringUtils.isNotBlank(userVo.getEmail())) {
//            用户密码加密
            String encodePwd = TaleUtils.MD5encode(userVo.getUsername() + userVo.getPassword());
            userVo.setPassword(encodePwd);
            userDao.insertSelective(userVo);
        }
        return userVo.getUid();
    }

    @Override
    public UserVo queryUserById(Integer uid) {
        UserVo userVo = null;
        if (uid != null) {
            userVo = userDao.selectByPrimaryKey(uid);
        }
        return userVo;
    }

    @Override
    public UserVo queryUserByUsername(String username) {
//        根据用户的博客名称来查找该用户的具体信息⤴️

//        创建新的用户接口
        UserVoExample example = new UserVoExample();

//        新建新的查询对象
        UserVoExample.Criteria criteria = example.createCriteria();

//        添加查询的SQL语句,下面的函数是根据用户的博客名称来获得具体数据（后期要控制用户的博客名字也要唯一）
        criteria.andUsernameEqualTo(username);

//        检查该用户的博客名字是否存在
        long count = userDao.countByExample(example);
        if (count < 1) {
            throw new TipException("不存在该用户");
        }

//        检查该用户的博客名字是否有重复命名的
        List<UserVo> userVos = userDao.selectByExample(example);
        if (userVos.size() != 1) {
            throw new TipException("用户名不唯一");
        }

//        返回列表中第一个数据,也是有且仅有的一个
        return userVos.get(0);
    }

    @Override
    public UserVo login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new TipException("用户名和密码不能为空");
        }
        UserVoExample example = new UserVoExample();
        UserVoExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        long count = userDao.countByExample(example);
        if (count < 1) {
            throw new TipException("不存在该用户");
        }
        String pwd = TaleUtils.MD5encode(username + password);
        criteria.andPasswordEqualTo(pwd);
        List<UserVo> userVos = userDao.selectByExample(example);
        if (userVos.size() != 1) {
            throw new TipException("用户名或密码错误");
        }
        return userVos.get(0);
    }


    @Override
    public UserVo findPassword(String username, String email) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(email)) {
            throw new TipException("用户名和邮箱不能为空");
        }
        UserVoExample example = new UserVoExample();
        UserVoExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        long count = userDao.countByExample(example);
        if (count < 1) {
            throw new TipException("不存在该用户");
        }
        criteria.andEmailEqualTo(email);
        List<UserVo> userVos = userDao.selectByExample(example);
        if (userVos.size() != 1) {
            throw new TipException("用户名或邮箱错误");
        }
        return userVos.get(0);
    }

    @Override
    @Transactional
    public void updateByUid(UserVo userVo) {
        if (null == userVo || null == userVo.getUid()) {
            throw new TipException("userVo is null");
        }
        int i = userDao.updateByPrimaryKeySelective(userVo);
        if (i != 1) {
            throw new TipException("update user by uid and retrun is not one");
        }
    }

}
