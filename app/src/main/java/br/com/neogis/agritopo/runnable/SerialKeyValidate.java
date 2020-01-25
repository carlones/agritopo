package br.com.neogis.agritopo.runnable;

import android.content.Context;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.parse.JsonParse;
import br.com.neogis.agritopo.parse.views.SerialKeyView;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.utils.DateUtils;
import br.com.neogis.agritopo.utils.NetworkUtils;
import br.com.neogis.agritopo.utils.RetornoErroServidorException;

import static br.com.neogis.agritopo.utils.Constantes.ENDERECO_SERVIDOR_LICENCIAMENTO;

/**
 * Created by marci on 14/04/2018.
 */

public class SerialKeyValidate {
    private String serialKey;
    private String email;
    private String deviceId;
    private Context context;

    public SerialKeyValidate(Context context, String serialKey, String email, String deviceId){
        this.context = context;
        this.serialKey = serialKey;
        this.email = email;
        this.deviceId = deviceId;
    }

    public void run() throws Exception {
        String url = ENDERECO_SERVIDOR_LICENCIAMENTO +
                "/api/SerialKey/ProcessSerialKey?" +
                "serialKey=" + serialKey.replace("-", "") +
                "&deviceId=" + deviceId +
                "&email=" + email;
        try {
            String retorno = NetworkUtils.getJSONFromAPI(url);
            SerialKeyView serial = processJson(retorno);
            if (serial == null)
                throw new Exception(context.getString(R.string.erro_ao_validar_licenca));
            else if (serial.expiration.getTime() < DateUtils.getCurrentDate().getTime())
                throw new Exception(context.getString(R.string.erro_ao_validar_licenca));
            else {
                new SerialKeyService(context).setChaveSerial(serial);
            }
        }
        catch(RetornoErroServidorException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw new Exception( context.getString(R.string.existe_conexao_com_internet));
        }
    }

    private SerialKeyView processJson(String retorno){
        if(retorno.equals(""))
            return null;

        return new JsonParse().getParser().fromJson(retorno, SerialKeyView.class);
    }
}
