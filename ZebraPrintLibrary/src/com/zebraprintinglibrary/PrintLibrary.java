package com.zebraprintinglibrary;

import com.zebraprintinglibrary.printer.PrinterZebraService;
import com.zebraprintinglibrary.printer.PrinterZebraSingleton;
import com.zebraprintinglibrary.utils.GlobalesCustom;

public class PrintLibrary {
	private static PrintLibrary INSTANCE;
//	public static Printer actualPrinter = new Printer();
	
	private PrintLibrary(){
	}
	
	public static PrintLibrary getInstance(){
		if (INSTANCE == null)
			INSTANCE = new PrintLibrary();
		
		return INSTANCE;
	}
	
	public static PrinterZebraService getPrinterZebraService(){
		return PrinterZebraService.getInstance();
	}
	
	public static PrinterZebraSingleton getPrinterZebraSingleton(){
		return PrinterZebraSingleton.getInstance();
	}
	
	public static GlobalesCustom getGlobals(){
		return GlobalesCustom.getInstance();
	}

}
