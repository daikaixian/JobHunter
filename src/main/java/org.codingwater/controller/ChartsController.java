package org.codingwater.controller;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.codingwater.concurrency.CaculateThread;
import org.codingwater.controller.loadbalance.DynamicLB;
import org.codingwater.controller.loadbalance.ImageHelper;
import org.codingwater.controller.loadbalance.Nodes;
import org.codingwater.model.SalaryQueryResult;
import org.codingwater.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by water on 4/26/16.
 */
@Controller
public class ChartsController {

  @Autowired
  IReportService reportService;

  final static String[] caption = { "fast", "common", "slow" };

//  static double[][] respSample = new double[][] {
//          { 0.060d, 0.001d }, // 60ms, fast
//          { 0.200d, 0.01d }, // 200ms, common
//          { 0.500d, 0.001d }// 500ms, slow
//  };
//
//  static double[] opOKSample = new double[] {
//          0.9999d, // ten-thousandth
//          0.999d, // thousandth
//          0.05d, // 5%
//  };
//
//  static double[][] loadSample = new double[][] {
//          { 20d, 0.01d }, // lightweight
//          { 40d, 0.01d }, // normal
//          { 60d, 0.01d }// overload
//  };




  @RequestMapping(value = "/charts/", method = RequestMethod.GET)
  public String chartsView(Model model, @RequestParam(value = "city", defaultValue = "上海")String
      city, @RequestParam(value = "keyword", defaultValue = "Java")String
      keyword) {
    //查询多个不同工作经验的薪资水平.

    SalaryQueryResult result1 = reportService.calculateAverageSalary(city, keyword, "应届毕业生");
    SalaryQueryResult result2 = reportService.calculateAverageSalary(city, keyword, "1年以下");
    SalaryQueryResult result3 = reportService.calculateAverageSalary(city, keyword, "1-3年");
    SalaryQueryResult result4 = reportService.calculateAverageSalary(city, keyword, "3-5年");
    SalaryQueryResult result5 = reportService.calculateAverageSalary(city, keyword, "5-10年");

    model.addAttribute("result1", result1);
    model.addAttribute("result2", result2);
    model.addAttribute("result3", result3);
    model.addAttribute("result4", result4);
    model.addAttribute("result5", result5);
    return "charts";
  }

  @RequestMapping(value = "/loadbalance/", method = RequestMethod.GET)
  public String loadbalance(Model model, @RequestParam("profiles") String profiles,
          @RequestParam(value = "latestWindowSize", defaultValue = "100")int latestWS,
          @RequestParam(value = "avgWindowSize", defaultValue = "5000")int avgWS,
          @RequestParam(value = "opOKSample[]", required=false)double[] _opOKSample
          ) {

    double[][] respSample = new double[][] { { 0.060d, 0.001d }, // 60ms, fast
            { 0.200d, 0.01d }, // 200ms, common
            { 0.500d, 0.001d }// 500ms, slow
    };

    double[] opOKSample = new double[] { 0.9999d, // ten-thousandth
            0.999d, // thousandth
            0.05d, // 5%
    };

    double[][] loadSample = new double[][] { { 20d, 0.01d }, // lightweight
            { 40d, 0.01d }, // normal
            { 60d, 0.01d }// overload
    };



    String[] str = profiles.split(",");
    int array[] = new int[str.length];
    for (int i = 0; i < str.length; i++) {
      array[i] = Integer.parseInt(str[i]);
    }

    //直接调用LB
    DynamicLB dynamicLB = new DynamicLB(array.length, 1000, 5000);
    double[] possbility = simulation(array, 1000, dynamicLB, respSample, loadSample, opOKSample); //count=1000
    List<Integer> samples = new ArrayList<Integer>();

    //根据生成的累加概率积分进行随机采样，统计10000次中各个节点被选择的次数
    for (int j = 0; j < 10000; j++) {
      int pos = dynamicLB.sample();
      //做一万次采样
      samples.add(pos);
    }
    //对样品进行统计.
    //对采样数据整理
    Map<Integer, Long> result = samples.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    String[] title = new String[array.length];

    List<Nodes> nodesList = new ArrayList<>();
    for (int j = 0; j < array.length; j++) {
      Nodes nodes = new Nodes();
      nodes.setCaption(caption[array[j]]);

      System.out.println(result.get(j).intValue());

      nodes.setCount(String.valueOf(result.get(j)));
      nodes.setPossibility(possbility[j]);
      nodesList.add(nodes);

      title[j] = caption[array[j]];
    }

    for (Nodes node : nodesList) {
      System.out.println(node.getCaption() + "=>" + node.getCount() + "," + node.getPossibility());
    }

    model.addAttribute("nodes", nodesList);
    return "lb";
    }



  @RequestMapping(value = "/lbrate_fail/", method = RequestMethod.GET)
  public String lbratefail(Model model) {

    double[][] respSample = new double[][] { { 0.060d, 0.001d }, // 60ms, fast
            { 0.200d, 0.01d }, // 200ms, common
            { 0.500d, 0.001d }// 500ms, slow
    };

    double[] opOKSample = new double[] { 0.9999d, // ten-thousandth
            0.999d, // thousandth
            0.05d, // 5%
    };

    double[][] loadSample = new double[][] { { 20d, 0.01d }, // lightweight
            { 40d, 0.01d }, // normal
            { 60d, 0.01d }// overload
    };


    int array[] = new int[] { 0, 1, 2 };
    DynamicLB dynamicLB = new DynamicLB(array.length, 1000, 5000);


    double okAvg = 0.50;
    double okGap = 0.02;
    List<ImageHelper> imageHelperList1 = new ArrayList<>();

    for (int i = 0; i <= 24; i++) {
      opOKSample[2] = okAvg - i * okGap;
      opOKSample[1] = okAvg;
      opOKSample[0] = okAvg + i * okGap;

      //直接调用LB
      double[] possbility = simulation(array, 1000, dynamicLB, respSample, loadSample, opOKSample); //count=1000

      double rate_f_s = possbility[0] / possbility[2];
      double rate_c_s = possbility[1] / possbility[2];
      ImageHelper imageHelper = new ImageHelper();
      imageHelper.setDistance(String.valueOf(i));
      imageHelper.setRate_f_s(rate_f_s);
      imageHelper.setRate_c_s(rate_c_s);
      imageHelperList1.add(imageHelper);
    }

    model.addAttribute("imageHelperList1", imageHelperList1);

    System.out.println("opOKSample[1]:"+opOKSample[1]);

    return "lbrate_fail";
  }


  @RequestMapping(value = "/lbrate_load/", method = RequestMethod.GET)
  public String lbrateload(Model model) {
    double[][] respSample = new double[][] { { 0.060d, 0.001d }, // 60ms, fast
            { 0.200d, 0.01d }, // 200ms, common
            { 0.500d, 0.001d }// 500ms, slow
    };

    double[] opOKSample = new double[] { 0.9999d, // ten-thousandth
            0.999d, // thousandth
            0.05d, // 5%
    };

    double[][] loadSample = new double[][] { { 20d, 0.01d }, // lightweight
            { 40d, 0.01d }, // normal
            { 60d, 0.01d }// overload
    };

    System.out.println("loadsamp[0][1]:"+loadSample[0][1]);

    double[][] _respSample = respSample;
    double[] _opOKSample = opOKSample;
    double[][] _loadSample = loadSample;

    int array[] = new int[] { 0, 1, 2 };
    DynamicLB dynamicLB = new DynamicLB(array.length, 1000, 5000);

    double loadAvg = 25d;
    double load_gap = 1d;
    List<ImageHelper> imageHelperList2 = new ArrayList<>();

    for (int i = 0; i <= 24; i++) {
      loadSample[0][0] = loadAvg - i * load_gap;
      loadSample[1][0] = loadAvg;
      loadSample[2][0] = loadAvg + i * load_gap;

      //直接调用LB
      double[] possbility = simulation(array, 1000, dynamicLB, respSample, loadSample, opOKSample); //count=1000

      double rate_f_s = possbility[0] / possbility[2];
      double rate_c_s = possbility[1] / possbility[2];
      ImageHelper imageHelper = new ImageHelper();
      imageHelper.setDistance(String.valueOf(i));
      imageHelper.setRate_f_s(rate_f_s);
      imageHelper.setRate_c_s(rate_c_s);
      imageHelperList2.add(imageHelper);
    }

    model.addAttribute("imageHelperList2", imageHelperList2);

    respSample = _respSample;
    opOKSample = _opOKSample;
    loadSample = _loadSample;
    System.out.println("loadsamp[0][1]:"+loadSample[0][1]);
    return "lbrate_load";
  }


  @RequestMapping(value = "/lbrate_resp/", method = RequestMethod.GET)
  public String lbrate(Model model,
          @RequestParam(value = "latestWindowSize", defaultValue = "100")int latestWS,
          @RequestParam(value = "avgWindowSize", defaultValue = "5000")int avgWS
  ) {

    double[][] respSample = new double[][] { { 0.060d, 0.001d }, // 60ms, fast
            { 0.200d, 0.01d }, // 200ms, common
            { 0.500d, 0.001d }// 500ms, slow
    };

    double[] opOKSample = new double[] { 0.9999d, // ten-thousandth
            0.999d, // thousandth
            0.05d, // 5%
    };

    double[][] loadSample = new double[][] { { 20d, 0.01d }, // lightweight
            { 40d, 0.01d }, // normal
            { 60d, 0.01d }// overload
    };


    System.out.println("respSample[0][1]:"+respSample[0][1]);

    int array[] = new int[] { 0, 1, 2 };
    DynamicLB dynamicLB = new DynamicLB(array.length, 1000, 5000);

    //1.rate 随 resp时间变化的曲线图
    double respAvg = 0.250;
    double gap = 0.010;
    List<ImageHelper> imageHelperList = new ArrayList<>();

    for (int i = 0; i <= 24; i++) {
      respSample[0][0] = respAvg - i * gap;
      respSample[1][0] = respAvg;
      respSample[2][0] = respAvg + i * gap;

      //直接调用LB
      double[] possbility = simulation(array, 1000, dynamicLB, respSample, loadSample, opOKSample); //count=1000

      double rate_f_s = possbility[0] / possbility[2];
      double rate_c_s = possbility[1] / possbility[2];
      ImageHelper imageHelper = new ImageHelper();
      imageHelper.setDistance(String.valueOf(i));
      imageHelper.setRate_f_s(rate_f_s);
      imageHelper.setRate_c_s(rate_c_s);

      imageHelperList.add(imageHelper);
    }
    model.addAttribute("imageHelperList", imageHelperList);

    return "lbrate";
  }


  @RequestMapping(value = "/lbrate/", method = RequestMethod.GET)
  public String threeinone(Model model
  ) {

    double[][] respSample = new double[][] { { 0.060d, 0.001d }, // 60ms, fast
            { 0.200d, 0.01d }, // 200ms, common
            { 0.500d, 0.001d }// 500ms, slow
    };

    double[] opOKSample = new double[] { 0.9999d, // ten-thousandth
            0.999d, // thousandth
            0.05d, // 5%
    };

    double[][] loadSample = new double[][] { { 20d, 0.01d }, // lightweight
            { 40d, 0.01d }, // normal
            { 60d, 0.01d }// overload
    };

    int array[] = new int[] { 0, 1, 2 };
    DynamicLB dynamicLB = new DynamicLB(array.length, 1000, 5000);


    //1.rate 随 resp时间变化的曲线图
    double respAvg = 0.250;
    double gap = 0.010;
    List<ImageHelper> imageHelperList = new ArrayList<>();

    for (int i = 0; i <= 24; i++) {
      respSample[0][0] = respAvg - i * gap;
      respSample[1][0] = respAvg;
      respSample[2][0] = respAvg + i * gap;

      //直接调用LB
      double[] possbility = simulation(array, 1000, dynamicLB, respSample, loadSample, opOKSample); //count=1000

      double rate_f_s = possbility[0] / possbility[2];
      double rate_c_s = possbility[1] / possbility[2];
      ImageHelper imageHelper = new ImageHelper();
      imageHelper.setDistance(String.valueOf(i));
      imageHelper.setRate_f_s(rate_f_s);
      imageHelper.setRate_c_s(rate_c_s);

      imageHelperList.add(imageHelper);
    }
    model.addAttribute("imageHelperList", imageHelperList);


//2. rate随失败率变化的曲线
    respSample = new double[][] { { 0.060d, 0.001d }, // 60ms, fast
            { 0.200d, 0.01d }, // 200ms, common
            { 0.500d, 0.001d }// 500ms, slow
    };
    double okAvg = 0.50;
    double okGap = 0.02;
    List<ImageHelper> imageHelperList1 = new ArrayList<>();

    for (int i = 0; i <= 24; i++) {
      opOKSample[2] = okAvg - i * okGap;
      opOKSample[1] = okAvg;
      opOKSample[0] = okAvg + i * okGap;

      //直接调用LB
      double[] possbility = simulation(array, 1000, dynamicLB, respSample, loadSample, opOKSample); //count=1000

      double rate_f_s = possbility[0] / possbility[2];
      double rate_c_s = possbility[1] / possbility[2];
      ImageHelper imageHelper = new ImageHelper();
      imageHelper.setDistance(String.valueOf(i));
      imageHelper.setRate_f_s(rate_f_s);
      imageHelper.setRate_c_s(rate_c_s);
      imageHelperList1.add(imageHelper);
    }

    model.addAttribute("imageHelperList1", imageHelperList1);



    //3.rate 随node变化的曲线
    loadSample = new double[][] { { 20d, 0.01d }, // lightweight
            { 40d, 0.01d }, // normal
            { 60d, 0.01d }// overload
    };

    double loadAvg = 25d;
    double load_gap = 1d;
    List<ImageHelper> imageHelperList2 = new ArrayList<>();

    for (int i = 0; i <= 24; i++) {
      loadSample[0][0] = loadAvg - i * load_gap;
      loadSample[1][0] = loadAvg;
      loadSample[2][0] = loadAvg + i * load_gap;
      System.out.println(respSample[0][0]);

      //直接调用LB
      double[] possbility = simulation(array, 1000, dynamicLB, respSample, loadSample, opOKSample); //count=1000

      double rate_f_s = possbility[0] / possbility[2];
      double rate_c_s = possbility[1] / possbility[2];
      ImageHelper imageHelper = new ImageHelper();
      imageHelper.setDistance(String.valueOf(i));
      imageHelper.setRate_f_s(rate_f_s);
      imageHelper.setRate_c_s(rate_c_s);
      imageHelperList2.add(imageHelper);
    }

    model.addAttribute("imageHelperList2", imageHelperList2);





    return "threeinone";
  }







  @RequestMapping(value = "/charts/multithread/", method = RequestMethod.GET)
  public String chartsViewWithCallable(Model model, @RequestParam(value = "city", defaultValue =
      "上海")String
      city, @RequestParam(value = "keyword", defaultValue = "Java")String
      keyword) {
    //查询多个不同工作经验的薪资水平.

    final ExecutorService service;
    final Future<?> task1;
    final Future<?> task2;
    final Future<?> task3;
    final Future<?> task4;
    final Future<?> task5;

    service = Executors.newFixedThreadPool(5);
    task1 = service.submit(new CaculateThread(city, keyword, "应届毕业生", this.reportService));
    task2 = service.submit(new CaculateThread(city, keyword, "1年以下", this.reportService));
    task3 = service.submit(new CaculateThread(city, keyword, "1-3年", this.reportService));
    task4 = service.submit(new CaculateThread(city, keyword, "3-5年", this.reportService));
    task5 = service.submit(new CaculateThread(city, keyword, "5-10年", this.reportService));

    try {

      // waits the 10 seconds for the Callable.call to finish.
      SalaryQueryResult result1 = (SalaryQueryResult) task1.get(); // this raises
      SalaryQueryResult result2 = (SalaryQueryResult) task2.get(); // this raises
      SalaryQueryResult result3 = (SalaryQueryResult) task3.get(); // this raises
      SalaryQueryResult result4 = (SalaryQueryResult) task4.get(); // this raises
      SalaryQueryResult result5 = (SalaryQueryResult) task5.get(); // this raises

      // ExecutionException if thread dies
//      System.out.println(result);
      model.addAttribute("result1", result1);
      model.addAttribute("result2", result2);
      model.addAttribute("result3", result3);
      model.addAttribute("result4", result4);
      model.addAttribute("result5", result5);
    } catch (final InterruptedException ex) {
      ex.printStackTrace();
    } catch (final ExecutionException ex) {
      ex.printStackTrace();
    }

    service.shutdownNow();

    return "charts";
  }



  /**
   * 更新响应时间，失败率，负载load，更新后重新计算每个节点的选择概率
   * @param profile
   * @param count
   * @param lb
   * @param respSample
   * @param loadSample
   * @param opOKSample
   */
  static double[] simulation(int[] profile, int count, DynamicLB lb, double[][] respSample, double[][] loadSample,
          double[] opOKSample) {
    Random r = new Random();
    //生成正态分布数组
    NormalDistribution[] profiles = new NormalDistribution[profile.length];

    for (int i = 0; i < profile.length; i++) {
      //根据sample中的均值和标准差为每个节点生成对应的正态分布

      profiles[i] = new NormalDistribution(respSample[profile[i]][0], respSample[profile[i]][1]);
    }
    NormalDistribution[] loads = new NormalDistribution[profile.length];
    for (int i = 0; i < profile.length; i++) {
      loads[i] = new NormalDistribution(loadSample[profile[i]][0], loadSample[profile[i]][1]);
    }

    int[] load = new int[lb.nodeCount()];
    for (int j = 0; j < lb.nodeCount(); j++) {
      for (int i = 0; i < count; i++) { // how many records
        //根据标准正态分布来随机生成响应时间，接近均值的值生成可能性大
        double resp = profiles[j].sample();
        boolean ok = r.nextDouble() < opOKSample[profile[j]] ? true : false;
        lb.updateOneResp(j, resp, ok);
      }
      load[j] = Double.valueOf(loads[profile[j]].sample()).intValue();
    }
    lb.updateActive(load);

    return lb.flashChoice();
  }









}



