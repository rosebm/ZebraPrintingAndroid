package com.zebraprintinglibrary.printer;

import android.graphics.Bitmap;
import android.util.Log;

import com.zebra.android.comm.BluetoothPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.android.printer.PrinterLanguage;
import com.zebra.android.printer.PrinterStatus;
import com.zebra.android.printer.ZebraPrinter;
import com.zebra.android.printer.ZebraPrinterFactory;
import com.zebraprintinglibrary.entities.Printer;
import com.zebraprintinglibrary.utils.GlobalesCustom;

public class PrinterZebraSingleton {
	private static final String TAG = "PrinterSingleton";
	private ZebraPrinterConnection zebraPrinterConnection;
	private ZebraPrinter printer;
	private String macAddress;

	private PrinterZebraSingleton() { 
	}
  
	private static class SingletonHolder { 
	  public static final PrinterZebraSingleton INSTANCE = new PrinterZebraSingleton();
	}

	public static PrinterZebraSingleton getInstance() {
      return SingletonHolder.INSTANCE;
	}

	public synchronized ZebraPrinter connect() {

	  zebraPrinterConnection = new BluetoothPrinterConnection(macAddress);

      try {
      	zebraPrinterConnection.open();
      } catch (ZebraPrinterConnectionException e) {
          Log.d(TAG, "Can't connect", e);
          Sleeper.sleep(1000);
          disconnect();
      }

      if (zebraPrinterConnection.isConnected()) {
          try {
              printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, zebraPrinterConnection);

              Printer currentPrinter = new Printer();
              currentPrinter.setMacAddress(macAddress);
      		  GlobalesCustom.setCurrentPrinter(currentPrinter);
              
          } catch (ZebraPrinterConnectionException e) {
              printer = null;
              Sleeper.sleep(1000);
              disconnect();
          }
      } 

      GlobalesCustom.getInstance().setZebraPrinterConnection(zebraPrinterConnection);
      
      return printer;
	}

	public synchronized void disconnect() {
      try {  
          if (zebraPrinterConnection != null) {
          	zebraPrinterConnection.close();
          }
      } catch (Exception e) {
          Log.i(TAG, e.getMessage());
      } finally {

      }
	}

	public synchronized boolean isConnected() {
	  
	  if (zebraPrinterConnection != null) {
    	  
		  if (zebraPrinterConnection.isConnected())
			  return true;
      } 
      return false;
	}
  
	public synchronized boolean isPrinterConnected() {
	  
      if (printer != null) {
       try {	   
			if (printer.getCurrentStatus().isReadyToPrint)
				 return true;
		} catch (ZebraPrinterConnectionException e) {
			
			e.printStackTrace();
			return false;
		}
      } 
      return false;
	}
  
	public synchronized ZebraPrinterConnection doConnection()  {
	  
      if (zebraPrinterConnection == null || 
    		  !(zebraPrinterConnection.isConnected())){
          printer = connect();
          
          if (printer == null){
        	  Sleeper.sleep(3500);
              connect();
          }
      }  
      
      return zebraPrinterConnection;
	}
  
	public synchronized void doPrint(String data) throws ZebraPrinterConnectionException {
      byte[] dataBytes = data.getBytes();
      zebraPrinterConnection.write(dataBytes);
	}
  
	public synchronized String getFriendlyName() {
	  
	  	if (zebraPrinterConnection != null) {
	  		
	      if (!(zebraPrinterConnection instanceof BluetoothPrinterConnection)) 
	          throw new RuntimeException("Printer not supported");
	      
	      String friendlyName = ((BluetoothPrinterConnection) zebraPrinterConnection).getFriendlyName();
	      
	      return friendlyName;
	  	} else {
	  		return null;
	  	}
	}
  
	public String getMacAddress() {
      return macAddress;
	}

	public void setMacAddress(String macAddress) {
      this.macAddress = macAddress;
	}
  
	public PrinterLanguage getPrinterLanguage() {
	  	if (printer != null){
	      return printer.getPrinterControlLanguage();  
	  	} else {
	  		return null;
	  	}
	}
  
	public PrinterStatus getPrinterStatus(){
      PrinterStatus status = null;    
      
      if (printer != null){
          try {
              status = printer.getCurrentStatus();
          } catch (ZebraPrinterConnectionException e) {
             Log.d("PRINTER_ERROR","NOT CONNECTED");
          }
      }
      return status;
	}
  
	public boolean printerStatusNull(){
	  PrinterStatus status = getPrinterStatus();
	  
	  if (status != null)
		  return false;
	  
	  return true;
	}
 
	public void resetPrinter(){
      printer = null;
      zebraPrinterConnection = null;
	}

	public String getPrinterLanguageName(){
	  String name = "";
	  PrinterLanguage pl = getPrinterLanguage(); 
	  
	  if (pl != null)
		  name = pl.name();
	  
	  return name;
	}
  
	public void printImage(int initImage, Bitmap image, int x, int y) throws ZebraPrinterConnectionException{
		ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, GlobalesCustom.getZebraPrinterConnection());
		printer.getGraphicsUtil().printImage(image, initImage, 0, x, y, false);
	}


  
}
