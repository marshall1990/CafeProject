package com.marshall.cafeproject.adapters;

import com.marshall.cafeproject.R;
import com.marshall.cafeproject.fragments.*;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	private String titles[];

	public TabsPagerAdapter(Context context, FragmentManager fragmentManager) {
		super(fragmentManager);
		titles = context.getResources().getStringArray(
				R.array.tab_add_cafe_items);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			AddCafeInfoFragment fragmentTab1 = new AddCafeInfoFragment();
			return fragmentTab1;
		case 1:
			AddCafeDetailsFragment fragmentTab2 = new AddCafeDetailsFragment();
			return fragmentTab2;
		case 2:
			AddCafeHoursFragment fragmentTab3 = new AddCafeHoursFragment();
			return fragmentTab3;
		case 3:
			AddCafeTablesFragment fragmentTab4 = new AddCafeTablesFragment();
			return fragmentTab4;
		case 4:
			AddCafeMenuFragment fragmentTab5 = new AddCafeMenuFragment();
			return fragmentTab5;
		default:
			return null;
		}
	}

	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return titles.length;
	}

}