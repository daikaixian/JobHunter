package org.codingwater.dao;

import com.google.common.collect.Maps;

import org.apache.ibatis.session.SqlSession;
import org.codingwater.model.BaseJobInfo;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


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
    System.out.println("fuck spring ");
    return 1;
  }


}
