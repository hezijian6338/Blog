package com.my.blog.website.service;



import com.my.blog.website.model.Vo.RelationshipForeLinkVoKey;

import java.util.List;

/**
 * Created by Mickey on 2018/7/10.
 */
public interface IRelationshipForLinkService {
    /**
     * 按住键删除
     * @param uid
     * @param mid
     */
    void deleteById(Integer uid, Integer mid);

    /**
     * 按主键统计条数
     * @param uid
     * @param mid
     * @return 条数
     */
    Long countById(Integer uid, Integer mid);


    /**
     * 保存對象
     * @param relationshipForeLinkVoKey
     */
    void insertVo(RelationshipForeLinkVoKey relationshipForeLinkVoKey);

    /**
     * 根据id搜索
     * @param uid
     * @param mid
     * @return
     */
    List<RelationshipForeLinkVoKey> getRelationshipForLinkById(Integer uid, Integer mid);
}
