package br.com.neogis.agritopo.service.Sincronizacao.Sincronizadores;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.Integracao.TipoAlteracao;
import br.com.neogis.agritopo.service.Sincronizacao.ISincronizador;

/**
 * Created by marci on 15/08/2018.
 */

public class FabricaSincronizador {
    public static ISincronizador Criar(Context context, TipoAlteracao tipo){
        switch (tipo){
            case Elemento:
                return new ElementoSincronizador(context);
            case TipoElemento:
                return new TipoElementoSincronizador(context);
            default: return null;
        }
    }
}
