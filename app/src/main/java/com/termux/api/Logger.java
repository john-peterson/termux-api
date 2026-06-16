package com.termux.api;
public class Logger {
	public static void logDebug(String t, String s) {
		System.out.println(s);
	}
	public static void logError(String t, String s) {
		System.out.println(s);
	}
	public static void logInfo(String t, String s) {
		System.out.println(s);
	}
	public static void logStackTraceWithMessage(String t, String s, Throwable r){
		System.out.println(s);
	}
}


