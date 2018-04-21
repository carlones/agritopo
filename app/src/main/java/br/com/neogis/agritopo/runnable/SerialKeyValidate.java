package br.com.neogis.agritopo.runnable;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import br.com.neogis.agritopo.parse.JsonParse;
import br.com.neogis.agritopo.parse.views.SerialKeyView;
import br.com.neogis.agritopo.utils.NetworkUtils;

import static br.com.neogis.agritopo.utils.Constantes.ENDERECO_SERVIDOR_INTEGRACAO;

/**
 * Created by marci on 14/04/2018.
 */

public class SerialKeyValidate implements Runnable {
    private String serialKey;
    private String email;
    private String deviceId;
    private OnSerialValidate validate;

    public SerialKeyValidate(String serialKey, String email, String deviceId, OnSerialValidate validate){
        this.serialKey = serialKey;
        this.email = email;
        this.deviceId = deviceId;
        this.validate = validate;
    }

    @Override
    public void run() {
        validate.onSucess();
        try {
            String url = ENDERECO_SERVIDOR_INTEGRACAO +
                    "/api/SerialKeyView/ProcessSerialKey?" +
                    "serialKey=" + serialKey.replace("-", "") +
                    "&deviceId=" + deviceId +
                    "&email=" + email;
            String retorno = NetworkUtils.getJSONFromAPI(url);
            SerialKeyView serial = processJson(retorno);
            if(serial == null || serial.expiration.getTime() < Calendar.getInstance().getTime().getTime())
                validate.onFail();
            else
                validate.onSucess();

        } catch (Exception e) {
            Log.e("SerialKeyValidate", e.getMessage(), e);
            validate.onFail();
        }
    }

    public interface OnSerialValidate {

        void onSucess();
        void onFail();
    }

    private SerialKeyView processJson(String retorno){
        Log.i("SerialKeyValidate", "Retorno: " + retorno);
        if(retorno.equals(""))
            return null;

        return new JsonParse().getParser().fromJson(retorno, SerialKeyView.class);
    }
}
