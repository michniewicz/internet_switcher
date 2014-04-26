package com.rubchick.internetswitcher.broadcastreceivers;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rubchick.internetswitcher.Main;
import com.rubchick.internetswitcher.R;
import com.rubchick.internetswitcher.onclicklisteners.WIfiNetworkItemClickListener;

public class WifiReceiver extends BroadcastReceiver {

	private Activity mainActivity;

	private WifiManager wifiManager;
	private List<ScanResult> wifiList;

	public WifiReceiver(Activity mainActivity, WifiManager wifiManager) {
		this.mainActivity = mainActivity;
		this.wifiManager = wifiManager;
	}

	public void onReceive(Context c, Intent intent) {
		wifiList = wifiManager.getScanResults();

		LinearLayout linLay = (LinearLayout) mainActivity
				.findViewById(R.id.scanResultsWrapper);
		linLay.setGravity(Gravity.CENTER_HORIZONTAL);

		// clear scan results
		try {
			linLay.removeViews(0, linLay.getChildCount());
		} catch (Exception e) {
			e.printStackTrace();
		}

		String currentSsid = "";

		final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		if (connectionInfo != null && connectionInfo.getSSID() != null) {
			currentSsid = connectionInfo.getSSID();
		}

		for (int i = 0; i < wifiList.size(); i++) {
			StringBuilder sb = new StringBuilder();
			String ssid = (wifiList.get(i)).SSID;
			String capabilities = (wifiList.get(i)).capabilities;
			sb.append(ssid);

			if (ssid.equals(currentSsid)) {
				// TODO add to strings.xml
				sb.append(" - connected");
			}

			sb.append("\n");
			sb.append(capabilities);
			sb.append("\n\n");

			TextView tv = new TextView(mainActivity.getApplicationContext());
			tv.setTextColor(Color.BLACK);
			tv.setText(sb.toString());
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setOnClickListener(new WIfiNetworkItemClickListener(
					mainActivity, wifiManager, ssid, capabilities));

			View view = new View(mainActivity.getApplicationContext());
			view.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 3));
			view.setBackgroundColor(Color.GRAY);
			linLay.addView(tv);
			linLay.addView(view);
		}

		TextView text = (TextView) mainActivity
				.findViewById(R.id.scanResultsProgress);
		text.setVisibility(View.GONE);

		ProgressBar progressBar = (ProgressBar) mainActivity
				.findViewById(R.id.scanResultsProgressBar);
		progressBar.setVisibility(View.GONE);
	}
}
