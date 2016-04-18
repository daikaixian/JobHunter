package org.codingwater.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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

  //拉勾
  @Test
  public void testLagouWithJsoup() {

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

  //内推,通过解析html页面获取数据
  @Test
  public void testNeituiWithJsoup() {
    String neiTuiUrl = "http://www.neitui" +
        ".me/?name=neitui&handle=lists&keyword=Java&kcity=上海&workage=2&page=1";
    try {
      Document doc = Jsoup.connect(neiTuiUrl).ignoreContentType(true).get();
     // System.out.println(doc);
      System.out.println("________________________________________________");
//      System.out.println(doc.getElementById("joblist"));
//      System.out.println(doc.select(".commentjobs")); //通过css 类选择器,选择最具有唯一性标志的css类,来解析joblist.

//      Elements jobList = doc.select(".commentjobs");
//      System.out.println(jobList.size());

      System.out.println(
          doc.getElementsByAttributeValue("class", "content commentjobs brjobs topjobs"));
      //通过attribute的K-V查找模式.

      System.out.println("________________________________________________");
//      System.out.println(doc.getElementById("J_Screen_Workage"));

      System.out.println("________________________________________________");
//      System.out.println(doc.select("content commentjobs brjobs topjobs"));
      System.out.println("________________________________________________");
//      System.out.println(doc.getElementsByClass("content commentjobs brjobs topjobs"));



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