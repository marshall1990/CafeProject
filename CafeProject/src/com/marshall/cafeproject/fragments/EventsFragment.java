package com.marshall.cafeproject.fragments;

import java.util.ArrayList;
import com.marshall.cafeproject.Event;
import com.marshall.cafeproject.R;
import com.marshall.cafeproject.adapters.EventsArrayAdapter;
import com.marshall.cafeproject.database.DatabaseHandler;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class EventsFragment extends Fragment {

	private View eventsFragmentView;
	private ListView listView;
	private EventsArrayAdapter adapter;

	public EventsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		eventsFragmentView = inflater.inflate(R.layout.events_layout,
				container, false);

		return eventsFragmentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getControls();

		new LoadEvents().execute();
	}

	private void getControls() {
		listView = (ListView) eventsFragmentView.findViewById(R.id.list_view);
	}

	private class LoadEvents extends AsyncTask<Void, Void, Void> {

		private ArrayList<Event> events;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			DatabaseHandler db = new DatabaseHandler(getActivity());

			events = db.getEvents();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			getActivity().runOnUiThread(new Runnable() {
				public void run() {

					adapter = new EventsArrayAdapter(getActivity(), events);
					listView.setAdapter(adapter);
				}
			});
		}
	}

}
