package org.codingwater.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by water on 4/14/16.
 */
public class LagouSpiderServiceTest {

  @Test
  public void testRun() {
    System.out.println("let's start");
  }

  @Test
  public void testJsoup() {

    try {
      //fix UnsupportedMimeTypeException by using ignoreContentType(true)
      Document doc = Jsoup.connect("http://www.lagou.com/jobs/positionAjax" +
          ".json?city=上海&first=false&pn=2&kd=Java").ignoreContentType(true).get();
      System.out.println(doc);
      System.out.println(doc.body());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testHttpClient() {

    // giveup. HttpClient is not as handy as Jsoup for me.
    DefaultHttpClient httpclient = new DefaultHttpClient();

    HttpGet httpget = new HttpGet("http://www.baidu.com");

    try {
      HttpResponse response = httpclient.execute(httpget);
      System.out.println(response);

      HttpEntity entity = response.getEntity();

      System.out.println("----------------------------------------");
      System.out.println(response.getStatusLine());
      if (entity != null) {
        System.out.println("Response content length: " + entity.getContentLength());
      }
      if (entity != null) {
        entity.consumeContent();
      }

      // When HttpClient instance is no longer needed,
      // shut down the connection manager to ensure
      // immediate deallocation of all system resources
      httpclient.getConnectionManager().shutdown();

    } catch (IOException e) {
      e.printStackTrace();
    }


  }


}