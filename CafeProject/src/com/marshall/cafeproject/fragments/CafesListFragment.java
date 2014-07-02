package com.marshall.cafeproject.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.marshall.cafeproject.Cafe;
import com.marshall.cafeproject.CheckConnection;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.UserLocation;
import com.marshall.cafeproject.activities.CafesDetailsActivity;
import com.marshall.cafeproject.database.DatabaseHandler;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CafesListFragment extends ListFragment {
	
	private boolean isLandscape, isEmptyCafeList;
	private int cafeId = 0;
	private ArrayList<HashMap<String, String>> cafesList;
	private CheckConnection checkConnection = new CheckConnection();
	private String name, street, district, city, findCity, product;
	
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_CITY = "city";
	private static final String TAG_STREET = "street";
	private static final String TAG_BUILDING = "building";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (getActivity() != null) {
			new LoadAllCafes().execute();
		}
		
		if(savedInstanceState != null) {
			cafeId = savedInstanceState.getInt("CafeId", 0);
		}
		
		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			name = bundle.getString("Name");
			street = bundle.getString("Street");
			district = bundle.getString("District");
			findCity = bundle.getString("City");
			product = bundle.getString("Product");
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("CafeId", cafeId);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		cafeId = Integer.parseInt(((TextView) v.findViewById(R.id.id)).getText().toString());
		showDetails(position, cafeId);
	}
	
	void showDetails(int index, int cafeId) {
		if (isLandscape) {
			getListView().setItemChecked(cafeId, true);
			
			CafesDetailsFragment details = (CafesDetailsFragment) getFragmentManager().findFragmentById(R.id.cafesDetails);
			if (details == null || details.getShownCafeId() != cafeId) {
				details = CafesDetailsFragment.newInstance(cafeId);
				
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.cafesDetails, details);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
		} else {
			Intent intent = new Intent();
			intent.setClass(getActivity(), CafesDetailsActivity.class);
			intent.putExtra("CafeId", cafeId);
			startActivity(intent);
		}
	}
	
	private class LoadAllCafes extends AsyncTask<String, String, String> {
		
		private List<Cafe> cafes;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			cafesList = new ArrayList<HashMap<String, String>>();
			
			if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			} else {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}
		
		@Override
		protected String doInBackground(String... params) {
			DatabaseHandler db = new DatabaseHandler(getActivity());
			
			if (checkConnection.internet(getActivity()) || checkConnection.wifi(getActivity())) {
				LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
				UserLocation userLocation = new UserLocation(getActivity(), locationManager);
				city = userLocation.getCity();
			} else {
				
				List<String> lastLocation = db.getLastLocation();
				city = lastLocation.get(2).toString();
			}
			
			HashMap<String, String> columns = new HashMap<String, String>();
			columns.put("Name", name);
			columns.put("Street", street);
			columns.put("District", district);
			columns.put("City", city);
			columns.put("FindCity", findCity);
			columns.put("Product", product);
			
			cafes = db.getAllCafes(columns);
			
			for (Cafe cafe: cafes) {
				String id = Integer.toString(cafe.getId());
				String name = cafe.getName();
				String street = cafe.getStreet();
				String building = cafe.getBuilding();
				String city = cafe.getCity();
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put(TAG_ID, id);
				map.put(TAG_NAME, name);
				map.put(TAG_CITY, city + ", ");
				map.put(TAG_STREET, street);
				map.put(TAG_BUILDING, building);
				
				cafesList.add(map);
				
				isEmptyCafeList = false;
			}
			
			if (cafes.size() == 0) isEmptyCafeList = true;
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					String[] from = {TAG_ID, TAG_NAME, TAG_CITY, TAG_STREET, TAG_BUILDING};
					int[] to = {R.id.id, R.id.name, R.id.city, R.id.street, R.id.building};
					
					ListAdapter adapter = new SimpleAdapter(getActivity(), cafesList, R.layout.cafes_list_item, from, to);
					setListAdapter(adapter);
					
					if (isEmptyCafeList) {
						getActivity().findViewById(R.id.tvInfo).setVisibility(View.VISIBLE);	
					}
					
					View detailsFrame = getActivity().findViewById(R.id.cafesDetails);
					isLandscape = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
					
					if (isLandscape) {
						getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
						cafeId = cafes.get(0).getId();
						showDetails(0, cafeId);
					}
				}
			});
			
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
	}
}
