/*    */ package com.striveen.express.runmethodinthread;
/*    */ 
/*    */ public class RunMITObjectQueue extends RunMITQueue
/*    */ {
/*    */   private Object object;
/*    */ 
/*    */   final boolean isStaticClass()
/*    */   {
/* 20 */     return false;
/*    */   }
/*    */ 
/*    */   Object getObject()
/*    */   {
/* 27 */     return this.object;
/*    */   }
/*    */ 
/*    */   public void setObject(Object object)
/*    */   {
/* 34 */     this.object = object;
/*    */   }
/*    */ }

/* Location:           D:\dev\AymUtil_1.1.2.jar
 * Qualified Name:     aym.util.runmetodinthread.RunMITObjectQueue
 * JD-Core Version:    0.6.2
 */