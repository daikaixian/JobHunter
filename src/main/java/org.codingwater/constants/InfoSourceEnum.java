package org.codingwater.constants;

/**
 * Created by water on 4/19/16.
 */
public enum InfoSourceEnum {

  Lagou(1), Neitui(2), Liepin(3);

  int value;

  private InfoSourceEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }


}
