package com.zebraprintinglibrary.entities;

import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.android.discovery.BluetoothDiscoverer;
import com.zebra.android.discovery.DiscoveredPrinter;
import com.zebra.android.discovery.DiscoveredPrinterBluetooth;
import com.zebra.android.discovery.DiscoveryHandler;
import com.zebraprintinglibrary.interfaces.PrinterResponse;

import android.content.Context;
import android.os.Looper;

/**
 * Implementa la busqueda de dispositivos para la conexion
 * @author rblanco
 *
 */
public class PrinterFinder implements DiscoveryHandler{
    private final String mac;
    private Thread discoverThread;
    public PrinterResponse delegate = null;
    private final Context context;

    public PrinterFinder(Context ctx,  PrinterResponse delegate, String mac){
        this.context = ctx;
        this.delegate = delegate;
        this.mac = mac;
        doDiscover();
    }
    
    public void cancelDiscovery(){
        discoverThread.interrupt();
    }

    private void doDiscover() {

       discoverThread =  new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                try {

                    BluetoothDiscoverer.findPrinters(context, PrinterFinder.this);
                } catch (InterruptedException e) {
                	e.printStackTrace();
                } catch (ZebraPrinterConnectionException ze) {
					// TODO Auto-generated catch block
					ze.printStackTrace();
				} finally {

                    Looper.myLooper().quit();
                }
            }
        });
       discoverThread.start();
    }

    @Override
    public void foundPrinter(DiscoveredPrinter discoveredPrinter) {
         DiscoveredPrinterBluetooth p = (DiscoveredPrinterBluetooth) discoveredPrinter;
         String friendlyName = p.friendlyName != null ? p.friendlyName:"?";
         String texto = p.address + " (" + friendlyName + ")";
         
         //Retorna el nombre del dispositivo a ZebraPrintApplication
         //a traves de un Interface para evitar llamada directa a lib zebra
         delegate.processFoundPrinter(texto);
    }

    @Override
    public void discoveryFinished() {
    	delegate.processFinished();
    }

    @Override
    public void discoveryError(String message) {
    	delegate.processError(message);
    }
    
    

}
