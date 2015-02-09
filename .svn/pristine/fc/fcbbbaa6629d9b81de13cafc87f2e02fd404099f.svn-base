/*    */ package com.striveen.express.view;
/*    */ 
/*    */ public class AsyImgFileCacheUtil
/*    */ {
/*    */   public String imgPathToFilePath_PNG(String imgPath)
/*    */   {
/* 34 */     imgPath = imgPathToFilePath(imgPath);
/* 35 */     imgPath = imgPath + ".png";
/* 36 */     return imgPath;
/*    */   }
/*    */ 
/*    */   private String imgPathToFilePath(String imgPath)
/*    */   {
/* 47 */     int i = imgPath.lastIndexOf(".");
/* 48 */     int j = imgPath.lastIndexOf("/");
/* 49 */     if ((i > 0) && (i > j)) {
/* 50 */       imgPath = imgPath.substring(0, i);
/*    */     }
/* 52 */     if (imgPath.length() > 8) {
/* 53 */       imgPath = imgPath.substring(7);
/*    */     }
/*    */ 
/* 56 */     imgPath = imgPath.replaceAll(".jpg", "");
/* 57 */     imgPath = imgPath.replaceAll(".jpeg", "");
/* 58 */     imgPath = imgPath.replaceAll(".png", "");
/* 59 */     imgPath = imgPath.replaceAll(".JPG", "");
/* 60 */     imgPath = imgPath.replaceAll(".JPEG", "");
/* 61 */     imgPath = imgPath.replaceAll(".PNG", "");
/*    */ 
/* 63 */     imgPath = imgPath.replaceAll("//", "/");
/* 64 */     imgPath = imgPath.replaceAll("//", "/");
/* 65 */     imgPath = imgPath.replaceAll("/", ".");
/* 66 */     imgPath = imgPath.replaceAll(":", ".");
/* 67 */     return imgPath;
/*    */   }
/*    */ }
