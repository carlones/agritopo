package br.com.neogis.agritopo.runnable;

import android.app.Activity;
import android.content.Context;

import br.com.neogis.agritopo.parse.JsonParse;
import br.com.neogis.agritopo.parse.views.SerialKeyView;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.utils.DateUtils;
import br.com.neogis.agritopo.utils.NetworkUtils;
import br.com.neogis.agritopo.utils.RetornoErroServidorException;

import static br.com.neogis.agritopo.utils.Constantes.ENDERECO_SERVIDOR_INTEGRACAO;

/**
 * Created by marci on 14/04/2018.
 */

public class SerialKeyValidate {
    private String serialKey;
    private String email;
    private String deviceId;
    private Context context;
    private Activity activity;

    public SerialKeyValidate(Context context, String serialKey, String email, String deviceId, Activity activity){
        this.context = context;
        this.activity = activity;
        this.serialKey = serialKey;
        this.email = email;
        this.deviceId = deviceId;
    }

    public void run() throws Exception {
        String url = ENDERECO_SERVIDOR_INTEGRACAO +
                "/api/SerialKey/ProcessSerialKey?" +
                "serialKey=" + serialKey.replace("-", "") +
                "&deviceId=" + deviceId +
                "&email=" + email;
        try {
            String retorno = NetworkUtils.getJSONFromAPI(url);
            SerialKeyView serial = processJson(retorno);
            if (serial == null)
                throw new Exception("Erro ao validar licença");
            else if (serial.expiration.getTime() < DateUtils.getCurrentDate().getTime())
                throw new Exception("Erro ao validar licença");
            else {
                new SerialKeyService(context, activity).saveSerialKey(serial);
            }
        }
        catch(RetornoErroServidorException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw new Exception("Erro ao contatar o site. Existe conexão com a Internet?");
        }
    }

    private SerialKeyView processJson(String retorno){
        if(retorno.equals(""))
            return null;

        return new JsonParse().getParser().fromJson(retorno, SerialKeyView.class);
    }
}
