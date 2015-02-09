package com.striveen.express.util;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class CharsetUtils
{
  private static final String TAG = CharsetUtils.class.getSimpleName();
  public static final String DEFAULT_ENCODING_CHARSET = "ISO-8859-1";
  public static final List<String> SUPPORT_CHARSET = new ArrayList();

  static {
    SUPPORT_CHARSET.add("ISO-8859-1");

    SUPPORT_CHARSET.add("GB2312");
    SUPPORT_CHARSET.add("GBK");
    SUPPORT_CHARSET.add("GB18030");

    SUPPORT_CHARSET.add("US-ASCII");
    SUPPORT_CHARSET.add("ASCII");

    SUPPORT_CHARSET.add("ISO-2022-KR");

    SUPPORT_CHARSET.add("ISO-8859-2");

    SUPPORT_CHARSET.add("ISO-2022-JP");
    SUPPORT_CHARSET.add("ISO-2022-JP-2");

    SUPPORT_CHARSET.add("UTF-8");
  }

  public static String toCharset(String str, String charset, int judgeCharsetLength)
  {
    try
    {
      String oldCharset = getEncoding(str, judgeCharsetLength);
      return new String(str.getBytes(oldCharset), charset);
    } catch (Throwable ex) {
      Log.e(TAG, ex.toString());
    }return str;
  }

  public static String getEncoding(String str, int judgeCharsetLength)
  {
    String encode = "ISO-8859-1";
    for (String charset : SUPPORT_CHARSET) {
      if (isCharset(str, charset, judgeCharsetLength)) {
        encode = charset;
        break;
      }
    }
    return encode;
  }

  public static boolean isCharset(String str, String charset, int judgeCharsetLength)
  {
    try {
      String temp = str.length() > judgeCharsetLength ? str.substring(0, 
        judgeCharsetLength) : str;
      return temp.equals(new String(temp.getBytes(charset), charset)); } catch (Throwable e) {
    }
    return false;
  }
}