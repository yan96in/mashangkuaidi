/*     */ package com.striveen.express.runmethodinthread;
/*     */ 
/*     */ import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RunMITUtil
/*     */ {
/*     */   private static RunMITUtil util;
/*  30 */   private Map<Integer, RunMITQueue> queues = new HashMap();
/*     */ 
/*  34 */   private int index = 0;
/*     */ 
/*  71 */   private final int what_ok = 0;
/*     */ 
/*  75 */   private final int what_error = -1;
/*     */ 
/*  79 */   private Handler handler = new Handler() {
/*     */     public void handleMessage(Message msg) {
/*  81 */       int index = msg.arg2;
/*  82 */       RunMITQueue q = (RunMITQueue)RunMITUtil.this.queues.get(Integer.valueOf(index));
/*  83 */       q.setRunReslutIsOk(msg.what == 0);
/*  84 */       if (q.isRunReslutIsOk()) {
/*  85 */         q.setResult(msg.obj);
/*  86 */         q.setException(null);
/*     */       } else {
/*  88 */         q.setResult(null);
/*  89 */         q.setException((Exception)msg.obj);
/*     */       }
/*  91 */       q.getCallBack().onRuned(q);
/*  92 */       synchronized (RunMITUtil.this.queues) {
/*  93 */         RunMITUtil.this.queues.remove(Integer.valueOf(index));
/*     */       }
/*     */     }
/*  79 */   };
/*     */ 
/*     */   private RunMITUtil()
/*     */   {
/*  40 */     Random random = new Random();
/*  41 */     this.index = random.nextInt(1000);
/*     */   }
/*     */ 
/*     */   public static RunMITUtil init()
/*     */   {
/*  50 */     if (util == null) {
/*  51 */       util = new RunMITUtil();
/*     */     }
/*  53 */     return util;
/*     */   }
/*     */ 
/*     */   public void runQueue(RunMITQueue queue)
/*     */   {
/*  60 */     this.index += 1;
/*  61 */     queue.setRmitqId(this.index);
/*  62 */     new RunMetodInThread(queue).start();
/*  63 */     synchronized (this.queues) {
/*  64 */       this.queues.put(Integer.valueOf(this.index), queue);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface IRunMITCallBack
/*     */   {
/*     */     public abstract void onRuned(RunMITQueue paramRunMITQueue);
/*     */   }
/*     */ 
/*     */   class RunMetodInThread extends Thread
/*     */   {
/* 105 */     private final String TAG = getClass().getSimpleName();
/*     */     private RunMITQueue queue;
/*     */ 
/*     */     public RunMetodInThread(RunMITQueue queue)
/*     */     {
/* 113 */       this.queue = queue;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 118 */       super.run();
/*     */       try {
/* 120 */         int parmsLength = 0;
/* 121 */         Class[] cs = null;
/* 122 */         if (this.queue.getParms() != null) {
/* 123 */           parmsLength = this.queue.getParms().length;
/* 124 */           cs = new Class[parmsLength];
/* 125 */           for (int i = 0; i < parmsLength; i++) {
/* 126 */             cs[i] = this.queue.getParms()[i].getClass();
/*     */           }
/*     */         }
/*     */ 
/* 130 */         Method method = null;
/* 131 */         Object result = null;
/* 132 */         if (this.queue.isStaticClass()) {
/* 133 */           RunMITStaticQueue staticQueue = (RunMITStaticQueue)this.queue;
/*     */ 
/* 136 */           method = searchClassMethod(staticQueue.getCls(), 
/* 137 */             this.queue.getMethodName(), cs);
/* 138 */           result = method.invoke(staticQueue.getCls(), 
/* 139 */             staticQueue.getParms());
/*     */         } else {
/* 141 */           RunMITObjectQueue objectQueue = (RunMITObjectQueue)this.queue;
/* 142 */           Object obj = objectQueue.getObject();
/* 143 */           Class c = obj.getClass();
/*     */ 
/* 146 */           method = searchClassMethod(obj.getClass(), 
/* 147 */             this.queue.getMethodName(), cs);
/* 148 */           result = method.invoke(obj, objectQueue.getParms());
/*     */         }
/* 150 */         if (RunMITUtil.this.handler != null) {
/* 151 */           Message message = RunMITUtil.this.handler.obtainMessage(0);
/* 152 */           message.obj = result;
/* 153 */           message.arg2 = this.queue.getRmitqId();
/* 154 */           RunMITUtil.this.handler.sendMessage(message);
/*     */         }
/*     */       } catch (Exception e) {
/* 157 */         Log.e(this.TAG, e.getClass().getName() + " : " + e.getMessage(), e);
/* 158 */         if (RunMITUtil.this.handler != null) {
/* 159 */           Message message = RunMITUtil.this.handler.obtainMessage(-1);
/* 160 */           message.obj = e;
/* 161 */           message.arg2 = this.queue.getRmitqId();
/* 162 */           RunMITUtil.this.handler.sendMessage(message);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private Method searchClassMethod(Class cls, String name, Class<?>[] parameterTypes)
/*     */       throws NoSuchMethodException
/*     */     {
/* 181 */       if ((parameterTypes == null) || (parameterTypes.length == 0)) {
/* 182 */         return cls.getMethod(name, null);
/*     */       }
/*     */ 
/* 185 */       Method method = null;
/*     */       try {
/* 187 */         method = cls.getMethod(name, parameterTypes);
/* 188 */         if (method != null) {
/* 189 */           return method;
/*     */         }
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 194 */         Method[] methods = cls.getMethods();
/* 195 */         for (int i = 0; i < methods.length; i++) {
/* 196 */           if (methods[i].getName().equals(name)) {
/* 197 */             Type[] types = methods[i].getGenericParameterTypes();
/* 198 */             if (types.length == parameterTypes.length) {
/* 199 */               int l = 0;
/* 200 */               for (int j = 0; j < types.length; j++)
/*     */               {
/* 202 */                 if (checkTypeEquals(types[j].getClass(), 
/* 202 */                   parameterTypes[j].getClass())) {
/* 203 */                   l++;
/*     */                 }
/*     */               }
/* 206 */               if (l == types.length) {
/* 207 */                 method = methods[i];
/* 208 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 213 */         if (method == null)
/* 214 */           throw new NoSuchMethodException(getName() + "." + name + 
/* 215 */             argumentTypesToString(parameterTypes));
/*     */       }
/* 217 */       return method;
/*     */     }
/*     */ 
/*     */     private boolean checkTypeEquals(Class cls1, Class cls2)
/*     */     {
/* 226 */       if (cls1.equals(cls2))
/* 227 */         return true;
/* 228 */       if (((cls1.equals(Integer.class)) && (Integer.TYPE.equals(cls2))) || 
/* 229 */         ((cls1.equals(Integer.TYPE)) && (Integer.class.equals(cls2))) || 
/* 230 */         ((cls1.equals(Float.class)) && (Float.TYPE.equals(cls2))) || 
/* 231 */         ((cls1.equals(Float.TYPE)) && (Float.class.equals(cls2))) || 
/* 232 */         ((cls1.equals(Double.class)) && (Double.TYPE.equals(cls2))) || 
/* 233 */         ((cls1.equals(Double.TYPE)) && (Double.class.equals(cls2))) || 
/* 234 */         ((cls1.equals(Boolean.class)) && (Boolean.TYPE.equals(cls2))) || 
/* 235 */         ((cls1.equals(Boolean.TYPE)) && (Boolean.class.equals(cls2))) || 
/* 236 */         ((cls1.equals(Long.class)) && (Long.TYPE.equals(cls2))) || 
/* 237 */         ((cls1.equals(Long.TYPE)) && (Long.class.equals(cls2))) || 
/* 238 */         ((cls1.equals(Character.class)) && (Character.TYPE.equals(cls2))) || 
/* 239 */         ((cls1.equals(Character.TYPE)) && (Character.class.equals(cls2))) || 
/* 240 */         ((cls1.equals(Byte.class)) && (Byte.TYPE.equals(cls2))) || 
/* 241 */         ((cls1.equals(Byte.TYPE)) && (Byte.class.equals(cls2))) || 
/* 242 */         ((cls1.equals(Short.class)) && (Short.TYPE.equals(cls2))) || (
/* 243 */         (cls1.equals(Short.TYPE)) && (Short.class.equals(cls2)))) {
/* 244 */         return true;
/*     */       }
/* 246 */       Class[] face = cls2.getInterfaces();
/* 247 */       for (Class class1 : face) {
/* 248 */         if (class1.equals(cls1)) {
/* 249 */           return true;
/*     */         }
/*     */       }
/*     */ 
/* 253 */       return false;
/*     */     }
/*     */ 
/*     */     private String argumentTypesToString(Class<?>[] argTypes)
/*     */     {
/* 263 */       StringBuilder buf = new StringBuilder();
/* 264 */       buf.append("(");
/* 265 */       if (argTypes != null) {
/* 266 */         for (int i = 0; i < argTypes.length; i++) {
/* 267 */           if (i > 0) {
/* 268 */             buf.append(", ");
/*     */           }
/* 270 */           Class c = argTypes[i];
/* 271 */           buf.append(c == null ? "null" : c.getName());
/*     */         }
/*     */       }
/* 274 */       buf.append(")");
/* 275 */       return buf.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dev\AymUtil_1.1.2.jar
 * Qualified Name:     aym.util.runmetodinthread.RunMITUtil
 * JD-Core Version:    0.6.2
 */