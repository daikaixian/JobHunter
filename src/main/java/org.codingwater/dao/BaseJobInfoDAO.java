package org.codingwater.dao;

import com.google.common.collect.Maps;

import org.apache.ibatis.session.SqlSession;
import org.codingwater.model.BaseJobInfo;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * Created by water on 4/24/16.
 */

@Repository
public class BaseJobInfoDAO {

  @Autowired
  private SqlSession writeTpl;

  @Autowired
  private SqlSession readTpl;

  public int savePosition(BaseJobInfo jobInfo) {
    int result = writeTpl.insert("position.insertPosition", jobInfo);
    return result;
  }

  public BaseJobInfo findPositionById(String positionId) {
    Map<String, Object> param = Maps.newHashMap();
    param.put("positionId", positionId);
    return readTpl.selectOne("position.findPositionById", param);
  }


  public List<BaseJobInfo> queryPositionsWithCondition(String city,
      String keyword, String workYear) {
    Map<String, Object> param = Maps.newHashMap();
    param.put("city", city);
    param.put("keyword", "%" + keyword + "%");
    param.put("workYear", workYear);

    return readTpl.selectList("position.queryPositionsWithCondition", param);
  }
}
