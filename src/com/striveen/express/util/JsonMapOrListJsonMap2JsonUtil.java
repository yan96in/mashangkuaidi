package com.striveen.express.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class JsonMapOrListJsonMap2JsonUtil<K, V>
  implements Serializable
{
  private static final long serialVersionUID = 1L;

  public String map2Json(Map<K, V> data)
  {
    if ((data == null) || (data.size() == 0)) {
      return "{}";
    }
    StringBuilder sb = new StringBuilder("{");
    Set ks = data.keySet();
    for (Object k : ks) {
      sb.append("\"" + k.toString() + "\":");
      Object v = data.get(k);
      if ((v instanceof Number)) {
        sb.append(v + ",");
      }
      else if (v.getClass().equals(ArrayList.class)) {
        sb.append(listJsonMap2Json((List)v) + 
          ",");
      }
      else if (v.getClass().equals(HashMap.class))
        sb.append(map2Json((Map)v) + ",");
      else if (v.getClass().equals(JsonMap.class))
        sb.append(map2Json((Map)v) + ",");
      else {
        sb.append("\"" + v + "\",");
      }

    }

    sb.deleteCharAt(sb.length() - 1);

    sb.append("}");
    return sb.toString();
  }

  public String listJsonMap2Json(List<? extends Map<K, V>> data)
  {
    if ((data == null) || (data.size() == 0)) {
      return "[]";
    }
    StringBuilder sb = new StringBuilder("[");
    for (Map map : data) {
      sb.append(map2Json(map) + ",");
    }

    sb.deleteCharAt(sb.length() - 1);

    sb.append("]");
    return sb.toString();
  }
}