package com.rubchick.internetswitcher.onclicklisteners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.rubchick.internetswitcher.Main;
import com.rubchick.internetswitcher.networkdata.WifiData;
import com.rubchick.internetswitcher.utils.HexUtils;

public class WIfiNetworkItemClickListener implements OnClickListener {
	private Activity mainActivity;
	private WifiManager wifiManager;

	private String ssid;
	private String capabilities;

	public WIfiNetworkItemClickListener(Activity mainActivity,
			WifiManager wifiManager, String ssid, String capabilities) {
		this.ssid = ssid;
		this.capabilities = capabilities;
		this.wifiManager = wifiManager;
		this.mainActivity = mainActivity;
	}

	@Override
	public void onClick(View v) {
		Log.i("CLICK", "Click OnWIfiItemNetworrClickListener");

		final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

		AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);

		// TODO add to strings.xml
		alert.setTitle("Password: ");

		// Set an EditText view to get user input
		final EditText password = new EditText(mainActivity);
		password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		alert.setView(password);

		// TODO add to strings.xml
		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				if (connectionInfo != null && connectionInfo.getSSID() != null) {
					WifiManager wifi = (WifiManager) mainActivity
							.getSystemService(Context.WIFI_SERVICE);
					wifi.disconnect();
				}

				String pass = password.getText().toString();

				// setup a wifi configuration
				WifiData.setupWifiContiguration(wifiManager, capabilities,
						ssid, pass);
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		alert.show();
	}
}
