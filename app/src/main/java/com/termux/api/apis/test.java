
package com.termux.api.apis;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.JsonWriter;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.termux.api.TermuxApiReceiver;
import com.termux.api.util.ResultReturner;
import com.termux.shared.logger.Logger;
// import com.termux.api.Logger;

public class test {

private static final String LOG_TAG = "TestAPI";
static PrintWriter pw;
static StringWriter sw;
static Context context;
static TermuxApiReceiver receiver;
static Intent intent;

static void sleep(){
		try {Thread.sleep(5000);} catch (Exception e) {}
}

static void writeIn() {
	// ResultReturner.noteDone(receiver, intent);
	ResultReturner.returnData(context.getApplicationContext(), intent, new ResultReturner.WithInput() {
		@Override public void writeResult(PrintWriter out) throws Exception {
		// Logger.logInfo(LOG_TAG, "printing \""+sw+"\"");
		System.out.println("writeResult");
		// out.println("test");
		// System.out.println("test");
		out.print(sw);
		// out.flush();
		}
	});
}

static void writeJson() {
	ResultReturner.returnData(receiver, intent, new ResultReturner.ResultJsonWriter() {
		public void writeJson(JsonWriter out) throws Exception {
			out.setIndent("	");
			out.beginArray();
			out.value("0");
			// sleep();
			// out.value("0");
			out.endArray();
		}
	});
}

public static void onReceive(final TermuxApiReceiver _receiver, final Context _context, final Intent _intent) {
	Logger.logDebug(LOG_TAG, LOG_TAG+" onReceive");
	// System.out.println("3");
	context = _context;
	receiver = _receiver;
	intent = _intent;

	sw = new StringWriter();
	pw = new PrintWriter(sw);
	pw.println("test");
	// pw.flush();

	try {
		// writeIn();
		writeJson();
	} catch (Exception e) {
		Logger.logError(LOG_TAG, e.toString());
	}

}
}//class


