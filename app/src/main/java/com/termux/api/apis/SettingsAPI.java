package com.termux.api.apis;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.JsonWriter;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import com.termux.api.TermuxApiReceiver;
import com.termux.api.util.ResultReturner;
import com.termux.shared.logger.Logger;

import java.util.List;
import java.io.PrintWriter;
import java.io.StringWriter;

import android.content.ContentResolver;
import android.provider.Settings;

public class SettingsAPI {

private static final String LOG_TAG = "SettingsAPI";
static Context context;
static TermuxApiReceiver receiver;
static Intent intent;
static StringWriter sw;
static JsonWriter out;

public static void onReceive(TermuxApiReceiver _receiver, final Context _context, Intent _intent) {
	Logger.logDebug(LOG_TAG, "onReceive");
	context = _context;
	receiver = _receiver;
	intent = _intent;
	sw = new StringWriter();
	out = new JsonWriter(sw);
	// out.setIndent("  ");
	out.setIndent("	");

	ContentResolver cr = context.getContentResolver();
	String setting = intent.getStringExtra("setting");
	String type = intent.getStringExtra("type");

	try {
		out.beginObject();
		try {
			jsonTest();
			// settings();
		} catch (Exception e) {
			Logger.logError(LOG_TAG, e.toString());
			out.name(e.toString());
		}
		out.endObject();
		write();
		Logger.logInfo(LOG_TAG, sw.toString());
	} catch (Exception e) {
		Logger.logError(LOG_TAG, e.toString());
	}
}

static void write() {
	ResultReturner.returnData(context.getApplicationContext(), intent, new ResultReturner.WithInput() {
		@Override public void writeResult(PrintWriter _out) throws Exception {
		try {
			_out.print(sw);
		} catch (Exception e) {
			Logger.logError(LOG_TAG, e.toString());
		}
		}
	});
}

static void jsonTest() throws Exception {
	out.beginArray();
	// out.beginObject();
	out.name("123");
	// out.name("on").value(0);
	// out.value(name);
	// out.endObject();
	out.endArray();
}

static void get() {
	switch(type) {
		case "string":
			String get = Settings.Global.getString(cr, setting);
			out.name(get);
			break;
		case "int":
			int get = Settings.Global.getInt(cr, setting);
			out.name(get.toString());
			break;
		default:
			out.name(type +" invalid type");
			break;
	}
}

static void set() {
	String value = intent.getStringExtra("value");
	String result = "";
	switch(type) {
		case "string":
			result = Settings.Global.putString(cr, setting, value);
			break;
		case "int":
			result = Settings.Global.putInt(cr, setting, Integer.parseInt(value));
			break;
		default:
			out.name(type +" invalid type");
			break;
	}
	out.name("result "+result);
}

static void settings() throws Exception {
	String action = intent.getAction();
	out.beginArray();
	// out.beginObject();
	switch(action) {
		case "get":
			get();
			break
		case "set":
			set();
			break;
		default:
			out.name(action+" invalid action");
			break;
	}
	// out.endObject();
	out.endArray();
}
} // class
