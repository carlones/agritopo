package br.com.neogis.agritopo.service.Sincronizacao;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;

import br.com.neogis.agritopo.R;

/**
 * Created by marci on 06/08/2018.
 */

public class SincronizacaoDadosService extends Service implements IntegradorGeral.Completed {
    public final static int NOTIFICACAO_ID = 90001;

    private boolean executando;
    private NotificationManagerCompat notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void onCreate(){
        Log.i("SincronizacaoDados", "onCreate");
        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SincronizacaoDados", "onStartCommand");
        if(!executando) {
            executando = true;
            StartNotification();
            new IntegradorGeral(this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        stopSelf();
        super.onDestroy();
    }

    public void onComplete(boolean sucess){
        executando = false;
        String mensagem = "Sincronização concluida.";
        if(!sucess)
            mensagem = "Sincronização incompleta.";
        StopNotification(mensagem);
        SetAlarmRemoveNotification();
    }

    public void onProgress(int progress){
        NotificationCompat.Builder mBuilder = getNotification();
        mBuilder.setProgress(100, progress, false);
        notificationManager.notify(NOTIFICACAO_ID, mBuilder.build());
    }

    private void StartNotification(){
        NotificationCompat.Builder mBuilder = getNotification();
        notificationManager.notify(NOTIFICACAO_ID, mBuilder.build());
    }

    private void StopNotification(String mensagem){
        NotificationCompat.Builder mBuilder = getNotification()
                .setContentText(mensagem)
                .setProgress(0, 0, false)
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICACAO_ID, mBuilder.build());
    }

    private void SetAlarmRemoveNotification() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 10);

        Intent intent = new Intent("br.com.neogis.agritopo.receiver.cancelnotification");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100002, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private NotificationCompat.Builder getNotification(){
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_info_white_24dp)
                .setContentTitle("Agritopo")
                .setContentText("Sincronização de dados em andamento.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(100, 0, false);
    }


}
