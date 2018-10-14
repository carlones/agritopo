package br.com.neogis.agritopo.parse.views;

import br.com.neogis.agritopo.dao.tabelas.Classe;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;

/**
 * Created by marci on 07/08/2018.
 */

public class ElementoView implements ISincronizacaoView{
    public int classe;
    public String tipoelementoguid;
    public String titulo;
    public String descricao;
    public String geometria;
    public String created_at;
    public String modified_at;
}
