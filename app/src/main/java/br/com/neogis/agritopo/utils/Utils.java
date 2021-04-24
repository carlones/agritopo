package br.com.neogis.agritopo.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.PermissionRequest;
import android.widget.Toast;
import org.osmdroid.util.GeoPoint;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import br.com.neogis.agritopo.singleton.Configuration;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by carlo on 14/10/2017.
 */

public final class Utils {

    /**
     * Mostra mensagem no formato torradeira
     *
     * @param context  indica o contexto
     * @param mensagem mensagem a exibir
     */
    public static void toast(Context context, String mensagem) {
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }

    /**
     * Mostra mensagem no formato de diálogo, com botão
     *
     * @param context  indica o contexto
     * @param titulo   título da mensagem
     * @param mensagem mensagem a exibir
     * @param botao    texto a mostrar no botão
     */
    public static void alert(Context context, String titulo, String mensagem, String botao) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(mensagem);
        dlgAlert.setTitle(titulo);
        dlgAlert.setPositiveButton(botao,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void info(String msg) {
        Log.i("Agritopo", msg);
    }

    /**
     * Retorna um Ponto com o tamanho da tela onde x é comprimento (width) e y é altura (height)
     *
     * @param context indica o contexto
     * @return Point onde x é width e y é height
     */
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static void desenharMira(Canvas c, Context context) {
        int tamanhoCruz = 100;
        float diametroCirculo = 60.0f;
        //float espessuraLinha = 6.0f;

        Paint paint = new Paint();
        paint.setColor(Configuration.getInstance().CorDoCursor);
        paint.setStrokeWidth(Configuration.getInstance().EspessuraMiraMapeamento);
        paint.setStyle(Paint.Style.STROKE);
        int centroX = c.getWidth() / 2;
        int centroY = c.getHeight() / 2;

        c.drawLine(centroX - (tamanhoCruz / 2), centroY, centroX + (tamanhoCruz / 2), centroY, paint);
        c.drawLine(centroX, centroY - (tamanhoCruz / 2), centroX, centroY + (tamanhoCruz / 2), paint);
        c.drawCircle(centroX, centroY, diametroCirculo / 2, paint);
    }

    public static String getFormattedLocationInDegree(GeoPoint point) {
        String latitude = getFormattedLatitudeInDegree(point.getLatitude());
        String longitude = getFormattedLongitudeInDegree(point.getLongitude());
        return latitude + " " + longitude;
    }

    public static String getFormattedLongitudeInDegree(double longitude) {
        StringBuilder builder = new StringBuilder();
        String letter = longitude < 0 ? " W" : " E";
        String[] seconds;

        String longitudeDegrees = Location.convert(Math.abs(longitude), Location.FORMAT_SECONDS);
        String[] longitudeSplit = longitudeDegrees.split(":");
        builder.append(longitudeSplit[0]);
        builder.append("°");
        builder.append(String.format("%02d", Integer.parseInt(longitudeSplit[1])));
        builder.append("'");
        seconds = longitudeSplit[2].replace(".", ",").split(",");
        builder.append(String.format("%02d", Integer.parseInt(seconds[0])));
//        builder.append(".");
//        builder.append(String.format("%04d", Integer.parseInt(seconds[1])));
        builder.append("\"");
        builder.append(letter);

        return builder.toString();
    }

    public static String getFormattedLatitudeInDegree(double latitude) {
        StringBuilder builder = new StringBuilder();
        String letter = latitude < 0 ? " S" : " N";
        String[] seconds;

        String latitudeDegrees = Location.convert(Math.abs(latitude), Location.FORMAT_SECONDS);
        String[] latitudeSplit = latitudeDegrees.split(":");
        builder.append(latitudeSplit[0]);
        builder.append("°");
        builder.append(String.format("%02d", Integer.parseInt(latitudeSplit[1])));
        builder.append("'");
        seconds = latitudeSplit[2].replace(".", ",").split(",");
        builder.append(String.format("%02d", Integer.parseInt(seconds[0])));
//        builder.append(".");
//        builder.append(String.format("%04d", Integer.parseInt(seconds[1])));
        builder.append("\"");
        builder.append(letter);

        return builder.toString();
    }

    // https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
    public static double medirDistanciaEmMetros(GeoPoint p1, GeoPoint p2) {
        double dLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
        double dLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(p1.getLatitude())) * Math.cos(Math.toRadians(p2.getLatitude())) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Constantes.RAIO_DA_TERRA_EM_METROS * c; // Distance in m
    }

    /*public static TelephonyManager getTelephonyManager(Context context) {
        TelephonyManager telephonyManager = null;
        if (Build.VERSION.SDK_INT < 23)
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            } else
                throw new SecurityException("Sem permissão de ler o status do telefone (READ_PHONE_STATE)");
                //ContextCompat.requestPermissions(context,
                //        new String[]{Manifest.permission.READ_PHONE_STATE},
                //        1);
        }
        return telephonyManager;
    }*/

    public static String getDeviceId(Context context) {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
        /*WifiManager m_wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        String m_wlanMacAdd = m_wm.getConnectionInfo().getMacAddress();
        return m_wlanMacAdd;*/

        /*
        // IMEI para GSM, MEID/ESN para CDMA. Nem todos os aparelhos possuem chip de telefonia
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        String deviceId = getTelephonyManager(context).getDeviceId();
        if (deviceId != null) {
            Utils.info("Telephony:" + deviceId);
            return "Telephony:" + deviceId;
        }

        // Alguns aparelhos deixam valores sem nexo nesse campo (tablet do Carlos)
        String serial = android.os.Build.SERIAL;
        if (serial != null && !serial.equals("0123456789ABCDEF")) {
            Utils.info("AndroidSerial:" + serial);
            return "AndroidSerial:" + serial;
        }

        // Evitando endereço MAC: se o wifi não estiver ativo, o endereço não será retornado
        // https://stackoverflow.com/questions/11705906/programmatically-getting-the-mac-of-an-android-device

        // Muda a cada formatação
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Utils.info("Secure android id: " + androidId);
        return "SecureAndroidId:" + androidId;*/
    }

    public static byte[] substring(byte[] array, int start, int end) {
        if (end <= start)
            return null;
        int length = (end - start);

        byte[] newArray = new byte[length];
        System.arraycopy(array, start, newArray, 0, length);
        return newArray;
    }
}
