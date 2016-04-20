package org.codingwater.model.apiresp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.codingwater.constants.APIStatusEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by water on 4/20/16.
 */
public class APIListResponseDTO {

  private int status;
  private String message;
  private Map<String, Object> data;


  public APIListResponseDTO(APIStatusEnum apiStatus, String message) {
    this.status = apiStatus.getValue();
    this.message = message;
  }

  public APIListResponseDTO(boolean hasMore, int limit, long totalCount, int nextStart,
      List<Object> objectList) {
    this.status = APIStatusEnum.SUCCESS.getValue();
    this.data = Maps.newHashMap();
    data.put("more", hasMore ? 1 : 0);
    data.put("total", totalCount);
    data.put("limit", limit);
    data.put("next_start", nextStart);
    if (null == objectList) {
      objectList = Lists.newArrayList();
    }
    data.put("object_list", objectList);
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }
}
