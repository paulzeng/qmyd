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
/* * ��ʾActivity  Activity���ݹ�����Ҫ�̳�Base(BaseActivity,BaseFragmentActivity...)
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
		animation.setDuration(600);//���ö�������ʱ�� 
		animation.setFillAfter(true);//����ִ������Ƿ�ͣ����ִ�����״̬ 
		
//		final ScaleAnimation animation2=new ScaleAnimation(1, 1, 1, 0);
//		animation2.setDuration(2000);//���ö�������ʱ�� 
//		animation2.setFillAfter(true);//����ִ������Ƿ�ͣ����ִ�����״̬ 
		
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
		
//		float fromX ������ʼʱ X�����ϵ������ߴ� 
//		float toX ��������ʱ X�����ϵ������ߴ� 
//		float fromY ������ʼʱY�����ϵ������ߴ� 
//		float toY ��������ʱY�����ϵ������ߴ� 
//		int pivotXType ������X����������λ������ 
//		float pivotXValue ��������������X����Ŀ�ʼλ�� 
//		int pivotYType ������Y����������λ������ 
//		float pivotYValue ��������������Y����Ŀ�ʼλ�� 

		

		

//		RequestQueue queue = Volley.newRequestQueue(this);
//		//����StringRequest  JsonObjectRequest  JsonArrayRequest ImageRequest �� Ҳ���Լ̳�Request�Զ��� (Request�Ƿ���)
//		StringRequest strRequest=new StringRequest(Request.Method.GET, "http://xxxx.xxx", new Listener<String>() {
//
//			@Override
//			public void onResponse(String response) {
//				// TODO Auto-generated method stub
//				//ͨѶ�ɹ��� ��������
//			}
//		}, new ErrorListener() {
//
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				// TODO Auto-generated method stub
//				showToast("��������ʧ��");
//				
//				//ͨѶʧ�ܵ� �׳��쳣
//			}
//		});		
//		queue.add(strRequest);//���뵽ͨѶ������
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
//			        //������������Ҫpost�Ĳ���
//			            Map<String, String> map = new HashMap<String, String>();  
//			            map.put("loginId", "��ΰ");  
//			            map.put("passWord", "mima2222"); 
//			            map.put("hardId", "asd"); 
//			          return map;
//			    }
//			};  
//		queue.add(pr);//���뵽ͨѶ������
	
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
}
