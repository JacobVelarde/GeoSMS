package org.jacob.geosms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class RecibeSMS extends BroadcastReceiver{
	
	ayudanteBD aBD;
	SQLiteDatabase db = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String num="",mensajeCompleto="",mensaje="",lat="",lon="";
		Bundle bundle =intent.getExtras();
		SmsMessage[] msgs=null;
		String str="";
		
		if(bundle!=null){
			Object[] pdus=(Object[]) bundle.get("pdus");
			msgs=new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
				//str+="SMS de: "+msgs[i].getOriginatingAddress();
				num+=msgs[i].getOriginatingAddress().toString();
				//str+=":";
				//str+=msgs[i].getMessageBody().toString();
				mensajeCompleto+=msgs[i].getMessageBody().toString();
				//str+="\n";
			}
			Toast.makeText(context, "Nuevo mensaje", Toast.LENGTH_LONG).show();
			String []cadena=mensajeCompleto.split(";;");
			lat=cadena[0];
			lon=cadena[1];
			mensaje=cadena[2];
			
			try {
				aBD = new ayudanteBD(context, "SMS", null, 1);
				db = aBD.getWritableDatabase();
				if (db != null) {
					db.execSQL("INSERT INTO mensajes VALUES (" + num + ",'"
							+ mensaje +"','"+lat+"','"+lon+"')");
				}
			} catch (Exception e) {
				Toast.makeText(context,e.getMessage().toString(), Toast.LENGTH_LONG).show();

			} finally {
				if (db != null)
					db.close();

			}
		}	
	}
}
