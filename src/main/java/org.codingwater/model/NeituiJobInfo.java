package org.codingwater.model;

import org.codingwater.constants.InfoSourceEnum;

/**
 * Created by water on 4/19/16.
 */
public class NeituiJobInfo extends BaseJobInfo {

  public NeituiJobInfo() {
    this.setInfoSource(InfoSourceEnum.Neitui.getValue());
  }

}
