package com.why.project.fragmenttabhosttopdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TabHost;
import android.widget.Toast;

import com.why.project.fragmenttabhosttopdemo.fragment.WebViewFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private FragmentTabHost mTopFTabHostLayout;

	//选项卡子类集合
	private ArrayList<TabItem> tabItemList = new ArrayList<TabItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initTabList();
		initFTabHostLayout();
		setFTabHostData();
		initEvents();

	}

	/**
	 * 初始化选项卡数据集合
	 */
	private void initTabList() {
		//底部选项卡对应的Fragment类使用的是同一个Fragment，那么需要考虑切换Fragment时避免重复加载UI的问题】
		tabItemList.add(new TabItem(this,"已发布",WebViewFragment.class));
		tabItemList.add(new TabItem(this,"未发布",WebViewFragment.class));
	}

	/**
	 * 初始化FragmentTabHost
	 */
	private void initFTabHostLayout() {
		//实例化
		mTopFTabHostLayout = (FragmentTabHost) findViewById(R.id.tab_top_ftabhost_layout);
		mTopFTabHostLayout.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);//最后一个参数是碎片切换区域的ID值
		// 去掉分割线
		mTopFTabHostLayout.getTabWidget().setDividerDrawable(null);

	}

	/**
	 * 设置选项卡的内容
	 */
	private void setFTabHostData() {
		//Tab存在于TabWidget内，而TabWidget是存在于TabHost内。与此同时，在TabHost内无需在写一个TabWidget，系统已经内置了一个TabWidget
		for (int i = 0; i < tabItemList.size(); i++) {
			//实例化一个TabSpec,设置tab的名称和视图
			TabHost.TabSpec spec = mTopFTabHostLayout.newTabSpec(tabItemList.get(i).getTabTitle()).setIndicator(tabItemList.get(i).getTabView());
			// 添加Fragment
			//初始化传参：http://bbs.csdn.net/topics/391059505
			Bundle bundle = new Bundle();
			if(i == 0 ){
				bundle.putString("param", "http://www.baidu.com");
			}else{
				bundle.putString("param", "http://www.cnblogs.com");
			}

			mTopFTabHostLayout.addTab(spec, tabItemList.get(i).getTabFragment(), bundle);
		}

		//默认选中第一项
		mTopFTabHostLayout.setCurrentTab(0);
		upDateTab(mTopFTabHostLayout);
	}


	/**
	 * 更新文字颜色和背景
	 * http://blog.csdn.net/ray_admin/article/details/69951540
	 */
	private void upDateTab(FragmentTabHost mTabHost) {
		int size = mTabHost.getTabWidget().getChildCount();
		for (int i = 0; i < size; i++) {
          	//CheckedTextView tv = (CheckedTextView) mTabHost.getTabWidget().getChildAt(i).findViewById(toptab_checkedTextView);//常规写法
			CheckedTextView tv = (CheckedTextView) tabItemList.get(i).getToptab_checkedTextView();
			if (mTabHost.getCurrentTab() == i) {//选中
				//修改文字的颜色
				tv.setTextColor(this.getResources().getColor(R.color.tab_text_selected_top));
				//修改view的背景颜色
				if (0 == i) {
					mTopFTabHostLayout.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_left_selected);
				} else if (i == size - 1) {
					mTopFTabHostLayout.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_right_selected);
				} else {
					mTopFTabHostLayout.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_mid_selected);
				}
			} else {//不选中
				//修改文字的颜色
				tv.setTextColor(this.getResources().getColor(R.color.tab_text_normal_top));
				//修改view的背景颜色
				if (0 == i) {
					mTopFTabHostLayout.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_left_unselected);
				} else if (i == size - 1) {
					mTopFTabHostLayout.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_right_unselected);
				} else {
					mTopFTabHostLayout.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_mid_unselected);
				}
			}
		}
	}

	private void initEvents() {
		//选项卡的切换事件监听
		mTopFTabHostLayout.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {

				upDateTab(mTopFTabHostLayout);

				Toast.makeText(MainActivity.this, tabId, Toast.LENGTH_SHORT).show();

			}
		});

	}

	/**
	 * 选项卡子项类*/
	class TabItem{

		private Context mContext;

		private CheckedTextView toptab_checkedTextView;

		//底部选项卡对应的文字
		private String tabTitle;
		//底部选项卡对应的Fragment类
		private Class<? extends Fragment> tabFragment;

		public TabItem(Context mContext, String tabTitle, Class tabFragment){
			this.mContext = mContext;

			this.tabTitle = tabTitle;
			this.tabFragment = tabFragment;
		}

		public Class<? extends Fragment> getTabFragment() {
			return tabFragment;
		}

		public String getTabTitle() {
			return tabTitle;
		}

		/**
		 * 获取底部选项卡的布局实例并初始化设置*/
		private View getTabView() {
			//取得布局实例
			View toptabitemView = View.inflate(mContext, R.layout.tab_top_item, null);

			//===========设置CheckedTextView控件的图片和文字==========
			toptab_checkedTextView = (CheckedTextView) toptabitemView.findViewById(R.id.toptab_checkedTextView);

			//设置CheckedTextView的文字
			toptab_checkedTextView.setText(tabTitle);

			return toptabitemView;
		}

		public CheckedTextView getToptab_checkedTextView() {
			return toptab_checkedTextView;
		}
	}
}
