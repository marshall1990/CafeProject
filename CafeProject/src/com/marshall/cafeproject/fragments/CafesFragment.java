package com.marshall.cafeproject.fragments;

import com.marshall.cafeproject.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CafesFragment extends Fragment {

	public CafesFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cafes, container, false);

		return view;
	}
}