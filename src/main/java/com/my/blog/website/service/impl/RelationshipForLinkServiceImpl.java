package com.my.blog.website.service.impl;


import com.my.blog.website.dao.RelationshipsForLinkVoMapper;
import com.my.blog.website.model.Vo.RelationshipForeLinkVoKey;
import com.my.blog.website.model.Vo.RelationshipsForLinkVoExample;
import com.my.blog.website.service.IRelationshipForLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by BlueT on 2017/3/18.
 */
@Service
public class RelationshipForLinkServiceImpl implements IRelationshipForLinkService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RelationshipForLinkServiceImpl.class);

    @Resource
    private RelationshipsForLinkVoMapper relationshipsForLinkVoMapper;

    @Override
    public void deleteById(Integer uid, Integer mid) {
        RelationshipsForLinkVoExample relationshipsForLinkVoExample = new RelationshipsForLinkVoExample();
        RelationshipsForLinkVoExample.Criteria criteria = relationshipsForLinkVoExample.createCriteria();
        if (uid != null) {
            criteria.andUidEqualTo(uid);
        }
        if (mid != null) {
            criteria.andMidEqualTo(mid);
        }
        relationshipsForLinkVoMapper.deleteByExample(relationshipsForLinkVoExample);
    }

    @Override
    public List<RelationshipForeLinkVoKey> getRelationshipForLinkById(Integer uid, Integer mid) {
        RelationshipsForLinkVoExample relationshipsForLinkVoExample = new RelationshipsForLinkVoExample();
        RelationshipsForLinkVoExample.Criteria criteria = relationshipsForLinkVoExample.createCriteria();
        if (uid != null) {
            criteria.andUidEqualTo(uid);
        }
        if (mid != null) {
            criteria.andMidEqualTo(mid);
        }
        return relationshipsForLinkVoMapper.selectByExample(relationshipsForLinkVoExample);
    }

    @Override
    public void insertVo(RelationshipForeLinkVoKey relationshipForeLinkVoKey) {
        relationshipsForLinkVoMapper.insert(relationshipForeLinkVoKey);
    }

    @Override
    public Long countById(Integer uid, Integer mid) {
        LOGGER.debug("Enter countById method:uid={},mid={}",uid,mid);
        RelationshipsForLinkVoExample relationshipsForLinkVoExample = new RelationshipsForLinkVoExample();
        RelationshipsForLinkVoExample.Criteria criteria = relationshipsForLinkVoExample.createCriteria();
        if (uid != null) {
            criteria.andUidEqualTo(uid);
        }
        if (mid != null) {
            criteria.andMidEqualTo(mid);
        }
        long num = relationshipsForLinkVoMapper.countByExample(relationshipsForLinkVoExample);
        LOGGER.debug("Exit countById method return num={}",num);
        return num;
    }
}
