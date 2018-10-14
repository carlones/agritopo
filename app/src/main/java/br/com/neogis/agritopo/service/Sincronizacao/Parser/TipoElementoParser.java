package br.com.neogis.agritopo.service.Sincronizacao.Parser;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaTipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.parse.views.ISincronizacaoView;
import br.com.neogis.agritopo.parse.views.TipoElementoView;
import br.com.neogis.agritopo.service.Sincronizacao.IntegradorParser;

/**
 * Created by marci on 11/08/2018.
 */

public class TipoElementoParser extends IntegradorParser {
    private TipoElementoDao tipoDao;

    public TipoElementoParser(Context contexto, Alteracao alteracao) {
        super(contexto, alteracao);
        tipoDao = FabricaTipoElementoDao.Criar(contexto);
    }
    @Override
    protected ISincronizacaoView getParseValue(long id) {
        TipoElemento tipo = tipoDao.get((int) id);
        if (tipo == null)
            return null;
        return obterJsonView(tipo);
    }

    private ISincronizacaoView obterJsonView(TipoElemento tipo){
        TipoElementoView view = new TipoElementoView();
        view.id = tipo.getId();
        view.nome = tipo.getNome();
        return view;
    }
}
