package com.cpsteam.torontoso.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.cpsteam.torontoso.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TorsoCADUncaughtExceptionHandler implements
		UncaughtExceptionHandler {

	private final Context myContext;

	public TorsoCADUncaughtExceptionHandler(Context context) {
		myContext = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e("UncaughtException", "UncaughtException trace: ", ex);

		try {
			Process process = Runtime.getRuntime().exec("logcat -d");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			StringBuilder log = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				log.append(line + "\n");
			}

			String logFilePath = Environment.getExternalStorageDirectory() + File.separator + myContext.getString(R.string.app_name) + "_log_"+ getCurrentTimeStamp() + ".txt";

			File logFile = new File(logFilePath);
			if (!logFile.exists())
				logFile.createNewFile();

			FileOutputStream outStream = new FileOutputStream(logFile, true);
			byte[] buffer = log.toString().getBytes();

			outStream.write(buffer);
			outStream.close();
			myContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					Uri.parse("file://" + logFilePath)));
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	    
	    android.os.Process.killProcess(android.os.Process.myPid());
	    System.exit(10);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
}
