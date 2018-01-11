package br.com.neogis.agritopo.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.io.File;

/**
 * Created by carlo on 06/10/2017.
 */

public class MapaTiles {
    public String formatoImagem = ".png";
    public int zoomMin;
    public int zoomMax;
    public GeoPoint pontoCentral;

    public MapaTiles(File arquivo, GeoPoint pontoCentral, int zoomMin, int zoomMax) {
        try {
            // pegar níves de zoom e Ponto central diretamente do arquivo
            //SQLiteDatabase db_mapa = SQLiteDatabase.openOrCreateDatabase(arquivo.toString(), Context.MODE_PRIVATE, null);
            SQLiteDatabase db_mapa = SQLiteDatabase.openDatabase(arquivo.toString(), null, Context.MODE_PRIVATE);
            Cursor cursor = db_mapa.rawQuery("SELECT value FROM metadata WHERE name IN ('minzoom', 'maxzoom') ORDER BY value", null);
            cursor.moveToFirst();
            this.zoomMin = cursor.getInt(0);
            cursor.moveToNext();
            this.zoomMax = cursor.getInt(0);
            Log.d("Agritopo", String.format("níveis de zoom do arquivo: %d, %d", zoomMin, zoomMax));
            cursor.close();
            cursor = db_mapa.rawQuery("SELECT value FROM metadata WHERE name = 'bounds'", null);
            cursor.moveToFirst();
            String comma_separated_bounds = cursor.getString(0); // -52.70603474080805,-27.07971659645249,-52.70230512917284,-27.07683254649346
            String[] bounds_parts = comma_separated_bounds.split(",");
            double lat = (Double.parseDouble(bounds_parts[1]) + Double.parseDouble(bounds_parts[3])) / 2.0;
            double lon = (Double.parseDouble(bounds_parts[0]) + Double.parseDouble(bounds_parts[2])) / 2.0;
            Log.d("Agritopo", String.format("posições calculadas: %f, %f", lat, lon));
            cursor.close();
            db_mapa.close();

            pontoCentral = new GeoPoint(lat, lon);
        } catch (Exception e) {
            this.zoomMin = zoomMin;
            this.zoomMax = zoomMax;
            this.pontoCentral = pontoCentral;
        }
    }
}
