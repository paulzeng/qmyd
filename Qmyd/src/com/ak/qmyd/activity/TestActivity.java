package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.DragAdapter;
 
import com.ak.qmyd.view.DragGridView;
/* * 演示Activity  Activity根据功能需要继承Base(BaseActivity,BaseFragmentActivity...)
 * @author HM  
 * @date 2015-4-8 
 */
public class TestActivity extends FragmentActivity implements OnItemClickListener{
    private static final String[] CONTENT = new String[] { "Recent", "Artists", "Albums", "Songs", "Playlists", "Genres" };
    DragAdapter adapter;
	TextView textView;
	boolean isv=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_test);
		 
		//adapter=new DragAdapter(this, channelList);
		DragGridView dg=(DragGridView) findViewById(R.id.dragGrid1);
		dg.setOnItemClickListener(this);
		
		dg.setAdapter(adapter);
		
		
		
		
		DragGridView dg2=(DragGridView) findViewById(R.id.dragGrid2);
		//DragAdapter da2=new DragAdapter(this, channelList);
		//dg2.setAdapter(da2);
		dg2.setIsExchange(false);
		
		final ScaleAnimation animation=new ScaleAnimation(1, 1, 0, 1);
		animation.setDuration(600);//设置动画持续时间 
		animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态 
		
//		final ScaleAnimation animation2=new ScaleAnimation(1, 1, 1, 0);
//		animation2.setDuration(2000);//设置动画持续时间 
//		animation2.setFillAfter(true);//动画执行完后是否停留在执行完的状态 
		
		final View Layou=findViewById(R.id.yout);
		Layou.setAnimation(animation);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				animation.startNow();
				//Layou.setAnimation(animation);
			}
		});
		
//		float fromX 动画起始时 X坐标上的伸缩尺寸 
//		float toX 动画结束时 X坐标上的伸缩尺寸 
//		float fromY 动画起始时Y坐标上的伸缩尺寸 
//		float toY 动画结束时Y坐标上的伸缩尺寸 
//		int pivotXType 动画在X轴相对于物件位置类型 
//		float pivotXValue 动画相对于物件的X坐标的开始位置 
//		int pivotYType 动画在Y轴相对于物件位置类型 
//		float pivotYValue 动画相对于物件的Y坐标的开始位置 

		

		

//		RequestQueue queue = Volley.newRequestQueue(this);
//		//除了StringRequest  JsonObjectRequest  JsonArrayRequest ImageRequest 等 也可以继承Request自定义 (Request是泛型)
//		StringRequest strRequest=new StringRequest(Request.Method.GET, "http://xxxx.xxx", new Listener<String>() {
//
//			@Override
//			public void onResponse(String response) {
//				// TODO Auto-generated method stub
//				//通讯成功的 返回数据
//			}
//		}, new ErrorListener() {
//
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				// TODO Auto-generated method stub
//				showToast("网络连接失败");
//				
//				//通讯失败的 抛出异常
//			}
//		});		
//		queue.add(strRequest);//加入到通讯队列中
//
//		String post="http://192.168.10.9:8080/qmyd/api/rest/admin/login";
//		  
//		StringRequest pr = new StringRequest(Request.Method.POST,post,
//			    new Response.Listener<String>() {
//			        @Override
//			        public void onResponse(String response) {
//			        	 showToast("33333"+response);
//			        }
//			    }, new Response.ErrorListener() {
//			        @Override
//			        public void onErrorResponse(VolleyError error) {
//			            showToast("33333error");
//			        }
//			    }) {
//			    @Override
//			    protected Map<String, String> getParams() {
//			        //在这里设置需要post的参数
//			            Map<String, String> map = new HashMap<String, String>();  
//			            map.put("loginId", "王伟");  
//			            map.put("passWord", "mima2222"); 
//			            map.put("hardId", "asd"); 
//			          return map;
//			    }
//			};  
//		queue.add(pr);//加入到通讯队列中
	
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
}
