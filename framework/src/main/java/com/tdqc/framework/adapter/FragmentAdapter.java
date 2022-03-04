package com.tdqc.framework.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tdqc.framework.R;

import java.util.List;

public class FragmentAdapter {

	private List<Fragment> fragments;
	FragmentManager fragmentManager;
	/**Activity中所要被替换的区域的id*/
	private int fragmentContentId;
	/**当前Tab页面索引*/
	private int currentTab;

	/**Activity加载Fragment用这个构造方法*/
	public FragmentAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId, int index) {
		fragmentManager = fragmentActivity.getSupportFragmentManager();
		this.fragments = fragments;
		this.fragmentContentId = fragmentContentId;
		fragmentManager.beginTransaction()
				.add(fragmentContentId, fragments.get(index)).commitAllowingStateLoss();
	}

	/**Fragment嵌套子Fragment用这个构造方法，不然可能出现某些情况下Fragment不显示的问题*/
	public FragmentAdapter(Fragment fragment, List<Fragment> fragments, int fragmentContentId, int index ) {
		fragmentManager = fragment.getChildFragmentManager();
		this.fragments = fragments;
		this.fragmentContentId = fragmentContentId;
		fragmentManager.beginTransaction()
				.add(fragmentContentId, fragments.get(index)).commitAllowingStateLoss();
	}

	/**获取当前显示fragment的位置*/
	public int getCurrentIndex(){
		return currentTab;
	}

	public void onChange(int index) {
		onChange(index,true);
	}

	/**
	 * @param withAnim 是否使用跳转动画
	 * */
	public void onChange(int index,boolean withAnim){
		Fragment fragment = fragments.get(index);
		FragmentTransaction ft = obtainFragmentTransaction(index);
		getCurrentFragment().onPause(); // 暂停当前tab
		getCurrentFragment().onStop(); // 暂停当前tab
		if (fragment.isAdded()) {
			fragment.onStart(); // 启动目标tab的onStart()
			fragment.onResume(); // 启动目标tab的onResume()
		} else {
			ft.add(fragmentContentId, fragment);
		}
		showTab(index,withAnim); // 显示目标tab
		ft.commitAllowingStateLoss();
		currentTab = index;
	}

	/**切换tab*/
	private void showTab(int idx,boolean withAnim) {
		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			FragmentTransaction ft = withAnim ? obtainFragmentTransaction(idx):fragmentManager.beginTransaction();
			if (idx == i) {
				ft.show(fragment);
			} else {
				ft.hide(fragment);
			}
			ft.commitAllowingStateLoss();
		}
		currentTab = idx; // 更新目标tab为当前tab
	}

	/**获取一个带动画的FragmentTransaction*/
	private FragmentTransaction obtainFragmentTransaction(int index) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// 设置切换动画
		if (index > currentTab) {
			ft.setCustomAnimations(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
		} else {
			ft.setCustomAnimations(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
		}
		return ft;
	}

	public Fragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

}
