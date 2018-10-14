package br.com.neogis.agritopo.receivers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.service.Sincronizacao.SincronizacaoDadosService;

/**
 * Created by marci on 16/08/2018.
 */

public class IntegracaoReceiver extends BroadcastReceiver {
    private final static String TAG = IntegracaoReceiver.class.getSimpleName();

    private enum TipoChamada { nenhum, wifi, agritopo, boot, cancelNotification }

    @Override
    public void onReceive(Context context, Intent intent) {
        TipoChamada tipo = getTypeCall(intent);
        switch (tipo)
        {
            case nenhum: return;
            case wifi: runService(context, 10000); break;
            case agritopo: runService(context, 10); break;
            case boot: runService(context, 10000); break;
            case cancelNotification: cancelNotification(context); break;
        }
    }

    private TipoChamada getTypeCall(Intent intent){
        if(wiFiConected(intent))
            return TipoChamada.wifi;

        if(agritopoCalled(intent))
            return TipoChamada.agritopo;

        if(cancelNotificationCalled(intent))
            return TipoChamada.cancelNotification;

        if(bootReceived(intent))
            return TipoChamada.boot;

        return TipoChamada.nenhum;
    }

    private boolean wiFiConected(Intent intent){
        if(!WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction()))
            return false;

        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
        if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Wifi is now enabled");
            }
            return true;
        }
        return false;
    }

    private boolean agritopoCalled(Intent intent){
        return "br.com.neogis.agritopo.receiver.integracaoreceiver".equals(intent.getAction());
    }

    private boolean cancelNotificationCalled(Intent intent){
        return "br.com.neogis.agritopo.receiver.cancelnotification".equals(intent.getAction());
    }

    private boolean bootReceived(Intent intent) {
        return Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction());
    }

    private void cancelNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(SincronizacaoDadosService.NOTIFICACAO_ID);
    }

    private void runService(final Context context, int delay){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(context, SincronizacaoDadosService.class);
                context.startService(it);
            }
        }, delay);

        SetAlarm(context);
    }

    private void SetAlarm(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        float horas = prefs.getFloat(context.getResources().getString(R.string.pref_key_tempo_sincronizacao), 0.5f) * 12;
        if(horas < 1.0f)
            horas = 1.0f;

        horas = horas * 60 * 60;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, (int)(horas));

        Intent alertIntent = new Intent("br.com.neogis.agritopo.receiver.integracaoreceiver");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100001, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}
