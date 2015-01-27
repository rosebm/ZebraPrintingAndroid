package com.zebraprintinglibrary.printer;

import java.io.UnsupportedEncodingException;

import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebraprintinglibrary.entities.Printer;
import com.zebraprintinglibrary.utils.GlobalesCustom;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
 
public class PrinterZebraService extends Service{

	private static PrinterZebraService INSTANCE;
	
	public static PrinterZebraService getInstance () {
		if (INSTANCE == null)
			INSTANCE = new PrinterZebraService();
		return INSTANCE;
	}
	
	private PrinterZebraService() {
		
	}
    
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
    }
 
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public String setTextToPrinter(Printer printer){
    	String textToPrint;
		int paperWidth = printer.getWidth();
		int fontName = printer.getFontName();
		int fontSize = printer.getFontSize();
		String align = printer.getAlign();
		int x = printer.getX();
		int y = printer.getY();
		String text = printer.getText();
		
		textToPrint = "! 0 300 300 30 1\r\n" + 
				"PAGE-WIDTH " + paperWidth + "\r\n" +
				"ON-FEED IGNORE\r\n" +
				align + "\r\n" +
				"COUNTRY SPAIN \r\n" +
				"T " + fontName + " " + fontSize + " " + x + " " + y + " " + text + "\r\n" + 
				"PRINT\r\n";
	    
	    return textToPrint;
    }

    public String setImageToPrinter(){
    	String textToImage = "";
    	
    	textToImage = "! UTILITIES \r\n" +
				 	"IN-MILLIMETERS \r\n" +
				 	"SETFF 5 2 \r\n" + 
				 	"PRINT \r\n";
    	
    	return textToImage;
    }
    
	public final boolean printText(String texto) throws UnsupportedEncodingException, 
		ZebraPrinterConnectionException 
	{
        if (!GlobalesCustom.getZebraPrinterConnection().isConnected()) 	
        	return false; 
        
        if (GlobalesCustom.getZebraPrinterConnection().isConnected()) 
        	GlobalesCustom.getZebraPrinterConnection().write(texto.getBytes("CP1252"));
        
		return true;
	}
	
	public void sendText(String text){
		try {
			printText(text);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

 
}