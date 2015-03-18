package org.jacob.geosms;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Localizar extends Activity implements OnMarkerClickListener, OnClickListener{
	
	private GoogleMap mMap;
	private TextView telefono,mensaje;
	private Button btnEliminar;
	ayudanteBD aBD;
	SQLiteDatabase db = null;
	String tel="",msg="",lat="",lon="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localizar);
		
		setUpMapIfNeeded();
	    
	    telefono=(TextView)findViewById(R.id.textView1);
	    mensaje=(TextView)findViewById(R.id.textView2);
	    btnEliminar=(Button)findViewById(R.id.button1);
	    
	    btnEliminar.setOnClickListener(this);
//		
		Bundle bolsaRecibida=getIntent().getExtras();
		tel=bolsaRecibida.getString("num");
		msg=bolsaRecibida.getString("msg");
		lat=bolsaRecibida.getString("lat");
		lon=bolsaRecibida.getString("lon");
//		
		telefono.setText(tel);
		mensaje.setText(msg);
//		
//		try{
		mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))).title("wi-fi"));
//		}catch(Exception e){
//			Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
//		}
		 mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)),15));
		
	}
	
	 private void setUpMapIfNeeded() {
  	   if (mMap == null) {
  	      mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
  	      if (mMap != null) {

  	        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
  	        	
  	        mMap.setMyLocationEnabled(true);
  	      }
  	   }
  	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void eliminar(String mensaje) {
		String[] numeroControl = { mensaje };

		try {
			aBD = new ayudanteBD(this, "SMS", null, 1);
			db = aBD.getReadableDatabase();
			if (db != null) {
				db.execSQL("DELETE FROM mensajes WHERE mensaje=?", numeroControl);
			}

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

		} finally {
			if (db != null)
				db.close();
		}
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId()==R.id.button1){
			eliminar(msg);
			Intent intent=new Intent(this,LeerMensajes.class);
			startActivity(intent);
			finish();
		}
	}
}
