package br.com.neogis.agritopo.dao.tabelas.Fabricas;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDaoImpl;

/**
 * Created by marci on 07/08/2018.
 */

public class FabricaTipoElementoDao {
    public static TipoElementoDao Criar(Context contexto){
        return new TipoElementoDaoImpl(
                contexto,
                FabricaAlteracaoDao.Criar(contexto)
        );
    }
}
