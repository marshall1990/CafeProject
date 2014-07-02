package com.marshall.cafeproject.activities;

import java.util.ArrayList;
import com.marshall.cafeproject.Login;
import com.marshall.cafeproject.NavDrawerItem;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.adapters.NavDrawerListAdapter;
import com.marshall.cafeproject.fragments.*;
import com.marshall.cafeproject.interfaces.LoginListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

public class HomeActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private LoginListener loginListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTitle = mDrawerTitle = getTitle();

		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));

		navMenuIcons.recycle();

		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View view) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			displayView(0);
		}

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		
		if (isOnlineUser())
			handleLogin(true);
		
		loginListener = new LoginListener() {
			
			@Override
			public void onChangeState(boolean isOnline) {
				handleLogin(isOnline);
				if (isOnline) 
					performItemClick(0);
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
			}
		};
		
		Login.setLoginListener(loginListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent("com.marshall.cafeproject.activities.SETTINGSACTIVITY");
			startActivityForResult(intent, 100);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == 100) {
			HomeFragment homeFragment = (HomeFragment)getFragmentManager().findFragmentByTag("Home");
			CafesFragment cafesFragment = (CafesFragment)getFragmentManager().findFragmentByTag("Cafes");
			EventsFragment eventsFragment = (EventsFragment)getFragmentManager().findFragmentByTag("Events");
			
			if (homeFragment != null && homeFragment.isVisible()) {
				performItemClick(0);
			} else if (cafesFragment != null && cafesFragment.isVisible()) {
				performItemClick(1);
			} else if (eventsFragment != null && eventsFragment.isVisible()) {
				performItemClick(2);
			}
		}
	}
	
	private void performItemClick(int position) {
		mDrawerList.performItemClick(mDrawerList.getAdapter().getView(position, null, null), position, mDrawerList.getAdapter().getItemId(position));
	}

	private void displayView(int position) {
		Fragment fragment = null;
		String tag = "";
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			tag = "Home";
			break;
		case 1:
			removeOldFragment();
			fragment = new CafesFragment();
			tag = "Cafes";
			break;
		case 2:
			fragment = new EventsFragment();
			tag = "Events";
			break;
		case 3:
			fragment = new AddCafeFragment();
			tag = "AddCafe";
			break;
		case 4:
			fragment = new SearchFragment();
			tag = "Search";
			break;
		case 5:
			if (isOnlineUser())
				displayLogoutDialog();
			else {
				fragment = new LoginFragment();
				tag = "Login";
			}
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment, tag).commitAllowingStateLoss();

			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			displayView(position);
		}
	}
	
	private void removeOldFragment() {
		CafesListFragment cafesListFragment = (CafesListFragment) getFragmentManager().findFragmentById(R.id.cafesList);
		if (cafesListFragment != null)
			getFragmentManager().beginTransaction().remove(cafesListFragment).commit();
	}
	
	private void handleLogin(boolean isOnline) {
		if (isOnline) {
			String logout = getResources().getString(R.string.logout).toString();
			navMenuTitles [navMenuTitles.length - 1] = logout;
			navDrawerItems.get(navDrawerItems.size() - 1).setTitle(logout);
		} else {
			String login = getResources().getString(R.string.login).toString();
			navMenuTitles [navMenuTitles.length - 1] = login;
			navDrawerItems.get(navDrawerItems.size() - 1).setTitle(login);
		}
		
		adapter.notifyDataSetChanged();
	}
	
	private boolean isOnlineUser() {
		SharedPreferences preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
		
		if (!preferences.getString("login", "").equals(""))
			return true;
		else
			return false;		
	}
	
	private void displayLogoutDialog() {
		new AlertDialog.Builder(this).setTitle(getString(R.string.logout_info)).setPositiveButton(getString(R.string.yes), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences sharedPreferences = getSharedPreferences("Settings", 0);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("login", "");
				editor.commit();
				handleLogin(false);
				performItemClick(navDrawerItems.size() - 1);
				dialog.dismiss();
			}
		}).setNegativeButton(getString(R.string.no), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		}).setCancelable(false).create().show();
	}
}
