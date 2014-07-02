package com.marshall.cafeproject.fragments;

import com.marshall.cafeproject.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchFragment extends Fragment {

	private View searchFragmentView;
	private EditText etName, etStreet, etDistrict, etProduct, etCity;
	private String name, street, district, product, city;
	
	public SearchFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		searchFragmentView = inflater.inflate(R.layout.search_cafe_layout, container, false);
		
		return searchFragmentView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getControls();
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.action_settings);
		item.setVisible(false);
		boolean isCreated = false;

		for (int i = 0; i < menu.size(); i++) {
			if (menu.getItem(i).getTitle() == getResources().getString(
					R.string.send))
				isCreated = true;
		}

		if (!isCreated)
			getActivity().getMenuInflater().inflate(R.menu.save_menu, menu);

		DrawerLayout drawerMenu = (DrawerLayout) getActivity().findViewById(
				R.id.drawer_layout);
		ListView menuListView = (ListView) getActivity().findViewById(
				R.id.list_slidermenu);

		if (drawerMenu.isDrawerOpen(menuListView))
			menu.findItem(R.id.save).setVisible(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
			search();
			return true;
		default:
			return false;
		}
	}
	
	private void search() {
		getData();
		
		if (name.equals("") && street.equals("") && district.equals("") && product.equals("") && city.equals("")) {
			Toast.makeText(getActivity(), getResources().getString(R.string.enter_data), Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent("com.marshall.cafeproject.activities.FOUNDCAFESACTIVITY");
			Bundle bundle = new Bundle();
			bundle.putString("Name", name);
			bundle.putString("Street", street);
			bundle.putString("District", district);
			bundle.putString("City", city);
			bundle.putString("Product", product);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	
	private void getControls() {
		etName = (EditText) searchFragmentView.findViewById(R.id.etName);
		etStreet = (EditText) searchFragmentView.findViewById(R.id.etStreet);
		etDistrict = (EditText) searchFragmentView.findViewById(R.id.etDistrict);
		etProduct = (EditText) searchFragmentView.findViewById(R.id.etProduct);
		etCity = (EditText) searchFragmentView.findViewById(R.id.etCity);
	}
	
	private void getData() {
		name = etName.getText().toString();
		street = etStreet.getText().toString();
		district = etDistrict.getText().toString();
		product = etProduct.getText().toString();
		city = etCity.getText().toString();
	}
}
