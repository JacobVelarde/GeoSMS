package org.jacob.geosms;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LeerMensajes extends Activity implements OnClickListener,
		OnItemClickListener {

	Button btnNuevo;
	ArrayList<ItemUsuarios> items = new ArrayList<ItemUsuarios>();
	ArrayList<String> coordenadas = new ArrayList<String>();
	ArrayList<ItemUsuarios> itemsCompra = new ArrayList<ItemUsuarios>();

	ayudanteBD aBD;
	SQLiteDatabase db = null;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leer_mensajes);

		lv = (ListView) findViewById(R.id.listView1);
		btnNuevo = (Button) findViewById(R.id.button1);
		btnNuevo.setOnClickListener(this);
		lv.setOnItemClickListener(this);

		creaLista();

	}

	public void creaLista() {
		int pos = 0;
		try {
			aBD = new ayudanteBD(this, "SMS", null, 1);
			db = aBD.getReadableDatabase();
			if (db != null) {
				// Vector<String> resultado2 = new Vector<String>();
				Cursor cursor = db.rawQuery("SELECT * FROM mensajes", null);
				while (cursor.moveToNext()) {
					String telefono = cursor.getString(0);
					String mensaje = cursor.getString(1);
					// (idTelefono TEXT primary key,nombre TEXT,nomEmpresa
					// TEXT,correo INTEGER,pass TEXT)";
					itemsCompra = obtenerItems(pos, telefono, mensaje);
					pos++;
					coordenadas.add(cursor.getString(2));
					coordenadas.add(cursor.getString(3));
				}
				cursor.close();
				if(itemsCompra.size()==0){
					Toast.makeText(this, "No tienes mensajes", Toast.LENGTH_LONG).show();
				}
				ItemUserAdapter adapter = new ItemUserAdapter(this, itemsCompra);
				lv.setAdapter(adapter);
				
			}

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT)
					.show();

		} finally {
			if (db != null)
				db.close();
		}
	}

	private ArrayList<ItemUsuarios> obtenerItems(int pos, String nombre,
			String texto) {

		items.add(new ItemUsuarios(pos, nombre, texto, ""));

		return items;
	}
//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//		creaLista();
//	}
//	
//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		creaLista();
//	}
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		creaLista();
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			Intent intent = new Intent(this, NuevoMensaje.class);
			startActivity(intent);
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		
		ViewGroup grupo = (ViewGroup) arg1;
		TextView tvN= (TextView) grupo.getChildAt(1);
		TextView tvM= (TextView) grupo.getChildAt(2);
		String num=tvN.getText().toString();
		String msg=tvM.getText().toString();

		int pos = 0;
		String lat = "";
		String lon = "";
		if (arg2 == 0) {
			lat = coordenadas.get(0);
			lon = coordenadas.get(1);
		} else {
			pos = arg2 * 2;
			lat = coordenadas.get(pos);
			lon = coordenadas.get(pos + 1);
		}

		Intent intet = new Intent(this, Localizar.class);
		Bundle bolsa=new Bundle();
		bolsa.putString("num",num);
		bolsa.putString("msg",msg);
		bolsa.putString("lat",lat);
		bolsa.putString("lon",lon);
		//Toast.makeText(this, num+""+msg+""+lat+""+lon, Toast.LENGTH_LONG).show();
		intet.putExtras(bolsa);
		startActivity(intet);

	}

}
