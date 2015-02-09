package com.striveen.express.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public final class JsonParseHelper
{
  private static final String TAG = "JsonParseHelper";

  public static String getAttribute(String jsonStr, String[] attributeNames)
  {
    String attributeValue = null;
    try {
      JSONObject object = new JSONObject(jsonStr);
      int j = attributeNames.length - 1;
      for (int i = 0; i < j; i++) {
        attributeValue = object.getString(attributeNames[i]);
        object = new JSONObject(attributeValue.toString());
      }
      attributeValue = object.getString(attributeNames[j]);
    } catch (JSONException e) {
      Log.w("JsonParseHelper", e.getClass().getName() + " : " + e.getMessage());
      attributeValue = "";
    }
    return attributeValue;
  }

  public static String getAttribute(String jsonStr, int[] arrayIndexs, String attributeName)
  {
    String attributeValue = null;
    try
    {
      JSONArray array = new JSONArray(jsonStr);
      int j = arrayIndexs.length - 1;
      for (int i = 0; i < j; i++) {
        Object o = array.get(arrayIndexs[i]);
        array = new JSONArray(o.toString());
      }
      Object o = array.get(arrayIndexs[j]);
      JSONObject object = new JSONObject(o.toString());
      attributeValue = object.getString(attributeName);
    } catch (JSONException e) {
      Log.w("JsonParseHelper", e.getClass().getName() + " : " + e.getMessage());
      attributeValue = "";
    }
    return attributeValue;
  }

  public static Map<String, Object> getMap(String jsonString)
  {
    Map valueMap;
    try
    {
      JSONObject jsonObject = new JSONObject(jsonString);
      Iterator keyIter = jsonObject.keys();

      valueMap = new HashMap();
      while (keyIter.hasNext()) {
        String key = (String)keyIter.next();
        Object value = jsonObject.get(key);
        valueMap.put(key, value);
      }
    } catch (JSONException e) {
      Log.w("JsonParseHelper", e.getClass().getName() + " : " + e.getMessage());
      valueMap = new HashMap();
    }
    return valueMap;
  }

  public static List<Map<String, Object>> getList(String jsonString)
  {
    List list = null;
    try {
      JSONArray jsonArray = new JSONArray(jsonString);

      list = new ArrayList();
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        list.add(getMap(jsonObject.toString()));
      }
    } catch (Exception e) {
      Log.w("JsonParseHelper", e.getClass().getName() + " : " + e.getMessage());
      list = new ArrayList();
    }
    return list;
  }

  public static Map<String, Object> getMap_Map(String jsonString, String key)
  {
    Map map = getMap(getAttribute(jsonString, 
      new String[] { key }));
    return map;
  }

  public static List<Map<String, Object>> getMap_List(String jsonString, String key)
  {
    return getList(getAttribute(jsonString, new String[] { key }));
  }

  public static List<Map<String, Object>> getMap_Map_List(String jsonString, String key, String key_key)
  {
    return getList(getAttribute(jsonString, new String[] { key, key_key }));
  }

  public static JsonMap<String, Object> getJsonMap(String jsonString)
  {
    JsonMap valueMap;
    try
    {
      JSONObject jsonObject = new JSONObject(jsonString);
      Iterator keyIter = jsonObject.keys();

      valueMap = new JsonMap();
      while (keyIter.hasNext()) {
        String key = (String)keyIter.next();
        Object value = jsonObject.get(key);
        valueMap.put(key, value);
      }
    } catch (JSONException e) {
      Log.w("JsonParseHelper", e.getClass().getName() + " : " + e.getMessage());
      valueMap = new JsonMap();
    }
    return valueMap;
  }

  public static List<JsonMap<String, Object>> getList_JsonMap(String jsonString)
  {
    List list = null;
    try {
      JSONArray jsonArray = new JSONArray(jsonString);

      list = new ArrayList();
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        list.add(getJsonMap(jsonObject.toString()));
      }
    } catch (Exception e) {
      Log.w("JsonParseHelper", e.getClass().getName() + " : " + e.getMessage());
      list = new ArrayList();
    }
    return list;
  }

  public static JsonMap<String, Object> getJsonMap_JsonMap(String jsonString, String key)
  {
    JsonMap map = getJsonMap(getAttribute(jsonString, 
      new String[] { key }));
    return map;
  }

  public static List<JsonMap<String, Object>> getJsonMap_List_JsonMap(String jsonString, String key)
  {
    return getList_JsonMap(getAttribute(jsonString, new String[] { key }));
  }

  public static List<JsonMap<String, Object>> getJsonMap_JsonMap_List_JsonMap(String jsonString, String key, String key_key)
  {
    return getList_JsonMap(getAttribute(jsonString, new String[] { key, 
      key_key }));
  }
}