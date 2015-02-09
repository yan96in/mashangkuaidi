/*    */ package com.striveen.express.runmethodinthread.old;
/*    */ 
/*    */ import java.lang.reflect.Method;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThreadRunMethodThread extends Thread
/*    */ {
/* 19 */   private final String TAG = getClass().getSimpleName();
/*    */   private String methodName;
/*    */   private Object object;
/*    */   private Object[] parms;
/*    */   private Handler handler;
/* 29 */   private int what_error = -1;
/*    */ 
/* 31 */   private int what_ok = 0;
/*    */ 
/*    */   public ThreadRunMethodThread(String methodName, Object object, Object[] parms, Handler handler, int what_error, int what_ok)
/*    */   {
/* 52 */     this.methodName = methodName;
/* 53 */     this.object = object;
/* 54 */     this.parms = parms;
/* 55 */     this.handler = handler;
/* 56 */     this.what_error = what_error;
/* 57 */     this.what_ok = what_ok;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 62 */     super.run();
/*    */     try {
/* 64 */       Class[] cs = new Class[this.parms.length];
/* 65 */       for (int i = 0; i < this.parms.length; i++) {
/* 66 */         cs[i] = this.parms[i].getClass();
/*    */       }
/*    */ 
/* 69 */       Method method = null;
/* 70 */       method = this.object.getClass().getMethod(this.methodName, cs);
/*    */ 
/* 72 */       Object a = method.invoke(this.object, this.parms);
/* 73 */       if (this.handler != null) {
/* 74 */         Message message = this.handler.obtainMessage(this.what_ok);
/* 75 */         if (method.getReturnType().equals(Void.class)) {
/* 76 */           message.obj = "{\"code\":0,\"msg\":\"OK\"}";
/* 77 */         } else if (method.getReturnType().equals(String.class)) {
/* 78 */           message.obj = a;
/* 79 */         } else if (method.getReturnType().equals(Boolean.class)) {
/* 80 */           message.obj = a;
/* 81 */         } else if (method.getReturnType().equals(Double.class)) {
/* 82 */           message.obj = a;
/* 83 */         } else if (method.getReturnType().equals(Integer.class)) {
/* 84 */           message.obj = a;
/*    */         } else {
/* 86 */           message.what = this.what_error;
/* 87 */           message.obj = "{\"code\":-1,\"msg\":\"暂不支持\"}";
/*    */         }
/* 89 */         this.handler.sendMessage(message);
/*    */       }
/*    */     } catch (Exception e) {
/* 92 */       Log.e(this.TAG, e.getClass().getName() + " : " + e.getMessage());
/* 93 */       if (this.handler != null) {
/* 94 */         Message message = this.handler.obtainMessage(this.what_error);
/* 95 */         message.obj = 
/* 96 */           ("{\"code\":-1,\"msg\":\"" + e.getMessage() + 
/* 96 */           "\"}");
/* 97 */         this.handler.sendMessage(message);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\dev\AymUtil_1.1.2.jar
 * Qualified Name:     aym.util.runmetodinthread.old.ThreadRunMethodThread
 * JD-Core Version:    0.6.2
 */