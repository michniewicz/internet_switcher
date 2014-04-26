package com.rubchick.internetswitcher.networkdata;

import com.rubchick.internetswitcher.R;
import com.rubchick.internetswitcher.utils.HexUtils;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WifiData {

	public static boolean isWifiEnabled(Activity mainActivity) {
		WifiManager wifi = (WifiManager) mainActivity
				.getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled()) {
			return true;
		}

		return false;
	}

	public static void setWifiEnabled(Activity mainActivity,
			WifiManager wifiManager, boolean status) {

		wifiManager.setWifiEnabled(status);

		if (status) {
			wifiManager.startScan();
			TextView tv = (TextView) mainActivity
					.findViewById(R.id.scanResultsProgress);
			tv.setVisibility(View.VISIBLE);

			ProgressBar progressBar = (ProgressBar) mainActivity
					.findViewById(R.id.scanResultsProgressBar);
			progressBar.setVisibility(View.VISIBLE);

		} else {
			LinearLayout linLay = (LinearLayout) mainActivity
					.findViewById(R.id.scanResultsWrapper);

			// clear scan results
			try {
				linLay.removeViews(0, linLay.getChildCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void setupWifiContiguration(WifiManager wifiManager,
			String capabilities, String ssid, String pass) {
		WifiConfiguration wc = new WifiConfiguration();
		wc.SSID = "\"".concat(ssid).concat("\"");
		Log.i("CLICK", "ssid: " + ssid);
		int netId = -1;
		if (capabilities.contains("WPA")) {
			// WPA security
			Log.i("CLICK", "wifiNetworkPass: " + pass);
			wc.preSharedKey = "\"".concat(pass).concat("\"");
			wc.status = WifiConfiguration.Status.ENABLED;
			wc.priority = 40;
			wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			wc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			wc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

			// connect to and enable the connection
			netId = wifiManager.addNetwork(wc);
			Log.i("CLICK", "netId: " + netId);
			boolean enabled = wifiManager.enableNetwork(netId, true);
			boolean setEnabled = wifiManager.setWifiEnabled(true);
			Log.i("CLICK", "enabled: " + enabled);
			Log.i("CLICK", "setEnabled: " + setEnabled);
		} else if (capabilities.contains("WEP")) {
			// WEP Security
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			wc.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			wc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			wc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

			if (HexUtils.getHexKey(pass)) {
				wc.wepKeys[0] = pass;
			} else {
				wc.wepKeys[0] = "\"".concat(pass).concat("\"");
			}

			wc.wepTxKeyIndex = 0;
		} else {
			// No security
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			wc.allowedAuthAlgorithms.clear();
			wc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			wc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		}

		// TODO if netId != -1 - saveConfiguration()
		if(netId != -1){
			wifiManager.saveConfiguration();
		}
	}
}
