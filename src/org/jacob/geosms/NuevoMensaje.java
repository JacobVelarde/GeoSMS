package org.jacob.geosms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NuevoMensaje extends Activity implements OnClickListener{

	Button btnEnviar;
	EditText numero,texto;
	LocationManager locManager;
	String coordenadas;
	String num;
	String mensaje;
	TextView anuncio;
	boolean bandera=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_mensaje);
		
		btnEnviar=(Button)findViewById(R.id.button1);
		numero=(EditText)findViewById(R.id.editText2);
		texto=(EditText)findViewById(R.id.editText1);
		anuncio=(TextView)findViewById(R.id.textView1);
		//anuncio.setText("");
		
		btnEnviar.setOnClickListener(this);
		localizacion();
	}
	
	private void localizacion(){
		locManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener locListener=new MyLocationListener();
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locListener);
	}
	
	private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			coordenadas=location.getLatitude()+";;"+location.getLongitude();
			if(bandera){
			enviarSms(num, coordenadas+";;"+mensaje);
			mensaje("Mensaje Enviado");
			bandera=false;
			finish();
			}
		}

		@Override
		public void onProviderDisabled(String arg0) {
			mensaje("Gps Desactivado");
			btnEnviar.setVisibility(View.GONE);
			anuncio.setVisibility(View.VISIBLE);
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			mensaje("Gps Activado");
			btnEnviar.setVisibility(View.VISIBLE);
			anuncio.setVisibility(View.GONE);
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			
		}
		
	}


	private void enviarSms(String tel, String men){
		try{
		SmsManager sm= SmsManager.getDefault();
		sm.sendTextMessage(tel, null, men, null, null);
		}catch(Exception t){
			mensaje(t.getMessage());
		}
	}
	
	private void mensaje(String m){
		Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			num=numero.getText().toString().trim();
			mensaje=texto.getText().toString().trim();
			
			if(num.compareTo("")==0 || mensaje.compareTo("")==0){
				mensaje("Campos vacios");
			}else{
				bandera=true;
				mensaje("Enviando Mensaje");
				Intent intent=new Intent(this, LeerMensajes.class);
				startActivity(intent);
				finish();
			}
			break;
		}
		
	}
}
