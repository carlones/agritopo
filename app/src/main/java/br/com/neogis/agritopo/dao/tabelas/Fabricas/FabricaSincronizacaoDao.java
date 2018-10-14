package br.com.neogis.agritopo.dao.tabelas.Fabricas;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.Integracao.SincronizacaoDao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.SincronizacaoDaoImpl;

/**
 * Created by marci on 06/08/2018.
 */

public class FabricaSincronizacaoDao {
    public static SincronizacaoDao Criar(Context contexto){
        return  new SincronizacaoDaoImpl(contexto);
    }
}
