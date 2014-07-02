package com.marshall.cafeproject.fragments;

import java.util.HashMap;
import java.util.List;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marshall.cafeproject.Cafe;
import com.marshall.cafeproject.CheckConnection;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.UserLocation;
import com.marshall.cafeproject.database.DatabaseHandler;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {
	
	private MapView mapView;
	private GoogleMap map;
	private View myFragmentView;
	private CheckConnection checkConnection = new CheckConnection();
	private int mapType = GoogleMap.MAP_TYPE_SATELLITE;
	private SharedPreferences sharedPreferences;
	
	public HomeFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myFragmentView = inflater.inflate(R.layout.fragment_home, container, false);

		mapView = (MapView) myFragmentView.findViewById(R.id.the_map);
		mapView.onCreate(savedInstanceState);
		mapView.onResume();
		
		getMap();
		
		return myFragmentView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	public void getMap() {
		sharedPreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
		mapType = sharedPreferences.getInt("mapType", GoogleMap.MAP_TYPE_NORMAL);
		
		try {
			MapsInitializer.initialize(this.getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}
		
		map = mapView.getMap();
		
		map.setMapType(mapType);
		
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(false);
		
		new LoadPoints().execute();
	}
	
	
	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		mapView.onLowMemory();
		super.onLowMemory();
	}
	
	private class LoadPoints extends AsyncTask<Void, Void, Void> {
		private Context context = getActivity().getApplicationContext(); 
		private LatLng lastLatLng;
		private List<Cafe> cafes;
		private double lat, lng;
		private String city;
		private DatabaseHandler db;
		private List<String> lastLocation;
		private int icon;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			if (getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			} else {
				getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			db = new DatabaseHandler(context);
			
			if (checkConnection.internet(context) || checkConnection.wifi(context)) {
				LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
				UserLocation userLocation = new UserLocation(context, locationManager);

				lat = userLocation.getLat();
				lng = userLocation.getLng();
				
				lastLatLng = new LatLng(lat, lng);
				
				city = userLocation.getCity();
				
				db.addLastLocation(lat, lng, city);
			} else {
				lastLocation = db.getLastLocation();
				lat = Double.parseDouble(lastLocation.get(0).toString());
				lng = Double.parseDouble(lastLocation.get(1).toString());
				lastLatLng = new LatLng(lat, lng);
				city = lastLocation.get(2).toString();
			}
			
			HashMap<String, String> columns = new HashMap<String, String>();
			columns.put("City", city);
			
			cafes = db.getAllCafes(columns);

			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			map.addMarker(new MarkerOptions().position(lastLatLng).title("Jesteœ tutaj!").snippet("Twoja ostatnia lokalizacja."));
			
			for (Cafe cafe : cafes) {
				lat = Double.parseDouble(cafe.getLat());
				lng = Double.parseDouble(cafe.getLng());
				
				icon = getResources().getIdentifier(cafe.getIcon(), "drawable", context.getPackageName());
				
				map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(cafe.getName()).icon(BitmapDescriptorFactory.fromResource(icon)).snippet(cafe.getSnippet()));
			}
			
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 10), 3000, null);
			
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
		
	}
}
