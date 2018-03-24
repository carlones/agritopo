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
        int zoomMinimo = zoomMin;
        int zoomMaximo = zoomMax;
        GeoPoint pontoCentralizado = pontoCentral;
        try {
            // pegar níves de zoom e Ponto central diretamente do arquivo
            //SQLiteDatabase db_mapa = SQLiteDatabase.openOrCreateDatabase(arquivo.toString(), Context.MODE_PRIVATE, null);
            SQLiteDatabase db_mapa = SQLiteDatabase.openDatabase(arquivo.toString(), null, Context.MODE_PRIVATE);
            Cursor cursor = db_mapa.rawQuery("SELECT value FROM metadata WHERE name IN ('minzoom', 'maxzoom') ORDER BY value", null);
            cursor.moveToFirst();
            zoomMinimo = cursor.getInt(0);
            cursor.moveToNext();
            zoomMaximo = cursor.getInt(0);
            Log.d("Agritopo", String.format("níveis de zoom do arquivo: %d, %d", zoomMin, zoomMax));
            cursor.close();
            cursor = db_mapa.rawQuery("SELECT value FROM metadata WHERE name = 'bounds'", null);
            cursor.moveToFirst();
            String comma_separated_bounds = cursor.getString(0);
            // TESTE   -52.6200367289128, -27.2365439279134, -52.6126988756003, -27.22719027818526
            // VEDERTI -52.71901601171034,-27.09696458299671,-52.70286643123167,-27.08320044921776
            String[] bounds_parts = comma_separated_bounds.split(",");
            double lat = (Double.parseDouble(bounds_parts[1]) + Double.parseDouble(bounds_parts[3])) / 2.0;
            double lon = (Double.parseDouble(bounds_parts[0]) + Double.parseDouble(bounds_parts[2])) / 2.0;
            Log.d("Agritopo", String.format("posições calculadas: %f, %f", lat, lon));
            cursor.close();
            db_mapa.close();
            pontoCentralizado = new GeoPoint(lat, lon);
        } finally {
            this.zoomMin = zoomMinimo;
            this.zoomMax = zoomMaximo;
            this.pontoCentral = pontoCentralizado;
        }
    }
}
