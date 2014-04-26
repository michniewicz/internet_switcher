package com.rubchick.internetswitcher;

import com.rubchick.internetswitcher.broadcastreceivers.WifiReceiver;
import com.rubchick.internetswitcher.networkdata.MobileData;
import com.rubchick.internetswitcher.networkdata.WifiData;
import com.rubchick.internetswitcher.utils.MathUtils;

import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(14)
public class Main extends Activity {
	private Main mainActivity;

	private Button enableMobile;
	private Button enableWifi;

	private TextView tMoblieSpl;
	private TextView tWifiSpl;

	private TextView mtUsage;
	private TextView wfUsage;

	private WifiManager wifiManager;
	private WifiReceiver wifiReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mainActivity = this;
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		wifiReceiver = new WifiReceiver(mainActivity, wifiManager);
		registerReceiver(wifiReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		Typeface font = Typeface.createFromAsset(getAssets(), "amplitud.ttf");

		tMoblieSpl = (TextView) findViewById(R.id.mobileNetTxt);
		tWifiSpl = (TextView) findViewById(R.id.wifiTxt);

		tMoblieSpl.setTypeface(font);
		tWifiSpl.setTypeface(font);

		mtUsage = (TextView) findViewById(R.id.mobileTrafficTxt);
		wfUsage = (TextView) findViewById(R.id.wifiTrafficTxt);

		setupButtons();
		setupTraficValues();

		enableMobile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					boolean enabled = true;

					if (WifiData.isWifiEnabled(mainActivity)) {
						Toast.makeText(getApplicationContext(),
								getString(R.string.toast_turn_off_wifi),
								Toast.LENGTH_LONG).show();

						return;
					}

					if (MobileData.isMobileDataEnabled(mainActivity)) {
						enabled = false;

						enableMobile
								.setBackgroundResource(R.drawable.off_button);
					} else {
						enableMobile
								.setBackgroundResource(R.drawable.on_button);

					}

					MobileData.setMobileDataEnabled(getApplicationContext(),
							enabled);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		enableWifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {

					if (MobileData.isMobileDataEnabled(mainActivity)) {
						Toast.makeText(getApplicationContext(),
								getString(R.string.toast_turn_off_mobile),
								Toast.LENGTH_LONG).show();

						return;
					}

					boolean enabled = true;

					if (WifiData.isWifiEnabled(mainActivity)) {

						enabled = false;

						enableWifi.setBackgroundResource(R.drawable.off_button);
					} else {

						enableWifi.setBackgroundResource(R.drawable.on_button);

					}

					WifiData.setWifiEnabled(mainActivity, wifiManager, enabled);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		setupButtons();
		setupTraficValues();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		setupButtons();
		setupTraficValues();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_help:
			Intent i = new Intent(Main.this, About.class);
			startActivity(i);
			break;
		}

		return true;
	}

	private void setupButtons() {
		enableMobile = (Button) findViewById(R.id.enableMobileBtn);
		enableWifi = (Button) findViewById(R.id.enableWifiBtn);

		if (MobileData.isMobileDataEnabled(mainActivity)) {
			enableMobile.setBackgroundResource(R.drawable.on_button);
		} else {
			enableMobile.setBackgroundResource(R.drawable.off_button);
		}

		if (WifiData.isWifiEnabled(mainActivity)) {
			enableWifi.setBackgroundResource(R.drawable.on_button);
		} else {
			enableWifi.setBackgroundResource(R.drawable.off_button);
		}
	}

	// setup views for traffic stats
	private void setupTraficValues() {

		TextView mobileReceived = (TextView) findViewById(R.id.mobileTrafficReceivedValue);
		TextView mobileSent = (TextView) findViewById(R.id.mobileTrafficSentValue);

		TextView wifiReceived = (TextView) findViewById(R.id.wifiTrafficReceivedValue);
		TextView wifileSent = (TextView) findViewById(R.id.wifiTrafficSentValue);

		// For devices that don't suppot TrafficStats methods
		if (TrafficStats.getMobileRxBytes() == TrafficStats.UNSUPPORTED
				|| TrafficStats.getMobileTxBytes() == TrafficStats.UNSUPPORTED
				|| TrafficStats.getTotalRxBytes() == TrafficStats.UNSUPPORTED
				|| TrafficStats.getTotalTxBytes() == TrafficStats.UNSUPPORTED) {

			mobileReceived.setVisibility(View.GONE);
			mobileSent.setVisibility(View.GONE);
			wifiReceived.setVisibility(View.GONE);
			wifileSent.setVisibility(View.GONE);

			findViewById(R.id.mobileTrafficReceivedTxt)
					.setVisibility(View.GONE);
			findViewById(R.id.mobileTrafficSentTxt).setVisibility(View.GONE);
			findViewById(R.id.wifiTrafficReceivedTxt).setVisibility(View.GONE);
			findViewById(R.id.wifiTrafficSentTxt).setVisibility(View.GONE);

			mtUsage.setVisibility(View.GONE);
			wfUsage.setVisibility(View.GONE);
		}

		long mobileRes = TrafficStats.getMobileRxBytes();
		long mobileSnt = TrafficStats.getMobileTxBytes();

		String resBytes = " " + getString(R.string.str_bytes);
		String sentBytes = " " + getString(R.string.str_bytes);

		double dMobileRes = mobileRes;
		double dMobileSnt = mobileSnt;

		if (dMobileRes > 1024) {
			dMobileRes /= 1024;
			resBytes = " " + getString(R.string.str_kb);
		}

		if (dMobileSnt > 1024) {
			dMobileSnt /= 1024;
			sentBytes = " " + getString(R.string.str_kb);
		}

		if (dMobileRes > 1024) {
			dMobileRes /= 1024;
			resBytes = " " + getString(R.string.str_mb);
		}

		if (dMobileSnt > 1024) {
			dMobileSnt /= 1024;
			sentBytes = " " + getString(R.string.str_mb);
		}

		mobileReceived.setText(MathUtils.roundTwoDecimals(dMobileRes)
				+ resBytes);
		mobileSent.setText(MathUtils.roundTwoDecimals(dMobileSnt) + sentBytes);

		// ///////////////////////////////////////////////////////////////////////////////

		long wifiRes = TrafficStats.getTotalRxBytes()
				- TrafficStats.getMobileRxBytes();
		long wifiSnt = TrafficStats.getTotalTxBytes()
				- TrafficStats.getMobileTxBytes();

		String wifiResBytes = " " + getString(R.string.str_bytes);
		String wifiSentBytes = " " + getString(R.string.str_bytes);

		double dWifiRes = wifiRes;
		double dWifiSnt = wifiSnt;

		if (dWifiRes > 1024) {
			dWifiRes /= 1024;
			wifiResBytes = " " + getString(R.string.str_kb);
		}

		if (dWifiSnt > 1024) {
			dWifiSnt /= 1024;
			wifiSentBytes = " " + getString(R.string.str_kb);
		}

		if (dWifiRes > 1024) {
			dWifiRes /= 1024;
			wifiResBytes = " " + getString(R.string.str_mb);
		}

		if (dWifiSnt > 1024) {
			dWifiSnt /= 1024;
			wifiSentBytes = " " + getString(R.string.str_mb);
		}

		wifiReceived.setText(MathUtils.roundTwoDecimals(dWifiRes)
				+ wifiResBytes);
		wifileSent
				.setText(MathUtils.roundTwoDecimals(dWifiSnt) + wifiSentBytes);
	}
}
