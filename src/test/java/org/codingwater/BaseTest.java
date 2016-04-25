package org.codingwater;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by water on 4/20/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "file:src/test/spring-test.xml",
    "file:src/test/spring-mybatis.xml"
})
public class BaseTest {

  @Test
  public void testrun() {
    System.out.println("it's ok");
  }
}
