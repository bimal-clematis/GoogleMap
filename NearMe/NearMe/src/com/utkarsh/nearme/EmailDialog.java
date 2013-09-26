package com.utkarsh.nearme;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;




public class EmailDialog extends Activity {
	// GPS Location
	
	Button submit,cancel;
	TextView range_input;
	String types,search_place;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.email_dialog);
		
		submit = (Button)findViewById(R.id.email_dialog_submit_btn);
		cancel = (Button)findViewById(R.id.email_dialog_cancel_btn);
		range_input = (TextView)findViewById(R.id.email_dialog_title);
		range_input.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[]{"kartick.mishra@clematistech.com"});		  
				email.putExtra(Intent.EXTRA_SUBJECT, "Near Me - Feedback & Suggestions");
				email.putExtra(Intent.EXTRA_TEXT, "Hi, I have following remark for the Near Me App:");
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email, "Choose an Email client :"));
				finish();
			}
		});
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				finish();
				
			}
		});
		

	}

}	