package org.codingwater.serviceimpl;

import org.codingwater.model.LagouJobInfo;
import org.codingwater.service.IJobSpiderService;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by water on 4/19/16.
 */
public class JobSpiderServiceImplTest {

  IJobSpiderService jobSpiderService = new JobSpiderServiceImpl();

  @Test
  public void testFetchJobInfosFromLagou() throws Exception {
    List<LagouJobInfo> ret = jobSpiderService.fetchJobInfosFromLagou("上海", "Java", 1, "", "");
    Assert.assertNotEquals(0, ret.size());

  }
}