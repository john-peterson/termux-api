package com.termux.api.apis;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionInfo;
import android.content.pm.PackageManager;
import android.support.test.InstrumentationRegistry;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import com.termux.api.TermuxApiReceiver;
import com.termux.api.util.ResultReturner;
import com.termux.shared.logger.Logger;

import java.io.JsonWriter;
import java.util.ArrayList

import org.simalliance.openmobileapi.Channel;


public class SimAPI {

	private static final String LOG_TAG = "SimAPI";

	public static void onReceive(final Context context, final Intent intent) {
		Logger.logDebug(LOG_TAG, "onReceive");

		ResultReturner.returnData(apiReceiver, intent, new ResultJsonWriter() {
			@Override
			public void writeJson(JsonWriter out) throws Exception {


				out beginObject();
				SubscriptionManager sm = context.getSystemService(SubscriptionManager.class);
				for(SubscriptionInfo si: sm.getActiveSubscriptionInfoList()) {
					out.name("slot").value(si.getSimSlotIndex());	out.name("id").value(si.getSubscriptionId());	out.name("name").value(si.getCarrierName());
				}
				out.endObject();
				
				
				out.beginObject();
				final PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
		out name("omapi").value (pm.hasSystemFeature(PackageManager.FEATURE_SE_OMAPI_UICC));
		out.endObject();


out.beginObject();
		Reader[] readers = _service.getReaders();
		for (Reader reader : readers) {
			out name ("name").value(reader.getName());
			out.name("status").value(reader.isSecureElementPresent());
			}
		if (readers.length == 0) {
			out.name("info").value("No reader available");
		}
		out.endObject();


			}
		}
	}
}
