package com.striveen.express.util;

public class ProgressGet100Util
{
  public static String get100Progress(long currentPosition, long totalPosition)
  {
    return (int)(currentPosition / totalPosition * 100.0D) + "%";
  }

  public static String get100_0Progress(long currentPosition, long totalPosition)
  {
    return get100_n_0Progress(currentPosition, totalPosition, 1);
  }

  public static String get100_00Progress(long currentPosition, long totalPosition)
  {
    return get100_n_0Progress(currentPosition, totalPosition, 2);
  }

  public static String get100_000Progress(long currentPosition, long totalPosition)
  {
    return get100_n_0Progress(currentPosition, totalPosition, 3);
  }

  public static String get100_0000Progress(long currentPosition, long totalPosition)
  {
    return get100_n_0Progress(currentPosition, totalPosition, 4);
  }

  public static String get100_n_0Progress(long currentPosition, long totalPosition, int n)
  {
    if (n == 0) {
      return get100Progress(currentPosition, totalPosition);
    }

    int weishu = n;
    String p = currentPosition / totalPosition * 100.0D+"";
    int index = p.indexOf(".") + 1;
    if (index == 0)
      p = strEndAdd0(p + ".", weishu);
    else if (index + weishu >= p.length())
      p = strEndAdd0(p, index + weishu - p.length());
    else {
      p = p.substring(0, index + weishu);
    }
    return p + "%";
  }

  private static String strEndAdd0(String str, int i)
  {
    for (int j = 0; j < i; j++) {
      str = str + "0";
    }
    return str;
  }
}