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
    private SerialKeyView serial;

    public SerialKeyValidate(Context context, String serialKey, String email, String deviceId){
        this.context = context;
        this.serialKey = serialKey;
        this.email = email;
        this.deviceId = deviceId;
    }

    public void run() throws Exception {
        String url = NetworkUtils.getUrlLicenciamento(
                serialKey,
                email,
                deviceId);

        serial = null;
        String retorno = null;
        try {
            try {
                retorno = NetworkUtils.getJSONFromAPI(url);
            } catch(Exception ex) {
                throw new Exception(context.getString(R.string.existe_conexao_com_internet));
            }

            if (retorno.startsWith("{\"id\":")) {
                serial = processJson(retorno);
                validarPeriodoSerial(serial);
                transferirLicenca();
            } else {
                throw new Exception(context.getString(R.string.erro_ao_validar_licenca));
            }
        }
        catch(Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private void transferirLicenca() {
        new SerialKeyService(context).setChaveSerial(serial);
    }

    private SerialKeyView processJson(String retorno){
        return new JsonParse().getParser().fromJson(retorno, SerialKeyView.class);
    }

    private void validarPeriodoSerial(SerialKeyView serial) throws Exception {
        if (serial.expiration.getTime() < DateUtils.getCurrentDate().getTime())
            throw new Exception(context.getString(R.string.erro_ao_validar_licenca));
    }
}
