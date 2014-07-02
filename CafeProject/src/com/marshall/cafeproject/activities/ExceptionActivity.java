package com.marshall.cafeproject.activities;

import com.marshall.cafeproject.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExceptionActivity extends Activity {
	
	private Button btnExit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exception);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		btnExit = (Button) findViewById(R.id.bExit);
		btnExit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
				
			}
		});
	}

}
