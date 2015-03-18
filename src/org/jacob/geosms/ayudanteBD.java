package org.jacob.geosms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ayudanteBD extends SQLiteOpenHelper {
	String sentenciaCreacionSQL="CREATE TABLE mensajes (id INTEGER, mensaje TEXT, lat TEXT, lon TEXT)";

	public ayudanteBD(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
        //
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sentenciaCreacionSQL);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
