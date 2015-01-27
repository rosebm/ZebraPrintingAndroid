package com.zebraprintinglibrary.interfaces;

/**
 * Utilizado para el rastreo y conexion de dispositivos bluetooth
 * @author rblanco
 *
 */
public interface PrinterResponse {
	void processFoundPrinter(String texto);
	void processFinished();
	void processError(String message);
}
