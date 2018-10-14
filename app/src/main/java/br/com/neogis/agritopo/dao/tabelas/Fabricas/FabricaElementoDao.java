package br.com.neogis.agritopo.dao.tabelas.Fabricas;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Integracao.AlteracaoDaoImpl;

/**
 * Created by marci on 06/08/2018.
 */

public class FabricaElementoDao {
    public static ElementoDao Criar(Context contexto){
        return new ElementoDaoImpl(
                contexto,
                FabricaAlteracaoDao.Criar(contexto));
    }
}
