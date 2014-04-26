package com.rubchick.internetswitcher;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);

		TextView about = (TextView) findViewById(R.id.aboutTxt);
		about.setMovementMethod(LinkMovementMethod.getInstance());
		about.setText(Html.fromHtml(getResources().getString(
				R.string.about_info)));
	}
}
