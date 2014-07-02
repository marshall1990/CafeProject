package com.marshall.cafeproject;

import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

final public class UserLocation {

	private double lat;
	private double lng;
	private String city;

	public UserLocation(Context context, LocationManager locationManager) {
		Location networkLocation = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Location gpsLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location lastLoc = null;

		if (networkLocation != null && gpsLocation != null) {
			if (networkLocation.getTime() > gpsLocation.getTime())
				lastLoc = networkLocation;
			else
				lastLoc = gpsLocation;
		} else if (gpsLocation != null)
			lastLoc = gpsLocation;
		else if (networkLocation != null)
			lastLoc = networkLocation;

		if (lastLoc != null) {
			double lat = lastLoc.getLatitude();
			double lng = lastLoc.getLongitude();

			setLat(lat);
			setLng(lng);

			Geocoder geocoder = new Geocoder(context, Locale.getDefault());
			try {
				List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
				if (addresses.size() > 0)
					setCity(addresses.get(0).getLocality());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
