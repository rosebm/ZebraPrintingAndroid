package com.example.zebraprintapplication;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebraprintinglibrary.PrintLibrary;
import com.zebraprintinglibrary.entities.Printer;
import com.zebraprintinglibrary.entities.PrinterFinder;
import com.zebraprintinglibrary.interfaces.PrinterResponse;
import com.zebraprintinglibrary.utils.GlobalesCustom;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ListActivity {
	
	private Button btn_connect;
	private Button btn_print;
	private ImageButton btn_refresh;
	private EditText etx_macAddress;
	private Context mContext;
	private String bluetoothAddress;
	private String TAG = "App";
	private PrinterResponse printerResponse;
	private Printer currentPrinter;
	private boolean searchFinished;
	private ArrayList<String> discoveredPrinters = null;
    private ArrayAdapter<String> mArrayAdapter;
    private Bitmap image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mContext = this;
        etx_macAddress = (EditText) this.findViewById(R.id.etx_macAddress);
        etx_macAddress.setText(PrintLibrary.getPrinterZebraSingleton().getMacAddress());
        btn_print = (Button) this.findViewById(R.id.btn_print);
        btn_connect = (Button) this.findViewById(R.id.btn_connect);
        btn_refresh = (ImageButton)findViewById(R.id.btn_refresh);       
        
        defineEvents();
        setProgressBarIndeterminateVisibility(true);
        discoveredPrinters = new ArrayList<String>();
        setupListAdapter();
        findPrinters();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void defineEvents() {
    	btn_connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        Looper.prepare();
                        enableTestButton(false);
                        doConnection();
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                }).start();
            }
        });

    	btn_print.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        Looper.prepare();
                        enableTestButton(false);
                        printText("This is a printing test");
                        printImage();
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                }).start();
            }
        });

    	btn_refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	setProgressBarIndeterminateVisibility(true);
                findPrinters();
            }
        });
    	
    	etx_macAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	PrintLibrary.getPrinterZebraSingleton().setMacAddress(s.toString());
                
            }

            @Override
            public void afterTextChanged(Editable s) {
            	PrintLibrary.getPrinterZebraSingleton().setMacAddress(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            	PrintLibrary.getPrinterZebraSingleton().setMacAddress(s.toString());

            }
        });

        printerResponse = new PrinterResponse(){

			@Override
			public void processFoundPrinter(final String texto) {
                if (!discoveredPrinters.contains(texto)) {
                    discoveredPrinters.add(texto);
                    mArrayAdapter.notifyDataSetChanged();
                }
			}

			@Override
			public void processFinished() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
			          public void run() {
			        	  searchFinished = true;
			              Toast.makeText(MainActivity.this, "Discovered devices" + " " + discoveredPrinters.size() ,
			                      Toast.LENGTH_SHORT).show();
			              setProgressBarIndeterminateVisibility(false);
			              btn_refresh.setEnabled(true);
			          }
			      });
			}

			@Override
			public void processError(String message) {
				// TODO Auto-generated method stub
				Log.e(TAG, message);
			}};
 
    }
    
    private void findPrinters() {
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                    	btn_refresh.setEnabled(false);
                    }
                });

                Looper.prepare();
                try {
                	searchFinished = false;
                	PrinterFinder pf = new PrinterFinder(mContext,  printerResponse, PrintLibrary.getPrinterZebraSingleton().getMacAddress());
                } 
                catch (Exception e){
                	Log.e(TAG, e.getMessage());
                }
                finally {
                    Looper.myLooper().quit();
                }
            }
        }).start();
    }
    
    public void connect() {
        bluetoothAddress = getMacAddressFieldText();
        PrintLibrary.getPrinterZebraSingleton().setMacAddress(getMacAddressFieldText());
        
        try {
        	PrintLibrary.getGlobals().setZebraPrinterConnection(PrintLibrary.getPrinterZebraSingleton().doConnection());
            
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            disconnect();
        }

        if (PrintLibrary.getPrinterZebraSingleton().isConnected()) {
			currentPrinter = GlobalesCustom.getCurrentPrinter();
        	currentPrinter.setMacAddress(bluetoothAddress);
        	
            try {
                setConnectionButtonText();
                enableTestButton(true);
                
            } catch (Exception e) {
            	Log.e(TAG, e.getMessage());
                disconnect();
            }
        }

    }

    public void disconnect() {
    	
    	currentPrinter = null;
    	
        try {
            PrintLibrary.getPrinterZebraSingleton().disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } 
    }
    
    private void setupListAdapter() {
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, discoveredPrinters);
        setListAdapter(mArrayAdapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	etx_macAddress.setText(l.getItemAtPosition(position).toString().substring(0, 17).trim());
        
        new Thread(new Runnable() {
            public void run() {
                enableTestButton(false);
                Looper.prepare();
                enableTestButton(true);
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();

    }

    private void setConnectionButtonText(){
        runOnUiThread(new Runnable() {
            public void run() {
                if (PrintLibrary.getPrinterZebraSingleton().isConnected()) {
                	btn_connect.setText(getString(R.string.cfgDisconnect, PrintLibrary.getPrinterZebraSingleton().getFriendlyName() ));
                } else {
                	btn_connect.setText(getString(R.string.cfgConnect));
                }
            }
        });
        
    }

    private void enableTestButton(final boolean enabled) {
        runOnUiThread(new Runnable() {
            public void run() {
                btn_connect.setEnabled(enabled);
                btn_print.setEnabled(enabled);
                btn_refresh.setEnabled(enabled);
            }
        });
    }
    
    private String getMacAddressFieldText() {
        return etx_macAddress.getText().toString();
    }

    
    private void doConnection() {

        if (!PrintLibrary.getPrinterZebraSingleton().isConnected())
            connect();
        else
            disconnect();

        enableTestButton(true);
        setConnectionButtonText();
    }

    private void printText(String texto){
    	try {
    		if (currentPrinter != null){
    			currentPrinter.setText(texto);

    			texto = PrintLibrary.getPrinterZebraService().setTextToPrinter(currentPrinter);
    			PrintLibrary.getPrinterZebraService().printText(texto);
    		}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ZebraPrinterConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void printImage(){
    	BitmapFactory.Options options = new BitmapFactory.Options();
		image = null;

		int initImage = centerImage();
		try {
			image = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.image,options);
			String str = PrintLibrary.getPrinterZebraService().setImageToPrinter();	
			PrintLibrary.getPrinterZebraService().sendText(str);
			initImage = centerImage();
			PrintLibrary.getPrinterZebraSingleton().printImage(initImage, image, 200, 100);
		} catch (ZebraPrinterConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	private int centerImage(){
		int position = 0;

		if (currentPrinter != null){
			currentPrinter.setInches(3);
			position = (currentPrinter.getWidth() - 200) / 2;
		}
		
		return position;
	}

    
}
