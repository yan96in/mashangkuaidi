package com.striveen.express.adapter;

/*     */ import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.striveen.express.view.AsyImgView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleAsyImgAdapter extends BaseAdapter
/*     */   implements Filterable
/*     */ {
/*     */   private int[] mTo;
/*     */   private String[] mFrom;
/*     */   private String fliesPath;
/*     */   private SimpleAdapter.ViewBinder mViewBinder;
/*     */   private List<? extends Map<String, ?>> mData;
/*     */   private int mResource;
/*     */   private int mDropDownResource;
/*     */   private LayoutInflater mInflater;
/*     */   private SimpleFilter mFilter;
/*     */   private ArrayList<Map<String, ?>> mUnfilteredData;
/*     */   private int defaultImgResource;
/*     */ 
/*     */   public SimpleAsyImgAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, int defaultImgResource, String fliesPath)
/*     */   {
/*  72 */     this.mData = data;
/*  73 */     this.mResource = (this.mDropDownResource = resource);
/*  74 */     this.mFrom = from;
/*  75 */     this.mTo = to;
/*  76 */     this.mInflater = 
/*  77 */       ((LayoutInflater)context
/*  77 */       .getSystemService("layout_inflater"));
/*     */ 
/*  79 */     this.defaultImgResource = defaultImgResource;
/*  80 */     this.fliesPath = fliesPath;
/*     */   }
/*     */ 
/*     */   public SimpleAsyImgAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, int defaultImgResource)
/*     */   {
/* 102 */     this.mData = data;
/* 103 */     this.mResource = (this.mDropDownResource = resource);
/* 104 */     this.mFrom = from;
/* 105 */     this.mTo = to;
/* 106 */     this.mInflater = 
/* 107 */       ((LayoutInflater)context
/* 107 */       .getSystemService("layout_inflater"));
/* 108 */     this.defaultImgResource = defaultImgResource;
/*     */   }
/*     */ 
/*     */   public void setFliesPath(String fliesPath)
/*     */   {
/* 118 */     this.fliesPath = fliesPath;
/*     */   }
/*     */ 
/*     */   public int getCount()
/*     */   {
/* 125 */     return this.mData.size();
/*     */   }
/*     */ 
/*     */   public Object getItem(int position)
/*     */   {
/* 132 */     return this.mData.get(position);
/*     */   }
/*     */ 
/*     */   public long getItemId(int position)
/*     */   {
/* 139 */     return position;
/*     */   }
/*     */ 
/*     */   public View getView(int position, View convertView, ViewGroup parent)
/*     */   {
/* 146 */     return createViewFromResource(position, convertView, parent, this.mResource);
/*     */   }
/*     */ 
/*     */   private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource)
/*     */   {
/*     */     View v;
/* 155 */     if (convertView == null) {
/* 156 */        v = this.mInflater.inflate(resource, parent, false);
/*     */ 
/* 158 */       int[] to = this.mTo;
/* 159 */       int count = to.length;
/* 160 */       View[] holder = new View[count];
/*     */ 
/* 162 */       for (int i = 0; i < count; i++) {
/* 163 */         holder[i] = v.findViewById(to[i]);
/*     */       }
/*     */ 
/* 166 */       v.setTag(holder);
/*     */     } else {
/* 168 */       v = convertView;
/*     */     }
/*     */ 
/* 171 */     bindView(position, v);
/*     */ 
/* 173 */     return v;
/*     */   }
/*     */ 
/*     */   public void setDropDownViewResource(int resource)
/*     */   {
/* 186 */     this.mDropDownResource = resource;
/*     */   }
/*     */ 
/*     */   private void bindView(int position, View view)
/*     */   {
/* 197 */     Map dataSet = (Map)this.mData.get(position);
/* 198 */     if (dataSet == null) {
/* 199 */       return;
/*     */     }
/*     */ 
/* 202 */     SimpleAdapter.ViewBinder binder = this.mViewBinder;
/* 203 */     View[] holder = (View[])view.getTag();
/* 204 */     String[] from = this.mFrom;
/* 205 */     int[] to = this.mTo;
/* 206 */     int count = to.length;
/*     */ 
/* 208 */     for (int i = 0; i < count; i++) {
/* 209 */       View v = holder[i];
/* 210 */       if (v != null) {
/* 211 */         Object data = dataSet.get(from[i]);
/* 212 */         String text = data == null ? "" : data.toString();
/* 213 */         if (text == null) {
/* 214 */           text = "";
/*     */         }
/*     */ 
/* 217 */         boolean bound = false;
/* 218 */         if (binder != null) {
/* 219 */           bound = binder.setViewValue(v, data, text);
/*     */         }
/*     */ 
/* 222 */         if (!bound)
/* 223 */           if ((v instanceof Checkable)) {
/* 224 */             if ((data instanceof Boolean))
/* 225 */               ((Checkable)v).setChecked(((Boolean)data).booleanValue());
/*     */             else
/* 227 */               throw new IllegalStateException(v.getClass()
/* 228 */                 .getName() + 
/* 229 */                 " should be bound to a Boolean, not a " + 
/* 230 */                 data.getClass());
/*     */           }
/* 232 */           else if ((v instanceof TextView))
/*     */           {
/* 237 */             setViewText((TextView)v, text);
/* 238 */           } else if ((v instanceof AsyImgView)) {
/* 239 */             if ((data instanceof Integer))
/* 240 */               setViewAsyImage((AsyImgView)v, ((Integer)data).intValue());
/*     */             else
/* 242 */               setViewAsyImage((AsyImgView)v, text);
/*     */           }
/* 244 */           else if ((v instanceof ImageView)) {
/* 245 */             if ((data instanceof Integer))
/* 246 */               setViewImage((ImageView)v, ((Integer)data).intValue());
/*     */             else
/* 248 */               setViewImage((ImageView)v, text);
/*     */           }
/*     */           else
/* 251 */             throw new IllegalStateException(
/* 252 */               v.getClass().getName() + 
/* 253 */               " is not a " + 
/* 254 */               " view that can be bounds by this SimpleAsyImgAdapter");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public SimpleAdapter.ViewBinder getViewBinder()
/*     */   {
/* 269 */     return this.mViewBinder;
/*     */   }
/*     */ 
/*     */   public void setViewBinder(SimpleAdapter.ViewBinder viewBinder)
/*     */   {
/* 282 */     this.mViewBinder = viewBinder;
/*     */   }
/*     */ 
/*     */   public void setViewImage(ImageView v, int value)
/*     */   {
/* 301 */     v.setImageResource(value);
/*     */   }
/*     */ 
/*     */   public void setViewImage(ImageView v, String value)
/*     */   {
/*     */     try
/*     */     {
/* 324 */       v.setImageResource(Integer.parseInt(value));
/*     */     } catch (NumberFormatException nfe) {
/* 326 */       v.setImageURI(Uri.parse(value));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setViewAsyImage(AsyImgView v, String text)
/*     */   {
/* 341 */     v.setDefaultResource(this.defaultImgResource);
/*     */ 
/* 343 */     if ((this.fliesPath != null) || (!"".equals(this.fliesPath))) {
/* 344 */       v.setFliesPath(this.fliesPath);
/*     */     }
/* 346 */     v.setImgUrl(text);
/*     */   }
/*     */ 
/*     */   private void setViewAsyImage(AsyImgView v, int value)
/*     */   {
/* 360 */     v.setImageResource(value);
/*     */   }
/*     */ 
/*     */   public void setViewText(TextView v, String text)
/*     */   {
/* 374 */     v.setText(text);
/*     */   }
/*     */ 
/*     */   public Filter getFilter() {
/* 378 */     if (this.mFilter == null) {
/* 379 */       this.mFilter = new SimpleFilter();
/*     */     }
/* 381 */     return this.mFilter;
/*     */   }
/*     */ 
/*     */   private class SimpleFilter extends Filter
/*     */   {
/*     */     private SimpleFilter()
/*     */     {
/*     */     }
/*     */ 
/*     */     protected Filter.FilterResults performFiltering(CharSequence prefix)
/*     */     {
/* 395 */       Filter.FilterResults results = new Filter.FilterResults();
/*     */ 
/* 397 */       if (SimpleAsyImgAdapter.this.mUnfilteredData == null) {
/* 398 */         SimpleAsyImgAdapter.this.mUnfilteredData = new ArrayList(SimpleAsyImgAdapter.this.mData);
/*     */       }
/*     */ 
/* 401 */       if ((prefix == null) || (prefix.length() == 0)) {
/* 402 */         ArrayList list = SimpleAsyImgAdapter.this.mUnfilteredData;
/* 403 */         results.values = list;
/* 404 */         results.count = list.size();
/*     */       } else {
/* 406 */         String prefixString = prefix.toString().toLowerCase();
/*     */ 
/* 408 */         ArrayList unfilteredValues = SimpleAsyImgAdapter.this.mUnfilteredData;
/* 409 */         int count = unfilteredValues.size();
/*     */ 
/* 411 */         ArrayList newValues = new ArrayList(
/* 412 */           count);
/*     */ 
/* 414 */         for (int i = 0; i < count; i++) {
/* 415 */           Map h = (Map)unfilteredValues.get(i);
/* 416 */           if (h != null)
/*     */           {
/* 418 */             int len = SimpleAsyImgAdapter.this.mTo.length;
/*     */ 
/* 420 */             for (int j = 0; j < len; j++) {
/* 421 */               String str = (String)h.get(SimpleAsyImgAdapter.this.mFrom[j]);
/*     */ 
/* 423 */               String[] words = str.split(" ");
/* 424 */               int wordCount = words.length;
/*     */ 
/* 426 */               for (int k = 0; k < wordCount; k++) {
/* 427 */                 String word = words[k];
/*     */ 
/* 429 */                 if (word.toLowerCase().startsWith(prefixString)) {
/* 430 */                   newValues.add(h);
/* 431 */                   break;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 438 */         results.values = newValues;
/* 439 */         results.count = newValues.size();
/*     */       }
/*     */ 
/* 442 */       return results;
/*     */     }
/*     */ 
/*     */     protected void publishResults(CharSequence constraint, Filter.FilterResults results)
/*     */     {
/* 449 */       SimpleAsyImgAdapter.this.mData = ((List)results.values);
/* 450 */       if (results.count > 0)
/* 451 */         SimpleAsyImgAdapter.this.notifyDataSetChanged();
/*     */       else
/* 453 */         SimpleAsyImgAdapter.this.notifyDataSetInvalidated();
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\dev\AymUtil_1.1.2.jar
 * Qualified Name:     aym.view.listview.SimpleAsyImgAdapter
 * JD-Core Version:    0.6.2
 */