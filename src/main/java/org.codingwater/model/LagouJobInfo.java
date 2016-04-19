package org.codingwater.model;

import org.codingwater.constants.InfoSourceEnum;

/**
 * Created by water on 4/19/16.
 */
public class LagouJobInfo extends BaseJobInfo{

  public LagouJobInfo() {
    this.setInfoSource(InfoSourceEnum.Lagou.getValue());
  }


}
