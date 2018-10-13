package br.com.neogis.agritopo.runnable;

import org.json.JSONObject;

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
    private SerialKeyService serialKeyService;
    private JSONObject dados;

    public SerialKeyValidate(SerialKeyService serialKeyService, JSONObject dados){
        this.serialKeyService = serialKeyService;
        this.dados = dados;
    }

    public void run() throws Exception {
        String url = ENDERECO_SERVIDOR_INTEGRACAO + "/api/SerialKey/BuscarLicenca";
        try {
            String retorno = NetworkUtils.postJSON(url, dados);
            SerialKeyView dadosLicenca = processJson(retorno);
            if (dadosLicenca == null)
                throw new Exception("Erro ao validar licença");
            else if (dadosLicenca.valida_ate.getTime() < DateUtils.getCurrentDate().getTime())
                throw new Exception("Licença expirada, entre em contato com o suporte");
            else {
                serialKeyService.saveSerialKey(dadosLicenca);
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
