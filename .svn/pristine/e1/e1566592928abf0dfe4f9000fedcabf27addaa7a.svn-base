package com.striveen.express.util;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public final class SdcardHelper
{
  public boolean ExistSDCard()
  {
    if (Environment.getExternalStorageState().equals(
      "mounted")) {
      return true;
    }
    return false;
  }

  public long getSDFreeSize()
  {
    File path = Environment.getExternalStorageDirectory();
    StatFs sf = new StatFs(path.getPath());

    long blockSize = sf.getBlockSize();

    long freeBlocks = sf.getAvailableBlocks();

    return freeBlocks * blockSize / 1024L;
  }

  public long getSDAllSize()
  {
    File path = Environment.getExternalStorageDirectory();
    StatFs sf = new StatFs(path.getPath());

    long blockSize = sf.getBlockSize();

    long allBlocks = sf.getBlockCount();

    return allBlocks * blockSize / 1024L;
  }
}