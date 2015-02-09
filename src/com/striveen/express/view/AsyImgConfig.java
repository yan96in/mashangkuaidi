/*     */ package com.striveen.express.view;
/*     */ 
/*     */ import android.graphics.Bitmap;

import com.striveen.express.util.SdcardHelper;

/*     */ 
/*     */ public class AsyImgConfig
/*     */ {
/*  20 */   public static int imgFileSizeMaxValue = 716800;
/*     */ 
/*  24 */   public static boolean isCheckImgFileSizeMax = true;
/*     */   public static String appImgsFilesPath;
/*  33 */   public static LruMemoryCache<String, Bitmap> imgsCache = new LruMemoryCache(8388608);
/*     */ 
/*  38 */   public static boolean autoCreatScaled = true;
/*     */ 
/*     */   public static void init(boolean autoCreatScaled, int maxCacheSize)
/*     */   {
/*  49 */     init(autoCreatScaled, maxCacheSize, null, true);
/*     */   }
/*     */ 
/*     */   public static void init(boolean autoCreatScaled, int maxCacheSize, boolean isCheckImgFileSizeMax)
/*     */   {
/*  64 */     init(autoCreatScaled, maxCacheSize, null, isCheckImgFileSizeMax);
/*     */   }
/*     */ 
/*     */   public static void init(boolean autoCreatScaled, int maxCacheSize, String appCacheImgsPath, boolean isCheckImgFileSizeMax)
/*     */   {
/*  83 */     if (maxCacheSize > 1) {
/*  84 */       imgsCache = new LruMemoryCache(
/*  85 */         maxCacheSize * 1024 * 1024);
/*     */     }
/*  87 */     if (new SdcardHelper().ExistSDCard()) {
/*  88 */       if ((appCacheImgsPath != null) && (!"".equals(appCacheImgsPath)))
/*     */       {
/*  90 */         if ('/' == appCacheImgsPath
/*  91 */           .charAt(appCacheImgsPath.length() - 1))
/*  92 */           appImgsFilesPath = appCacheImgsPath;
/*     */         else
/*  94 */           appImgsFilesPath = appCacheImgsPath + "/";
/*     */       }
/*     */     }
/*     */     else {
/*  98 */       appImgsFilesPath = null;
/*     */     }
/* 100 */     isCheckImgFileSizeMax = isCheckImgFileSizeMax;
/*     */   }
/*     */ 
/*     */   public static void init(boolean autoCreatScaled, String appCacheImgsPath)
/*     */   {
/* 112 */     init(autoCreatScaled, 0, appCacheImgsPath, true);
/*     */   }
/*     */ 
/*     */   public static void init(String appCacheImgsPath)
/*     */   {
/* 123 */     init(true, 0, appCacheImgsPath, true);
/*     */   }
/*     */ 
/*     */   public static void init(int maxCacheSize, String appCacheImgsPath)
/*     */   {
/* 136 */     init(true, maxCacheSize, appCacheImgsPath, true);
/*     */   }
/*     */ 
/*     */   public static void init(boolean autoCreatScaled)
/*     */   {
/* 146 */     init(autoCreatScaled, 0, null, true);
/*     */   }
/*     */ 
/*     */   public static void init(int maxCacheSize)
/*     */   {
/* 156 */     init(true, maxCacheSize, null, true);
/*     */   }
/*     */ 
/*     */   public static void clearCache()
/*     */   {
/* 163 */     imgsCache.evictAll();
/*     */   }
/*     */ }

/* Location:           D:\dev\AymUtil_1.1.2.jar
 * Qualified Name:     aym.view.asyimgview.AsyImgConfig
 * JD-Core Version:    0.6.2
 */