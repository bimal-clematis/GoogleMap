package com.utkarsh.nearme;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;




public class RangeDialog extends Activity {
	// GPS Location
	
	Button submit,cancel;
	EditText range_input;
	String types,search_place;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.range_dialog);
		Bundle b = getIntent().getExtras();
		types = b.getString("types");
		search_place = b.getString("place");
		submit = (Button)findViewById(R.id.submit_btn);
		cancel = (Button)findViewById(R.id.cancel_btn);
		range_input = (EditText)findViewById(R.id.range_input);
		range_input.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String input = range_input.getText().toString();
				
				if(input.length()>0){
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				// Sending user current geo location
				i.putExtra("types", types);
				i.putExtra("place", search_place);
				
				// passing near places to map activity
				i.putExtra("radius_str", input);
				// staring activity
				startActivity(i);
				finish();
				}
				else{
					
				}
			}
		});
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				finish();
				
			}
		});
		
		range_input.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				try {
				     int val = Integer.parseInt(range_input.getText().toString());
				     if(val > 50) {
				    	 range_input.setText("");
				        
				     } else if(val < 1) {
				    	 range_input.setText("");
				     }
				   } catch (NumberFormatException ex) {
				      // Do something
				   }
				
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

}	