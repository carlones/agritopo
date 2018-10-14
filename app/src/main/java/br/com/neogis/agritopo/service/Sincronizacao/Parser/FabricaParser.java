package br.com.neogis.agritopo.service.Sincronizacao.Parser;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;
import br.com.neogis.agritopo.service.Sincronizacao.ISincronizacaoParse;

/**
 * Created by marci on 07/08/2018.
 */

public class FabricaParser {
    public static ISincronizacaoParse Criar(Context contexto, Alteracao alteracao){
        switch (alteracao.getTipo()){
            case Elemento:
                return new ElementoParser(contexto, alteracao);
            case TipoElemento:
                return new TipoElementoParser(contexto, alteracao);
            default: return null;
        }
    }
}
