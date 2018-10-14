package br.com.neogis.agritopo.dao.tabelas.Fabricas;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.ClasseDao;
import br.com.neogis.agritopo.dao.tabelas.ClasseDaoImpl;

/**
 * Created by marci on 07/08/2018.
 */

public class FabricaClasseDao {
    public static ClasseDao Criar(Context contexto){
        return new ClasseDaoImpl(
                contexto
        );
    }
}
