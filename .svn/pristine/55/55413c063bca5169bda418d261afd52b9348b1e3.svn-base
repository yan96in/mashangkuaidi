package com.striveen.express.download;

import java.io.File;
import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class DownLoadQueue
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int id;
  private int iconResId;
  private String name;
  private long downSize = 0L;

  private long offSize = 1L;

  private boolean isCancel = false;
  private String downLoadUrl;
  private String downLoadedPath;
  private String fileName;
  private String openFileType;
  private boolean downLoadedAutoOpenFile;

  public int getId()
  {
    return this.id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public int getIconResId()
  {
    return this.iconResId;
  }

  public void setIconResId(int iconResId)
  {
    this.iconResId = iconResId;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public long getDownSize()
  {
    return this.downSize;
  }

  void setDownSize(long downSize)
  {
    this.downSize = downSize;
  }

  public long getOffSize()
  {
    return this.offSize;
  }

  void setOffSize(long offSize)
  {
    this.offSize = offSize;
  }

  public boolean isCancel()
  {
    return this.isCancel;
  }

  public void setCancel(boolean isCancel)
  {
    this.isCancel = isCancel;
  }

  public boolean isDownLoadOK()
  {
    return this.downSize == this.offSize;
  }

  public String getDownLoadUrl()
  {
    return this.downLoadUrl;
  }

  public void setDownLoadUrl(String downLoadUrl)
  {
    this.downLoadUrl = downLoadUrl;
  }

  public String getDownLoadedPath()
  {
    return this.downLoadedPath;
  }

  public void setDownLoadedPath(String downLoadedPath)
  {
    this.downLoadedPath = downLoadedPath;
  }

  public String getFileName()
  {
    return this.fileName;
  }

  void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  public String getOpenFileType()
  {
    return this.openFileType;
  }

  public void setOpenFileType(String openFileType)
  {
    this.openFileType = openFileType;
  }

  boolean isDownLoadedAutoopenFile()
  {
    return this.downLoadedAutoOpenFile;
  }

  public void setDownLoadedAutoOpenFile(boolean downLoadedAutoOpenFile, String openFileType)
  {
    this.downLoadedAutoOpenFile = downLoadedAutoOpenFile;
    setOpenFileType(openFileType);
  }

  public void OpenFile(Context context)
  {
    if (!isDownLoadOK()) {
      return;
    }
    if ((this.openFileType == null) || ("".equals(this.openFileType)))
      return;
    try
    {
      Intent intent = new Intent("android.intent.action.VIEW");
      Uri name = Uri.fromFile(new File(this.fileName));
      intent.setDataAndType(name, this.openFileType);
      intent.addFlags(268435456);
      context.startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
      try {
        Toast.makeText(context, "文件打开失败", 
          1).show();
      }
      catch (Exception localException1)
      {
      }
    }
  }
}