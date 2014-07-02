package com.marshall.cafeproject.fragments;

import com.marshall.cafeproject.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddCafeDetailsFragment extends Fragment {

	private View myFragmentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myFragmentView = inflater.inflate(R.layout.add_cafe_details_layout, container, false);
		
		return myFragmentView;
	}

}
