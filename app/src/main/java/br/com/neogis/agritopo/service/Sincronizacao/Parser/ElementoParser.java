package br.com.neogis.agritopo.service.Sincronizacao.Parser;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.Classe;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.ElementoDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaAlteracaoDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaElementoDao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.AlteracaoDao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.ISincronizavel;
import br.com.neogis.agritopo.dao.tabelas.Integracao.TipoAlteracao;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.parse.JsonParse;
import br.com.neogis.agritopo.parse.views.ElementoView;
import br.com.neogis.agritopo.parse.views.ISincronizacaoView;
import br.com.neogis.agritopo.service.Sincronizacao.IntegradorParser;

/**
 * Created by marci on 07/08/2018.
 */

public class ElementoParser extends IntegradorParser{
    private ElementoDao elementoDao;
    private AlteracaoDao alteracaoDao;

    public ElementoParser(Context contexto, Alteracao alteracao) {
        super(contexto, alteracao);
        elementoDao = FabricaElementoDao.Criar(contexto);
        alteracaoDao = FabricaAlteracaoDao.Criar(contexto);
    }

    @Override
    protected ISincronizacaoView getParseValue(long id) {
        Elemento elemento = elementoDao.get((int)id);
        if(elemento == null)
            return null;
        return obterJsonView(elemento);
    }

    private ISincronizacaoView obterJsonView(Elemento elemento){
        ElementoView view = new ElementoView();
        view.classe = elemento.getClasse().getClasseid();
        view.tipoelementoguid = getGUID(elemento.getTipoElemento());
        view.created_at = elemento.getCreated_at();
        view.descricao = elemento.getDescricao();
        view.geometria = elemento.getGeometria();
        view.modified_at = elemento.getModified_at();
        view.titulo = elemento.getTitulo();
        return  view;
    }

    private String getGUID(ISincronizavel sinc){
        Alteracao alteracao = alteracaoDao.getByTipoEId(sinc.getTipoAlteracao(), sinc.getId());
        if(alteracao == null)
            return "";
        return alteracao.getGUID();
    }
}
