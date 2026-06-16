package com.termux.api;

import android.content.Context;
import android.os.Looper;
import java.lang.reflect.Method;
// import androidx.test.InstrumentationRegistry;

public class main {

void log(String s) {
	System.out.println(s);
}

public static Context getSystemContext() {
	try {
		Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
		Method method = activityThreadClass.getMethod("systemMain");
		Looper.prepareMainLooper();
		Object activityThread = method.invoke(null);
		System.exit(0);
		Method getSystemContextMethod = activityThreadClass.getMethod("getSystemContext");
		return (Context) getSystemContextMethod.invoke(activityThread);
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
}

void context(){
	// Intent intent = new Intent(InstrumentationRegistry.getContext(), TestComponentsService.class);
	// Workarounds.apply(true, true, true);
	// Context c = FakeContext.get();
	Context c = getSystemContext();
	// log(c.getPackageName());
	// c.getApplicationContext();
	// c.getSystemService(Context.BATTERY_SERVICE);
}

public main(){
	log("abc");
	context();
}

public static void main(String[] args) {
	new main();
}

}
