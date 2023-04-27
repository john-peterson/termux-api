package com.termux.api.apis;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper; 
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionInfo;

import androidx.annotation.RequiresPermission;

import com.termux.api.TermuxApiReceiver;
import com.termux.api.util.ResultReturner;
import com.termux.shared.logger.Logger;

import java.io.PrintWriter;
import java.util.ArrayList;

public class SmsSendAPI {

    private static final String LOG_TAG = "SmsSendAPI";

    public static void onReceive(TermuxApiReceiver apiReceiver, Context context, final Intent intent) {
        Logger.logDebug(LOG_TAG, "onReceive");

        ResultReturner.returnData(apiReceiver, intent, new ResultReturner.WithStringInput() {
            @RequiresPermission(allOf = { Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS })
            @Override
            public void writeResult(PrintWriter out) {

Logger.logInfo(LOG_TAG, "writing");
out.append("writing\n");
out.println("print");


String SENT_ACTION = "SMS_SENT_ACTION";
String DELIVERED_ACTION = "SMS_DELIVERED_ACTION";


Looper.prepare();
Looper looper = Looper.myLooper();
final String result[] = new String[2];

final BroadcastReceiver sentReceiver = new BroadcastReceiver()  {
@Override
public void onReceive(Context sContext, Intent sIntent) {
Logger.logInfo(LOG_TAG, "sent: " +   getResultCode());

result[0] = "sent: " +  getResultCode();
looper.quit();
sContext,.unregisterReceiver(this);
} 
};

final BroadcastReceiver deliveredReceiver = new BroadcastReceiver() {
@Override
public void onReceive(Context dContext, Intent dIntent) {
    Logger.logInfo(LOG_TAG, "delivered: " + getResultCode());
result[1] = "delivered: " + getResultCode();

looper.quit();
}
};

PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(SENT_ACTION ), 0 );

PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 1, new Intent(DELIVERED_ACTION), 0);


context.getApplicationContext().registerReceiver(sentReceiver, new IntentFilter(SENT_ACTION));

context.getApplicationContext().registerReceiver(deliveredReceiver, new IntentFilter(DELIVERED_ACTION));



                final SmsManager smsManager = getSmsManager(context, out, intent);
                if(smsManager == null) return;

                String[] recipients = intent.getStringArrayExtra("recipients");

                if (recipients == null) {
                    // Used by old versions of termux-send-sms.
                    String recipient = intent.getStringExtra("recipient");
                    if (recipient != null) recipients = new String[]{recipient};
                }

                if (recipients == null || recipients.length == 0) {
                    Logger.logError(LOG_TAG, "No recipient given");
                } else {
                    final ArrayList<String> messages = smsManager.divideMessage(inputString);
                    for (String recipient : recipients) {

Logger.logInfo(LOG_TAG, "Calling for network...");
out.append("Waiting for results...");
System.out.println("Print wait"); 

smsManager.sendTextMessage(recipient, null, inputString, sentIntent, deliveredIntent);

Logger.logInfo(LOG_TAG, "Waiting before loop...");


Looper.loop();


Logger.logInfo(LOG_TAG, "Loop ended  ...");

out.append(result[0] + "\n");
Looper.loop();
out.append(result[1] + "\n");
                    }
                }
            }
        });
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    static SmsManager getSmsManager(Context context, PrintWriter out, final Intent intent) {
        int slot = intent.getIntExtra("slot", -1);
        if(slot == -1) {
            return SmsManager.getDefault();
        } else {
            SubscriptionManager sm = context.getSystemService(SubscriptionManager.class);
            if(sm == null) {
                Logger.logError(LOG_TAG, "SubscriptionManager not supported");
                return null;
            }
            for(SubscriptionInfo si: sm.getActiveSubscriptionInfoList()) {
                if(si.getSimSlotIndex() == slot) {
                    return SmsManager.getSmsManagerForSubscriptionId(si.getSubscriptionId());
                }
            }
            out.append("Sim slot "+slot+" not found");
            return null;
        }
    }

}
