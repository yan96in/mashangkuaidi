/*     */ package com.striveen.express.view;
/*     */ 
 import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

 /*     */ public class LoadMoreListView extends ListView
/*     */   implements AbsListView.OnScrollListener, View.OnClickListener
/*     */ {
/*  26 */   private final String TAG = getClass().getSimpleName();
/*     */   private Context context;
/*  34 */   private int currentPage = 0;
/*     */ 
/*  38 */   private int totalPage = 2147483647;
/*     */ 
/*  43 */   private int pageSize = 10;
/*     */   private View loadMoreView;
/*     */   private OnLoadMoreDataListener onLoadMoreDataListener;
/*  56 */   private int visibleLastIndex = 0;
/*     */   private int visibleItemCount;
/*  64 */   private boolean isLoadFinshed = true;
/*     */   private ListAdapter adapter;
/*  73 */   private boolean isAutoLoadMore = false;
/*     */ 
/*  77 */   private boolean autoCorrectCurrentPage = false;
/*     */ 
/* 356 */   private DataSetObserver observer = new DataSetObserver()
/*     */   {
/*     */     public void onChanged()
/*     */     {
/* 360 */       super.onChanged();
/* 361 */       LoadMoreListView.this.loadFinished();
/*     */     }
/* 356 */   };
/*     */ 
/*     */   public LoadMoreListView(Context context)
/*     */   {
/*  80 */     super(context);
/*  81 */     this.context = context;
/*     */   }
/*     */ 
/*     */   public LoadMoreListView(Context context, AttributeSet attrs) {
/*  85 */     super(context, attrs);
/*  86 */     this.context = context;
/*     */   }
/*     */ 
/*     */   public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
/*  90 */     super(context, attrs, defStyle);
/*  91 */     this.context = context;
/*     */   }
/*     */ 
/*     */   public void setLoadMoreView(View loadMoreView)
/*     */   {
/* 100 */     this.loadMoreView = loadMoreView;
/*     */   }
/*     */ 
/*     */   public void setOnLoadMoreDataListener(OnLoadMoreDataListener onLoadMoreDataListener)
/*     */   {
/* 111 */     this.onLoadMoreDataListener = onLoadMoreDataListener;
/* 112 */     if (this.loadMoreView != null)
/* 113 */       this.loadMoreView.setOnClickListener(this);
/*     */   }
/*     */ 
/*     */   public void setAutoLoadMore(boolean isAutoLoadMore)
/*     */   {
/* 124 */     this.isAutoLoadMore = isAutoLoadMore;
/*     */   }
/*     */ 
/*     */   public void setTotalPage(int totalPage)
/*     */   {
/* 133 */     if (totalPage < 1)
/* 134 */       totalPage = 1;
/*     */     else
/* 136 */       this.totalPage = totalPage;
/*     */   }
/*     */ 
/*     */   public void setTotalSize(long totalSize)
/*     */   {
/* 147 */     if (totalSize < 0L) {
/* 148 */       totalSize = 0L;
/*     */     }
/* 150 */     int tatalPage = 2147483647;
/*     */     try {
/* 152 */       tatalPage = (int)(totalSize / this.pageSize);
/*     */     } catch (Exception e) {
/* 154 */       android.util.Log.w(this.TAG, "tatalPage 超过 int 最大值");
/*     */     }
/*     */ 
/* 157 */     if (totalSize % this.pageSize == 0L)
/* 158 */       setTotalPage(tatalPage);
/*     */     else
/* 160 */       setTotalPage(tatalPage + 1);
/*     */   }
/*     */ 
/*     */   public int getCurrentPage()
/*     */   {
/* 170 */     return this.currentPage;
/*     */   }
/*     */ 
/*     */   public int getNextPage()
/*     */   {
/* 179 */     return this.currentPage + 1;
/*     */   }
/*     */ 
/*     */   public int getPageSize()
/*     */   {
/* 188 */     return this.pageSize;
/*     */   }
/*     */ 
/*     */   public void setPageSize(int pageSize)
/*     */   {
/* 199 */     long totalSize = this.pageSize * this.totalPage;
/*     */ 
/* 201 */     if (pageSize < 1)
/* 202 */       this.pageSize = 10;
/*     */     else {
/* 204 */       this.pageSize = pageSize;
/*     */     }
/*     */ 
/* 207 */     setTotalSize(totalSize);
/*     */   }
/*     */ 
/*     */   public void openAutoCorrectCurrentPage(int pageSize)
/*     */   {
/* 217 */     setPageSize(pageSize);
/* 218 */     this.autoCorrectCurrentPage = true;
/*     */   }
/*     */ 
/*     */   public void closeAutoCorrectCurrentPage()
/*     */   {
/* 225 */     this.autoCorrectCurrentPage = false;
/*     */   }
/*     */ 
/*     */   private void loadFinished()
/*     */   {
/* 232 */     if (this.onLoadMoreDataListener != null) {
/* 233 */       this.onLoadMoreDataListener.loadMoreFinish(this.loadMoreView);
/*     */     }
/*     */ 
/* 236 */     correctCurrentPage();
/* 237 */     this.isLoadFinshed = true;
/*     */   }
/*     */ 
/*     */   private void correctCurrentPage()
/*     */   {
/* 244 */     if (this.adapter != null) {
/* 245 */       if (this.autoCorrectCurrentPage) {
/* 246 */         int count = this.adapter.getCount();
/* 247 */         if (count % this.pageSize == 0)
/* 248 */           this.currentPage = (count / this.pageSize);
/*     */         else
/* 250 */           this.currentPage = (count / this.pageSize + 1);
/*     */       }
/*     */       else
/*     */       {
/* 254 */         this.currentPage += 1;
/*     */       }
/*     */     }
/* 257 */     else this.currentPage = 0;
/*     */ 
/* 259 */     if ((this.currentPage >= this.totalPage) || (this.totalPage == 1))
/*     */       try {
/* 261 */         super.removeFooterView(this.loadMoreView);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   public void loadDataError()
/*     */   {
/* 272 */     loadFinished();
/*     */   }
/*     */ 
/*     */   public void setAdapter(ListAdapter adapter)
/*     */   {
/* 281 */     if (this.adapter != null) {
/* 282 */       if (this.loadMoreView != null) {
/* 283 */         if (this.onLoadMoreDataListener != null)
/* 284 */           this.onLoadMoreDataListener.loadMoreFinish(this.loadMoreView);
/*     */         try
/*     */         {
/* 287 */           super.removeFooterView(this.loadMoreView);
/*     */         } catch (Exception localException) {
/*     */         }
/*     */       }
/*     */       try {
/* 292 */         this.adapter.unregisterDataSetObserver(this.observer);
/*     */       }
/*     */       catch (Exception localException1) {
/*     */       }
/*     */     }
/* 297 */     this.adapter = adapter;
/* 298 */     if ((this.loadMoreView != null) && (adapter != null)) {
/*     */       try {
/* 300 */         removeFooterView(this.loadMoreView);
/* 301 */         super.addFooterView(this.loadMoreView);
/*     */       } catch (Exception localException2) {
/*     */       }
/* 304 */       this.currentPage = 0;
/* 305 */       correctCurrentPage();
/* 306 */       this.loadMoreView.setOnClickListener(this);
/* 307 */       setOnScrollListener(this);
/* 308 */       adapter.registerDataSetObserver(this.observer);
/*     */     }
/* 310 */     if (adapter == null) {
/* 311 */       this.currentPage = 0;
/*     */     }
/* 313 */     super.setAdapter(adapter);
/*     */   }
/*     */ 
/*     */   public void addFooterView(View v)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount)
/*     */   {
/* 329 */     this.visibleItemCount = visibleItemCount;
/* 330 */     this.visibleLastIndex = (firstVisibleItem + visibleItemCount - 1);
/*     */   }
/*     */ 
/*     */   public void onScrollStateChanged(AbsListView view, int scrollState)
/*     */   {
/* 337 */     if (this.adapter != null) {
/* 338 */       int itemsLastIndex = this.adapter.getCount() - 1;
/* 339 */       int lastIndex = itemsLastIndex + 1;
/* 340 */       if ((scrollState == 0) && 
/* 341 */         (this.visibleLastIndex == lastIndex))
/*     */       {
/* 343 */         if ((this.isAutoLoadMore) && (this.onLoadMoreDataListener != null) && 
/* 344 */           (this.loadMoreView != null) && (this.currentPage < this.totalPage) && 
/* 345 */           (this.isLoadFinshed)) {
/* 346 */           this.onLoadMoreDataListener.loadMore(this.loadMoreView);
/* 347 */           this.isLoadFinshed = false;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onClick(View v)
/*     */   {
/* 370 */     if ((this.isLoadFinshed) && (this.onLoadMoreDataListener != null)) {
/* 371 */       this.onLoadMoreDataListener.loadMore(this.loadMoreView);
/* 372 */       this.isLoadFinshed = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface OnLoadMoreDataListener
/*     */   {
/*     */     public abstract void loadMore(View paramView);
/*     */ 
/*     */     public abstract void loadMoreFinish(View paramView);
/*     */   }
/*     */ }

/* Location:           D:\dev\AymUtil_1.1.2.jar
 * Qualified Name:     aym.view.listview.LoadMoreListView
 * JD-Core Version:    0.6.2
 */