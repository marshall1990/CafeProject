package com.marshall.cafeproject.fragments;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import com.marshall.cafeproject.Cafe;
import com.marshall.cafeproject.CheckConnection;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.SendDataToServer;
import com.marshall.cafeproject.database.DatabaseHandler;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CafesDetailsFragment extends Fragment {

	private TextView txtName, txtStreet, txtBuilding, txtLocal, txtDistrict, txtCity, txtPhone, txtWebsite, txtHours, txtMonday, txtTuesday, txtWednesday, txtThursday, txtFriday, txtSaturday, txtSunday, txtDescription;
	private TableLayout tlHours;
	private Button btnLocation, btnRating, btnMenu, btnTables;
	private ImageView imageView;
	private String android_id;
	private int cafeId, points;
	private View cafesDetailsFragmentView;
	private CheckConnection checkConnection;
	
	public CafesDetailsFragment() {
	}

	public static CafesDetailsFragment newInstance(int index) {
		CafesDetailsFragment fragment = new CafesDetailsFragment();

		Bundle bundle = new Bundle();
		bundle.putInt("CafeId", index);
		fragment.setArguments(bundle);

		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		cafesDetailsFragmentView = inflater.inflate(R.layout.details_cafe, container, false);
		
		return cafesDetailsFragmentView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getControls();
		
		checkConnection = new CheckConnection();
		android_id = Secure.getString(getActivity().getBaseContext().getContentResolver(), Secure.ANDROID_ID);
		
		//Obiekt StrictMode zapobiega wyœwietlaniu siê wyj¹tku android.os.NetworkOnMainThreadException
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		if (getActivity() != null) {
			new GetCafeDetails().execute();
		}
		
		btnRating.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (checkConnection.internet(getActivity()) || checkConnection.wifi(getActivity())) {
					SendDataToServer sendData = new SendDataToServer();
					if (sendData.vote(cafeId, points, android_id)) {
						btnRating.setVisibility(View.GONE);
						Toast.makeText(getActivity(), getString(R.string.vote_info), Toast.LENGTH_LONG).show();
					} else
						Toast.makeText(getActivity(), getString(R.string.error_while_send_data), Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent("com.marshall.cafeproject.activities.EXCEPTIONACTIVITY");
					startActivity(intent);
				}
				
			}
		});
		
		btnMenu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("com.marshall.cafeproject.activities.MENUACTIVITY");
				Bundle bundle = new Bundle();
				bundle.putInt("CafeId", getShownCafeId());
				bundle.putString("Name", txtName.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		btnTables.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("com.marshall.cafeproject.activities.TABLESACTIVITY");
				Bundle bundle = new Bundle();
				bundle.putInt("CafeId", getShownCafeId());
				bundle.putString("Name", txtName.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	private void getControls() {
		imageView = (ImageView) cafesDetailsFragmentView.findViewById(R.id.ivImage);
		btnLocation = (Button) cafesDetailsFragmentView.findViewById(R.id.bLocation);
		btnRating = (Button) cafesDetailsFragmentView.findViewById(R.id.btnRating);
		btnMenu = (Button) cafesDetailsFragmentView.findViewById(R.id.bMenu);
		txtName = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvName);
		txtStreet = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvStreet);
		txtBuilding = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvBuilding);
		txtLocal = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvLocal);
		txtDistrict = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvDistrict);
		txtCity = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvCity);
		txtPhone = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvPhone);
		txtWebsite = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvWebsite);
		txtHours = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvHours);
		txtMonday = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvMonday);
		txtTuesday = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvTuesday);
		txtWednesday = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvWednesday);
		txtThursday = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvThursday); 
		txtFriday = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvFriday); 
		txtSaturday = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvSaturday); 
		txtSunday = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvSunday);
		tlHours = (TableLayout) cafesDetailsFragmentView.findViewById(R.id.tlHours);
		txtDescription = (TextView) cafesDetailsFragmentView.findViewById(R.id.tvDescription);
		btnTables = (Button) cafesDetailsFragmentView.findViewById(R.id.btnTables);
	}
	
	public int getShownCafeId() {
		return getArguments().getInt("CafeId", 0);
	}
	
	private class GetCafeDetails extends AsyncTask<String, String, String> {
		
		private Cafe cafe;
		
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
		protected String doInBackground(String... params) {
			DatabaseHandler db = new DatabaseHandler(getActivity());
			cafe = db.getCafe(getShownCafeId());
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					cafeId = cafe.getId();
					points = cafe.getPoints();
					
					txtName.setText(cafe.getName());
					txtStreet.setText(cafe.getStreet());
					txtBuilding.setText(cafe.getBuilding());
					
					if (cafe.getLocal().equals("null")) {
						txtLocal.setText("");
					} else {
						txtLocal.setText(cafe.getLocal());
					}
					
					txtDistrict.setText(cafe.getDistrict());
					txtCity.setText(cafe.getCity());
					txtPhone.setText(cafe.getPhone());
					txtWebsite.setText(cafe.getWebsite());
					
					getHours();
					
					txtDescription.setText(cafe.getDescription());
					
					getImage();
					
					btnLocation.setText(cafe.getCity() + ", " + cafe.getStreet() + " " + cafe.getBuilding());
					btnLocation.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							String uri = String.format(Locale.ENGLISH, "geo:0,0?q=" + cafe.getCity() + "+" + cafe.getStreet() + "+" + cafe.getBuilding() + "?z=15");
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
					});
					
					if (checkConnection.internet(getActivity()) || checkConnection.wifi(getActivity())) {
						new CanVote().execute();
					}
					
					
					if (cafe.isMenu())
						btnMenu.setVisibility(View.VISIBLE);
					else
						btnMenu.setVisibility(View.GONE);
					
					if (cafe.isTables()) 
						btnTables.setVisibility(View.VISIBLE);
					else
						btnTables.setVisibility(View.GONE);
						
				}
			});
			
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
		
		private void getHours() {
			if (cafe.getMonday().equals("-") && cafe.getTuesday().equals("-") && cafe.getWednesday().equals("-") && cafe.getThursday().equals("-") && cafe.getFriday().equals("-") && cafe.getSaturday().equals("-") && cafe.getSunday().equals("-")) {
				tlHours.setVisibility(View.GONE);
				txtHours.setVisibility(View.GONE);
			} else {
				txtMonday.setText(cafe.getMonday());
				txtTuesday.setText(cafe.getTuesday());
				txtWednesday.setText(cafe.getWednesday());
				txtThursday.setText(cafe.getThursday());
				txtFriday.setText(cafe.getFriday());
				txtSaturday.setText(cafe.getSaturday());
				txtSunday.setText(cafe.getSunday());
			}
		}
		
		private void getImage() {
			if (cafe.getImage().substring(0, 4).equals("http")){
				try {
					URL url = new URL(cafe.getImage());
					Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
					imageView.setImageBitmap(bmp);
				} catch (MalformedURLException e) {
					Log.e("Wczytywanie ImageView", "MalformedURLException: " + e.toString());
				} catch (IOException e) {
					Log.e("Wczytywanie ImageView", "IOException: " + e.toString());
				}	
			} else {
				int resID = getResources().getIdentifier(cafe.getImage(), "drawable", getActivity().getPackageName());
				imageView.setImageResource(resID);
			}
		}
	}
	
	private class CanVote extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			SendDataToServer sendData = new SendDataToServer();
			if (sendData.canVote(cafeId, android_id)) 
				return true;
			else 
				return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			if (result)
				btnRating.setVisibility(View.VISIBLE);
			else
				btnRating.setVisibility(View.GONE);
		}	
	}
}
