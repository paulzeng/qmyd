package com.ak.qmyd.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.DynamicFousRecyAdapter;
import com.ak.qmyd.activity.base.BaseFragment;
import com.ak.qmyd.bean.Dynamic;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Ȧ�Ӷ�̬-��ע
 * 
 * @author HM
 * @date 2015-4-18 ����1:36:27
 */
public class CircleFriendFragment extends BaseFragment {
	private RecyclerView mRecyclerView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private DynamicFousRecyAdapter adapter;
	private ProgressBar mProgressBar;
	private List<Dynamic> list;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		list=new ArrayList<Dynamic>();
		View view=inflater.inflate(R.layout.fragment_dynamic_friend, null);
		initFindViewById(view);
		return view;
	}
	private void initFindViewById(View view){
		mRecyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
		mProgressBar=(ProgressBar) view.findViewById(R.id.progressBar);
		mSwipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.orange_color);
		GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
		//layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
		mRecyclerView.setHasFixedSize(true);//���ݱ仯���ı��������
		mRecyclerView.setLayoutManager(layoutManager);
		//mRecyclerView.setItemAnimator(new CustomItemAnimator());
		adapter=new DynamicFousRecyAdapter(context,R.layout.view_dynamic_fous_item);
		mRecyclerView.setAdapter(adapter);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            	toObtainData();
            }
        });
		mProgressBar.setVisibility(View.VISIBLE);
		 
	}
	@Override
	public void onStart() {
		toObtainData();
		super.onStart();
	}
	private void toObtainData(){
		
		RequestQueue queue = Volley.newRequestQueue(getActivity());
		//����StringRequest  JsonObjectRequest  JsonArrayRequest ImageRequest �� Ҳ���Լ̳�Request�Զ��� (Request�Ƿ���)
		StringRequest strRequest=new StringRequest(Request.Method.GET, "http://aikong..vicp.net:7979/login?name=123&password", new Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				//ͨѶ�ɹ��� ��������
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				mSwipeRefreshLayout.setRefreshing(false);
				showToast("���粻����!");
				//ͨѶʧ�ܵ� �׳��쳣
				Dynamic dy=new Dynamic(123, "http://t12.baidu.com/it/u=4095575894,102452705&fm=32&s=A98AA55F526172A6F6A058E50300A060&w=623&h=799&img.JPEG", "����Խ��Խ��1", "http://t12.baidu.com.asd.jpg", "����һ���ܳ��ı���");
				Dynamic dy1=new Dynamic(123, "http://t12.baidu.com/it/u=4095575894,102452705&fm=32&s=A98AA55F526172A6F6A058E50300A060&w=623&h=799&img.JPEG", "����Խ��Խ��2", "http://b.hiphotos.baidu.com/image/pic/item/e4dde71190ef76c666af095f9e16fdfaaf516741.jpg", "����һ���ܳ��ı���");
				Dynamic dy2=new Dynamic(123, "http://b.hiphotos.baidu.com/image/pic/item/e4dde71190ef76c666af095f9e16fdfaaf516741.jpg", "����Խ��Խ��3", "http://t12.baidu.com/it/u=4095575894,102452705&fm=32&s=A98AA55F526172A6F6A058E50300A060&w=623&h=799&img.JPEG", "����һ���ܳ��ı���");
				//Dynamic dy3=new Dynamic(123, "http://t12.baidu.com/it/u=4095575894,102452705&fm=32&s=A98AA55F526172A6F6A058E50300A060&w=623&h=799&img.JPEG", "����Խ��Խ��4", "http://t12.baidu.com/it/u=4095575894,102452705&fm=32&s=A98AA55F526172A6F6A058E50300A060&w=623&h=799&img.JPEG", "����һ���ܳ��ı���");
				//Dynamic dy4=new Dynamic(123, "http://t12.baidu.com/it/u=4095575894,102452705&fm=32&s=A98AA55F526172A6F6A058E50300A060&w=623&h=799&img.JPEG", "����Խ��Խ��5", "http://t12.baidu.com/it/u=4095575894,102452705&fm=32&s=A98AA55F526172A6F6A058E50300A060&w=623&h=799&img.JPEG", "����һ���ܳ��ı���");
				list.add(dy);
				list.add(dy1);
				list.add(dy2);
				//list.add(dy3);list.add(dy4);
				adapter.add(list);
				mProgressBar.setVisibility(View.GONE);
				mSwipeRefreshLayout.setRefreshing(false);

			}
		});
		queue.add(strRequest);//���뵽ͨѶ������
	}
	
}
