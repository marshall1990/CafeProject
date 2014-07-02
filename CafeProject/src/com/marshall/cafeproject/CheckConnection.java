package com.marshall.cafeproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Klasa sprawdza czy u¿ytkownik ma po³¹czenie z sieci¹ Internet
 */

public class CheckConnection {

	// metoda sprawdza po³¹czenie z mobiln¹ sieci¹ Internet
	public boolean internet(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
						return true;
		}
		return false;
	}

	// metoda sprawdza po³¹czenie z sieci¹ WI-FI
	public boolean wifi(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			if (wifiManager.isWifiEnabled()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
