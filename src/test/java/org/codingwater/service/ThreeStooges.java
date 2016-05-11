package org.codingwater.service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by water on 5/9/16.
 */
public final class ThreeStooges {
  private final Set<String> stooges = new HashSet<String>();

  public ThreeStooges() {
    stooges.add("Moe");
    stooges.add("Larry");
    stooges.add("Curly");
  }
  public boolean isStooge(String name) {
    return stooges.contains(name);
  }
}
