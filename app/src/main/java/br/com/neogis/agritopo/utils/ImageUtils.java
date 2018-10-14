package br.com.neogis.agritopo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
/**
 * Created by marci on 26/05/2018.
 */

public class ImageUtils {
    public static Bitmap createPreviewImage(Context context, Uri image, final int size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            InputStream fis = context.getContentResolver().openInputStream(image);
            BitmapFactory.decodeStream(fis, null, options);
            fis.close();

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(options.outWidth / scale / 2 >= size &&
                    options.outHeight / scale / 2 >= size) {
                scale *= 2;
            }

            // Decode with inSampleSize
            options = new BitmapFactory.Options();
            options.inSampleSize = scale;
            fis = context.getContentResolver().openInputStream(image);
            Bitmap result = BitmapFactory.decodeStream(fis, null, options);
            fis.close();

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeLocation(Context context, Uri uri, double latitude, double longitude){
        /*
        try {
            ExifInterface exif = new ExifInterface(uri.getPath());

            //String latitudeStr = "90/1,12/1,30/1";
            double alat = Math.abs(latitude);
            String dms = Location.convert(alat, Location.FORMAT_SECONDS);
            String[] splits = dms.split(":");
            String[] secnds = (splits[2]).split("\\.");
            String seconds;
            if (secnds.length == 0) {
                seconds = splits[2];
            } else {
                seconds = secnds[0];
            }
            String latitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitudeStr);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latitude > 0 ? "N" : "S");

            double alon = Math.abs(longitude);
            dms = Location.convert(alon, Location.FORMAT_SECONDS);
            splits = dms.split(":");
            secnds = (splits[2]).split("\\.");

            if (secnds.length == 0) {
                seconds = splits[2];
            } else {
                seconds = secnds[0];
            }
            String longitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitudeStr);
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, longitude > 0 ? "E" : "W");

            exif.saveAttributes();
        }catch (IOException ex){
            Toast.makeText(context, "Não foi possivel gravar coordenadas na imagem. Motivo: Arquivo não encontrado",Toast.LENGTH_LONG).show();
        }
        */
    }
}
