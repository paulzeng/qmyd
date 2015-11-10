package com.ak.qmyd.activity.adapter;

import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.bean.Dynamic;
import com.ak.qmyd.tools.BitmapUtils;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * @author HM
 * @date 2015-4-15 ÏÂÎç4:55:23
 */
public class DynamicFousRecyAdapter extends Adapter<DynamicFousRecyAdapter.ViewHolder> {
	private static final int TYPE_FOOTER_VIEW = 0x7684;
	private List<Dynamic> list;
	private int layoutResId;
	private Context context;
	private View footerView;
	public DynamicFousRecyAdapter(Context context,int rowLayout) {
        this.context=context;
        this.layoutResId = rowLayout;
        BitmapUtils.initImageLoader(context);
       // notifyItemRangeInserted(0, list.size()-1);
    }
	public void add(List<Dynamic> list){
		int positionStart=0;
		if(this.list==null){
			this.list = list;
		}else{
			positionStart=this.list.size()-1;
			this.list.addAll(list);
		}
		notifyItemRangeInserted(positionStart, this.list.size()-1);
	}
	
	@Override
	public int getItemCount() {
		if(list == null)
			return 0;
		if(null!=footerView)
			return list.size()+1;
		return list.size();
	}
	@Override
	public int getItemViewType(int position) {
		 if (null != footerView && getItemCount() - 1 == position) { // footer
			return TYPE_FOOTER_VIEW;
		 }
		return super.getItemViewType(position);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int i) {
		// TODO Auto-generated method stub
			Dynamic dy = list.get(i);
	        viewHolder.title.setText(dy.getTitle());
	        //viewHolder.image.setim;
	        viewHolder.nickName.setText(dy.getNickName());
	        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(dy.getImage(), viewHolder.image);
	        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(dy.getPhoto(), viewHolder.photo);
	       // viewHolder.photo;
//	        RequestQueue mRequestQueue = Volley.newRequestQueue(context);  
//	        final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(  
//	                20);  
//	        ImageCache imageCache = new ImageCache() {  
//	            @Override  
//	            public void putBitmap(String key, Bitmap value) {  
//	                mImageCache.put(key, value);
//	            }  
//	  
//				@Override  
//	            public Bitmap getBitmap(String key) {  
//	                return mImageCache.get(key);  
//	            }  
//	        };  
//	        ImageLoader l=new ImageLoader(mRequestQueue, imageCache);
//	        ImageListener listener = ImageLoader  
//	                .getImageListener(viewHolder.image, android.R.drawable.ic_menu_rotate,  
//	                        android.R.drawable.ic_delete);  
//	        l.get(dy.getImage(), listener);
//	        ImageListener listener2 = ImageLoader  
//	                .getImageListener(viewHolder.photo, android.R.drawable.ic_menu_rotate,  
//	                        android.R.drawable.ic_delete);  
//	        l.get(dy.getPhoto(), listener2);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		  View v = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
	      return new ViewHolder(v);
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public TextView nickName;
        public ImageView photo;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            nickName=(TextView) itemView.findViewById(R.id.nickname);
            photo=(ImageView) itemView.findViewById(R.id.photo);
        }

    
}
}