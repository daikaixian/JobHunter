package org.codingwater.serviceimpl;

import org.codingwater.BaseTest;
import org.codingwater.model.LagouJobInfo;
import org.codingwater.service.IJobSpiderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by water on 4/19/16.
 */
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/spring-mvc.xml",
    "file:src/main/webapp/WEB-INF/web.xml"})
public class JobSpiderServiceImplTest {


  IJobSpiderService jobSpiderService = new JobSpiderServiceImpl();

  @Before
  public void before() {
    @SuppressWarnings("resource")
    ApplicationContext context = new ClassPathXmlApplicationContext(
        new String[]{"file:src/main/webapp/WEB-INF/spring/spring-mvc.xml"});
  }

  @Test
  public void testFetchJobInfosFromLagou() throws Exception {
    List<LagouJobInfo> ret = jobSpiderService.fetchJobInfosFromLagou("上海", "Java", 1, "", "");
    Assert.assertNotEquals(0, ret.size());

  }
}