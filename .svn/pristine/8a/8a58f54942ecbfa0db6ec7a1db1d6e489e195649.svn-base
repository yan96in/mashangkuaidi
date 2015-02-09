/*     */ package com.striveen.express.runmethodinthread.old;
/*     */ 
/*     */ import java.lang.reflect.Method;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RunMethodInThreadUtil
/*     */ {
/*  20 */   private final String TAG = getClass().getSimpleName();
/*     */   private String methodName;
/*     */   private Object object;
/*     */   private Object[] parms;
/*     */   private IRunMetodInThreadCallBack callBack;
/*     */   private Handler handler;
/*  41 */   private final int what_ok = 1; private final int what_error = 2;
/*     */ 
/*  45 */   private boolean isRuning = false;
/*     */ 
/*     */   public RunMethodInThreadUtil()
/*     */   {
/*  53 */     this.handler = new Handler()
/*     */     {
/*     */       public void handleMessage(Message msg) {
/*  56 */         super.handleMessage(msg);
/*  57 */         if ((msg.what == 1) && (RunMethodInThreadUtil.this.callBack != null))
/*  58 */           RunMethodInThreadUtil.this.callBack.onSucess(msg.obj);
/*  59 */         else if ((msg.what == 2) && (RunMethodInThreadUtil.this.callBack != null))
/*  60 */           RunMethodInThreadUtil.this.callBack.onError((Exception)msg.obj);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public void run(String methodName, Object object, Object[] parms, IRunMetodInThreadCallBack callBack)
/*     */     throws IllegalStateException
/*     */   {
/*  80 */     if (this.isRuning) {
/*  81 */       throw new IllegalStateException();
/*     */     }
/*  83 */     this.methodName = methodName;
/*  84 */     this.object = object;
/*  85 */     this.parms = parms;
/*  86 */     this.callBack = callBack;
/*  87 */     new RunMethodInThread().start();
/*     */   }
/*     */ 
/*     */   private class RunMethodInThread extends Thread
/*     */   {
/*     */     private RunMethodInThread()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*  99 */       super.run();
/* 100 */       RunMethodInThreadUtil.this.isRuning = true;
/*     */       try {
/* 102 */         Class[] cs = new Class[RunMethodInThreadUtil.this.parms.length];
/* 103 */         for (int i = 0; i < RunMethodInThreadUtil.this.parms.length; i++) {
/* 104 */           cs[i] = RunMethodInThreadUtil.this.parms[i].getClass();
/*     */         }
/*     */ 
/* 107 */         Method method = null;
/* 108 */         method = RunMethodInThreadUtil.this.object.getClass().getMethod(RunMethodInThreadUtil.this.methodName, cs);
/*     */ 
/* 110 */         Object a = method.invoke(RunMethodInThreadUtil.this.object, RunMethodInThreadUtil.this.parms);
/* 111 */         if (RunMethodInThreadUtil.this.handler != null) {
/* 112 */           Message message = RunMethodInThreadUtil.this.handler.obtainMessage(1);
/* 113 */           message.obj = a;
/* 114 */           RunMethodInThreadUtil.this.handler.sendMessage(message);
/*     */         }
/*     */       } catch (Exception e) {
/* 117 */         Log.e(RunMethodInThreadUtil.this.TAG, e.getClass().getName() + " : " + e.getMessage());
/* 118 */         if (RunMethodInThreadUtil.this.handler != null) {
/* 119 */           Message message = RunMethodInThreadUtil.this.handler.obtainMessage(1);
/* 120 */           message.obj = e;
/* 121 */           RunMethodInThreadUtil.this.handler.sendMessage(message);
/*     */         }
/*     */       }
/* 124 */       RunMethodInThreadUtil.this.isRuning = false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dev\AymUtil_1.1.2.jar
 * Qualified Name:     aym.util.runmetodinthread.old.RunMethodInThreadUtil
 * JD-Core Version:    0.6.2
 */