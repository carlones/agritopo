package br.com.neogis.agritopo.utils.http;

/**
 * Created by marci on 07/08/2018.
 */

public class HTTPResponse {
    public String resposta;
    public boolean sucesso;

    public HTTPResponse(String resposta, boolean sucesso){
        this.resposta = resposta;
        this.sucesso = sucesso;
    }
}
