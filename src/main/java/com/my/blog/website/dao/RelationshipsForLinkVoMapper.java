package com.my.blog.website.dao;

import com.my.blog.website.model.Vo.RelationshipForeLinkVoKey;
import com.my.blog.website.model.Vo.RelationshipsForLinkVoExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RelationshipsForLinkVoMapper{
    long countByExample(RelationshipsForLinkVoExample example);

    int deleteByExample(RelationshipsForLinkVoExample example);

    int deleteByPrimaryKey(RelationshipForeLinkVoKey key);

    int insert(RelationshipForeLinkVoKey record);

    int insertSelective(RelationshipForeLinkVoKey record);

    List<RelationshipForeLinkVoKey> selectByExample(RelationshipsForLinkVoExample example);

    int updateByExampleSelective(@Param("record") RelationshipForeLinkVoKey record, @Param("example") RelationshipsForLinkVoExample example);

    int updateByExample(@Param("record") RelationshipForeLinkVoKey record, @Param("example") RelationshipsForLinkVoExample example);
}
