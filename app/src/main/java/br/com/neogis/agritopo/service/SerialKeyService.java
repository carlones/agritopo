package br.com.neogis.agritopo.service;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.dao.tabelas.ChaveSerialDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Usuario;
import br.com.neogis.agritopo.dao.tabelas.UsuarioDaoImpl;
import br.com.neogis.agritopo.model.MyGpsMyLocationProvider;
import br.com.neogis.agritopo.parse.views.SerialKeyView;
import br.com.neogis.agritopo.utils.DateUtils;

/**
 * Created by marci on 21/04/2018.
 */

public class SerialKeyService {
    private Context contexto;
    private Activity activity;
    private ChaveSerialDaoImpl chaveSerialDao;
    private UsuarioDaoImpl usuarioDao;

    public SerialKeyService(Context contexto, Activity activity){
        this.contexto = contexto;
        this.activity = activity;
        chaveSerialDao = new ChaveSerialDaoImpl(contexto);
        usuarioDao = new UsuarioDaoImpl(contexto);
    }

    public boolean containsValidSerialKey(){
        Date currentDate = getCurrentDate();
        ChaveSerial serial = chaveSerialDao.getValid(currentDate.getTime());
        if(serial == null)
            return false;

        return DateUtils.getDaysBetween(currentDate, serial.getDataexpiracao()) >= 0;
    }

    public boolean containsFreeSerialKey(){
        return getFreeTimeDays() > 0;
    }

    public long getFreeTimeDays(){
        ChaveSerial serial = chaveSerialDao.getTrial();
        if(serial == null){
            return 0;
//            serial = insertSerialTrial();
        }
        return DateUtils.getDaysBetween(getCurrentDate(), serial.getDataexpiracao());
    }

    public void saveSerialKey(SerialKeyView serial){
        Usuario usuario = getUsuario();
        usuario.setEmail(serial.email_conta);
        ChaveSerial serialKey = chaveSerialDao.getBySerialKey(serial.chave);
        if(serialKey==null)
            serialKey = new ChaveSerial();

        serialKey.setChave(serial.chave);
        serialKey.setDataexpiracao(serial.valida_ate);
        serialKey.setTipo(serial.tipo);
        serialKey.setUsuarioId(usuario.getUsuarioid());

        usuarioDao.update(usuario);
        chaveSerialDao.save(serialKey);
    }
/*
    private ChaveSerial insertSerialTrial(){
        Usuario usuario = getUsuario();
        ChaveSerial serial = new ChaveSerial(
                0,
                "trial",
                DateUtils.addDays(getCurrentDate(), 15),
                usuario.getUsuarioid(),
                ChaveSerial.ChaveSerialTipo.Gratuito
                );
        chaveSerialDao.insert(serial);
        return serial;
    }
    */

    public Usuario getUsuario() {
        Usuario usuario = usuarioDao.get(1);
        if(usuario == null) {
            usuario = new Usuario(0, "", "", 0);
            new UsuarioDaoImpl(contexto).insert(usuario);
        }
        return usuario;
    }

    private Date getCurrentDate(){
        MyGpsMyLocationProvider gps = new MyGpsMyLocationProvider(contexto, activity, null);
        try {
            Location location = gps.getLastKnownLocation();
            if (location != null)
                return DateUtils.getDateWithOutTime(new Date(location.getTime()));
            else
                return DateUtils.getCurrentDate();
        }finally {
            gps.stopLocationProvider();
        }
    }

    public ChaveSerial getChaveSerial() {
        Date currentDate = getCurrentDate();
        return chaveSerialDao.getValid(currentDate.getTime());
    }

    public ChaveSerial getChaveSerialTrial() {
        return chaveSerialDao.getTrial();
    }

    public JSONObject getDadosDispositivo() throws Exception {
        JSONObject identificacao = new JSONObject();
        ActivityManager actManager = (ActivityManager) contexto.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemoryMB = memInfo.totalMem / (1024 * 1024);
        try {
            identificacao.put("memoria", totalMemoryMB);
            identificacao.put("api", android.os.Build.VERSION.SDK_INT);
            identificacao.put("versaoSo", android.os.Build.VERSION.RELEASE);
            identificacao.put("aparelho", android.os.Build.MODEL);
            identificacao.put("fabricante", Build.MANUFACTURER);
            identificacao.put("so", Build.PRODUCT);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new Exception("Contate o suporte: erro ao identificar dispositivo");
        }
        return identificacao;
    }

    public String getIdDispositivo() {

        // IMEI para GSM, MEID/ESN para CDMA. Nem todos os aparelhos possuem chip de telefonia
        if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        TelephonyManager telephonyManager = getTelephonyManager();
        if( telephonyManager == null )
            return null;
        String deviceId = telephonyManager.getDeviceId();
        if( deviceId != null ) {
//            Utils.info("Telephony:" + deviceId);
            return "Telephony:" + deviceId;
        }

        // Alguns aparelhos deixam valores sem nexo nesse campo (tablet do Carlos)
        String serial = android.os.Build.SERIAL;
        if( serial != null && !serial.equals("0123456789ABCDEF")) {
//            Utils.info("AndroidSerial:" + serial);
            return "AndroidSerial:" + serial;
        }

        // Evitando endereço MAC: se o wifi não estiver ativo, o endereço não será retornado
        // https://stackoverflow.com/questions/11705906/programmatically-getting-the-mac-of-an-android-device

        // Muda a cada formatação
        String androidId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
//        Utils.info("Secure android id: " + androidId);
        return "SecureAndroidId:" + androidId;
    }

    private TelephonyManager getTelephonyManager(){
        if (Build.VERSION.SDK_INT < 23)
            return (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        else{
            if (ContextCompat.checkSelfPermission(contexto, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        1);
            }
            else
                return (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        }
        return null;
    }
}
