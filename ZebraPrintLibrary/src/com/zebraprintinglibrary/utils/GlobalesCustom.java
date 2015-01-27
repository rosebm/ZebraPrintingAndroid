package com.zebraprintinglibrary.utils;

import java.util.ArrayList;

import android.widget.ArrayAdapter;

import com.zebra.android.comm.ZebraPrinterConnection;
import com.zebraprintinglibrary.entities.Printer;

public class GlobalesCustom {
	
	public static GlobalesCustom globals;
	private static ZebraPrinterConnection zebraPrinterConnection;
	private static Printer currentPrinter;

	private GlobalesCustom(){}
	
	public static GlobalesCustom getInstance(){
		
		if (globals == null)
			globals = new GlobalesCustom();
		
		return globals;
	}
	
	public static ZebraPrinterConnection getZebraPrinterConnection() {
		return zebraPrinterConnection;
	}

	public void setZebraPrinterConnection(ZebraPrinterConnection zebraPrinterConnection) {
		this.zebraPrinterConnection = zebraPrinterConnection;
	}

	public static Printer getCurrentPrinter() {
		return currentPrinter;
	}

	public static void setCurrentPrinter(Printer currentPrinter) {
		GlobalesCustom.currentPrinter = currentPrinter;
	}

}
