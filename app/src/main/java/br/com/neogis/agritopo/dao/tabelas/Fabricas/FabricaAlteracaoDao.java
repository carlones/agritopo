package br.com.neogis.agritopo.dao.tabelas.Fabricas;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.Integracao.AlteracaoDao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.AlteracaoDaoImpl;

/**
 * Created by marci on 06/08/2018.
 */

public class FabricaAlteracaoDao {
    public static AlteracaoDao Criar(Context context){
        return new AlteracaoDaoImpl(context);
    }
}
