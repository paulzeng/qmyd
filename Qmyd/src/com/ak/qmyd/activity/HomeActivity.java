package com.ak.qmyd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ak.qmyd.R;
import com.ak.qmyd.activity.adapter.DragAdapter;
import com.ak.qmyd.activity.adapter.FragmentTabContentAdapter;
import com.ak.qmyd.activity.adapter.PageImageAdapter;
import com.ak.qmyd.activity.base.BaseFragmentActivity;
import com.ak.qmyd.bean.model.Result;
import com.ak.qmyd.bean.model.SportType;
import com.ak.qmyd.bean.result.SportTypeResult;
import com.ak.qmyd.config.Config;
import com.ak.qmyd.config.MyApplication;
import com.ak.qmyd.tools.DebugUtility;
import com.ak.qmyd.tools.NetManager;
import com.ak.qmyd.tools.UIManager;
import com.ak.qmyd.view.CirclePageIndicator;
import com.ak.qmyd.view.DragGridView;
import com.ak.qmyd.view.TabPageIndicator;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

/**
 * @author HM
 * @date 2015-4-20 ����7:35:20
 */
public class HomeActivity extends BaseFragmentActivity implements
		OnItemClickListener {
	private ViewPager bannerViewPager;
	// �����˶�����
	private List<SportType> listTypes;
	private DrawerLayout mDrawerLayout;
	private View layoutColumneditor;
	private ViewPager viewPagerIndicator;
	private boolean isMoving;// �����Ƿ���ִ����
	private DragGridView myDragGridView;// ��ʾ�Ѿ���ע��GridView
	private DragGridView addDragGridView;// ��ʾû�й�ע��GridView
	private DragAdapter myAdapter;// �Ѿ���ע��Adapter
	private DragAdapter addAdapter;// û�й�ע��Adapter
	private FragmentTabContentAdapter fragmentTabAdapter;
	private TabPageIndicator tabIndicator;
	private List<SportType> addls;// δ��ӵ���Ŀ
	private boolean isLoop = true;
	private Map<String, ?> userInfoSp;
	private String currentTypeId;
	private SharedPreferences sp;
	private SharedPreferences.Editor edit;
	private Dialog loadDialog;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (bannerViewPager.getCurrentItem() == (addItems().size() - 1)) {
				bannerViewPager.setCurrentItem(0);
			} else {
				bannerViewPager
						.setCurrentItem(bannerViewPager.getCurrentItem() + 1);
			}
		}
	};
	private String sessionId;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		loadDialog = UIManager.getLoadingDialog(this);
		// Banner ViewPager
		bannerViewPager = (ViewPager) findViewById(R.id.banner_viewpager);
		// ���ñ���
		setTitle(R.string.app_name);
		findViewById(R.id.imageView_open_leftmenu).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDrawerLayout.openDrawer(Gravity.START);
					}
				});
		layoutColumneditor = findViewById(R.id.layout_column_editor);
		findViewById(R.id.imageButton).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (layoutColumneditor.getVisibility() == View.VISIBLE) {
							viewPagerIndicator.setVisibility(View.VISIBLE);
							layoutColumneditor.setVisibility(View.GONE);
							columnEditNotifyDataSetChanged(myAdapter
									.getChannnelLst());
							String typeId = "";
							for (int j = 0; j < myAdapter.getChannnelLst()
									.size(); j++) {
								if (myAdapter.getChannnelLst().get(j)
										.getTypeId() != null
										&& j != (myAdapter.getChannnelLst()
												.size() - 1)) {
									typeId += myAdapter.getChannnelLst().get(j)
											.getTypeId()
											+ "@";
								}
								if (j == (myAdapter.getChannnelLst().size() - 1)) {
									typeId += myAdapter.getChannnelLst().get(j)
											.getTypeId();
								}
							}
							if (!MyApplication.instance.userInfoSp.getAll()
									.isEmpty()) {
								sendMyType(typeId);
							}
						} else {
							layoutColumneditor.setVisibility(View.VISIBLE);
							viewPagerIndicator.setVisibility(View.INVISIBLE);
						}
					}
				});
		// �໬
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		setEnableDrawerLayout(mDrawerLayout);
		// end
		userInfoSp = MyApplication.instance.userInfoSp.getAll();
		get();
		initCircleIndicator();
		// �Զ��л�ҳ�湦��
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (isLoop) {
					SystemClock.sleep(3000);
					handler.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	// �����ҵ��˶�����
	protected void sendMyType(final String typeId) {
		loadDialog.show();
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = Config.SEND_MY_TYPE_URL;
		StringRequest strRequest = new StringRequest(Request.Method.POST, url,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {

						Gson gson = new Gson();
						Result r = gson.fromJson(response, Result.class);
						UIManager.toggleDialog(loadDialog);
						if (r.getResultCode() == 1) {
						} else {
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("��ȡ����ʧ��");
						UIManager.toggleDialog(loadDialog);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", (String) userInfoSp.get("userId"));
				map.put("typeId", typeId);
				map.put("hardId", MyApplication.instance.getHardId());
				String sessionId = (String) userInfoSp.get("sessionId") == null ? "1"
						: (String) userInfoSp.get("sessionId");
				map.put("sessionId", sessionId);
				return map;
			}
		};
		queue.add(strRequest);

	}

	// ��ʼ��ԭ���Indicator
	private void initCircleIndicator() {
		List<ImageView> images = addItems();
		PageImageAdapter mPageImageAdapter = new PageImageAdapter(images);
		bannerViewPager.setAdapter(mPageImageAdapter);
		CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.banner_indicator);
		mIndicator.setViewPager(bannerViewPager);
		// end Banner ViewPager
	}

	/***
	 * ��ʼ�� TabIndicator
	 */
	private void initTabIndicator() {
		DebugUtility.showLog("��ʼ��initTabIndicator");
		// **TabIndicator ViewPager**/
		viewPagerIndicator = (ViewPager) findViewById(R.id.viewpage_indicator);
		tabIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		fragmentTabAdapter = new FragmentTabContentAdapter(
				getSupportFragmentManager(), listTypes);
		viewPagerIndicator.setAdapter(fragmentTabAdapter);
		// ����Ԥ����Ϊ1
		viewPagerIndicator.setOffscreenPageLimit(1);

		tabIndicator.setViewPager(viewPagerIndicator);
		// ** end TabIndicator ViewPager**/
		sp = this.getSharedPreferences("config", 0);
		edit = sp.edit();
		ViewPager.OnPageChangeListener mListener = new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				edit.putString("typeId", listTypes.get(position).getTypeId());
				edit.commit();
			}

			@Override
			public void onPageScrollStateChanged(int position) {
			}
		};
		tabIndicator.setOnPageChangeListener(mListener);
	}

	private void columnEditNotifyDataSetChanged(List<SportType> listTypes) {
		this.listTypes.clear();
		this.listTypes.addAll(listTypes);
		fragmentTabAdapter.setListTypes(this.listTypes);

		// fragmentTabAdapter.notifyDataSetChanged();
		tabIndicator.notifyDataSetChanged();
	}

	// Banner ����add
	private List<ImageView> addItems() {
		List<ImageView> images = new ArrayList<ImageView>();
		ImageView iv = new ImageView(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		iv.setScaleType(ScaleType.CENTER_CROP);
		iv.setLayoutParams(lp);
		iv.setImageResource(R.drawable.banner);
		images.add(iv);
		ImageView iv2 = new ImageView(this);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		iv2.setScaleType(ScaleType.CENTER_CROP);
		iv2.setLayoutParams(lp2);
		iv2.setImageResource(R.drawable.banner);
		images.add(iv2);
		ImageView iv3 = new ImageView(this);
		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		iv3.setScaleType(ScaleType.CENTER_CROP);
		iv3.setLayoutParams(lp3);
		iv3.setImageResource(R.drawable.banner);
		images.add(iv3);
		return images;
	}

	//
	private void get() {
		loadDialog.show();
		if (NetManager.isNetworkConnected(this)) {
			RequestQueue queue = Volley.newRequestQueue(this);
			sessionId = ((String) userInfoSp.get("sessionId") == null ? "1"
					: (String) userInfoSp.get("sessionId"));
			userId = ((String) userInfoSp.get("userId") == null ? "0"
					: (String) userInfoSp.get("userId"));
			String url = API_URL + "admin/home/type/List" + "/"
					+ MyApplication.instance.getHardId() + "/" + sessionId
					+ "/" + userId;
			DebugUtility.showLog(url);
			StringRequest strRequest = new StringRequest(url,
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// TODO Auto-generated method stub
							DebugUtility.showLog("GetTypeList���ؽ��=" + response);
							Gson gson = new Gson();
							SportTypeResult st = gson.fromJson(response,
									SportTypeResult.class);
							UIManager.toggleDialog(loadDialog);
							if (st.getResultCode() == 1) {
								listTypes = st.getTypeArray();
								initTabIndicator();
								init();
								findViewById(R.id.imageButton).setVisibility(
										View.VISIBLE);
								getAddSport();
								// stopClick();
							} else if (st.getResultCode() == 10000) {
								findViewById(R.id.imageButton).setVisibility(
										View.VISIBLE);
								changeLogin();
								showToast("�����µ�¼,�û�����ʧЧ");
								listTypes = st.getTypeArray();
								initTabIndicator();
								init();
								getAddSport();
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("��ȡ����ʧ��");
							UIManager.toggleDialog(loadDialog);
						}
					});
			queue.add(strRequest);
		} else {
			showToast("���粻����,������������");
			UIManager.toggleDialog(loadDialog);
		}
	}

	private void getAddSport() {
		// rest/admin/home/type/List{hardId}/{sessionId}/{userId}
		RequestQueue queue = Volley.newRequestQueue(this);
		// //����StringRequest JsonObjectRequest JsonArrayRequest ImageRequest ��
		// Ҳ���Լ̳�Request�Զ��� (Request�Ƿ���)
		String url = API_URL + "admin/home/type/notInterestList" + "/"
				+ MyApplication.instance.getHardId() + "/" + sessionId + "/"
				+ userId;
		StringRequest strRequest = new StringRequest(url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Gson gson = new Gson();
						SportTypeResult st = gson.fromJson(response,
								SportTypeResult.class);
						DebugUtility.showLog("��ȡδ���ӵ��˶������" + response);
						if (st.getResultCode() == 1) {
							addls = st.getTypeArray();
							initAddGridView();
						} else if (st.getResultCode() == 3) {
							addls = st.getTypeArray();
							initAddGridView();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						showToast("��ȡ����ʧ��");
					}
				});
		queue.add(strRequest);
	}

	private void init() {
		myDragGridView = (DragGridView) findViewById(R.id.dragGridview_my);
		ArrayList<SportType> myls = new ArrayList<SportType>();
		myls.addAll(listTypes);
		myAdapter = new DragAdapter(this, myls);
		myDragGridView.setAdapter(myAdapter);
		myDragGridView.setOnItemClickListener(this);

	}

	private void initAddGridView() {
		addDragGridView = (DragGridView) findViewById(R.id.dragGridview_add);
		//
		addAdapter = new DragAdapter(this, addls);
		addDragGridView.setAdapter(addAdapter);
		addDragGridView.setIsExchange(false);
		addDragGridView.setOnItemClickListener(this);
		myDragGridView.setTwoGridView(addDragGridView);
	}

	private void stopClick() {
		LinearLayout ll_home_column = (LinearLayout) findViewById(R.id.ll_home_column);
		ll_home_column.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		// TODO Auto-generated method stub
		if (isMoving) {
			return;
		}
		if (parent == myDragGridView) {

			final ImageView moveImageView = getView(view);
			if (moveImageView != null) {
				TextView newTextView = (TextView) view
						.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final SportType channel = ((DragAdapter) parent.getAdapter())
						.getItem(position);// ��ȡ�����Ƶ������
				DebugUtility.showLog("�˶�����"+myAdapter.getCount());
				if(myAdapter.getCount()>1){
					// û�й�ע��
					addAdapter.setVisible(false);
					// ��ӵ����һ��
					addAdapter.addItem(channel);

					new Handler().postDelayed(new Runnable() {
						public void run() {
							try {
								int[] endLocation = new int[2];
								// ��ȡ�յ������
								addDragGridView.getChildAt(
										addDragGridView.getLastVisiblePosition())
										.getLocationInWindow(endLocation);
								myAdapter.setRemove(position);
								MoveAnim(moveImageView, startLocation, endLocation,
										channel, myDragGridView);

							} catch (Exception localException) {
							}
						}
					}, 50L);
				}else{
					showToast("����ѡ��һ���˶�");
				}
			}
		} else if (parent == addDragGridView) {
			final ImageView moveImageView = getView(view);
			if (moveImageView != null) {
				TextView newTextView = (TextView) view
						.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final SportType channel = ((DragAdapter) parent.getAdapter())
						.getItem(position);
				myAdapter.setVisible(false);
				// ��ӵ����һ��
				myAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// ��ȡ�յ������
							myDragGridView.getChildAt(
									myDragGridView.getLastVisiblePosition())
									.getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation, endLocation,
									channel, addDragGridView);
							addAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
		}
	}

	/**
	 * ��ȡ�����Item�Ķ�ӦView��
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}

	/**
	 * ���ITEM�ƶ�����
	 * 
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,
			int[] endLocation, final SportType moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		// ��ȡ���ݹ�����VIEW������
		moveView.getLocationInWindow(initLocation);
		// �õ�Ҫ�ƶ���VIEW,�������Ӧ��������
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView,
				initLocation);
		// �����ƶ�����
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);// ����ʱ��
		// ��������
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// ����Ч��ִ����Ϻ�View���󲻱�������ֹ��λ��
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				isMoving = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof �����ж�2��ʵ���ǲ���һ�����жϵ������DragGrid����OtherGridView
				if (clickGridView == myDragGridView) {
					addAdapter.setVisible(true);
					addAdapter.notifyDataSetChanged();
					myAdapter.remove();
				} else {
					myAdapter.setVisible(true);
					myAdapter.notifyDataSetChanged();
					addAdapter.remove();
				}
				isMoving = false;
			}
		});
	}

	/**
	 * ��ȡ�ƶ���VIEW�������ӦViewGroup��������
	 * 
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	/**
	 * �����ƶ���ITEM��Ӧ��ViewGroup��������
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isLoop = false;
		ActivityManager manager = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);
		String name = manager.getRunningTasks(1).get(0).topActivity
				.getClassName();
	}

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// Intent home = new Intent(Intent.ACTION_MAIN);
	// home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// home.addCategory(Intent.CATEGORY_HOME);
	// startActivity(home);
	// }
	// return false;
	// }

	public String getCurrentTypeId() {
		currentTypeId = myAdapter.getChannnelLst().get(0).getTypeId();
		return currentTypeId;
	}

	private long exitTime;

	@Override
	public void onBackPressed() {
		// 2�η����˳�
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�Ӧ��",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			((MyApplication) getApplication()).exit();
		}
	}

}
