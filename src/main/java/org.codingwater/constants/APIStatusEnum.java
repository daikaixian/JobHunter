package org.codingwater.constants;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created by water on 4/20/16.
 */

public enum  APIStatusEnum {

  SUCCESS(1),

  INVALID_PARAM(2);

  public int apiCode;

  private APIStatusEnum(int value) {
    this.apiCode = value;
  }

  public int getValue() {
    return apiCode;
  }

}
