package org.codingwater.serviceimpl;

import org.codingwater.BaseTest;
import org.codingwater.model.BaseJobInfo;
import org.codingwater.model.LagouJobInfo;
import org.codingwater.service.IJobSpiderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * Created by water on 4/19/16.
 */
public class JobSpiderServiceImplTest extends BaseTest{

  @Autowired
  private IJobSpiderService jobSpiderService;

  @Test
  public void testFetchJobInfosFromLagou() throws Exception {
    List<LagouJobInfo> ret = jobSpiderService.fetchJobInfosFromLagou("上海", "Java", 1, "10k-15k", "");
    Assert.assertNotEquals(0, ret.size());

  }
}