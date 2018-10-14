package br.com.neogis.agritopo.service.Sincronizacao.Sincronizadores;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaTipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDao;
import br.com.neogis.agritopo.parse.JsonParse;
import br.com.neogis.agritopo.parse.views.SincronizacaoView;
import br.com.neogis.agritopo.parse.views.TipoElementoView;

/**
 * Created by marci on 15/08/2018.
 */

public class TipoElementoSincronizador extends SincronizadorBase {
    TipoElementoDao tipoElementoDao;

    public TipoElementoSincronizador(Context context) {
        super(context);
        tipoElementoDao = FabricaTipoElementoDao.Criar(context);
    }

    @Override
    protected boolean InserirInformacao(SincronizacaoView view, Alteracao alteracao) {
        TipoElementoView tipoView = deserializar(view.conteudo);
        TipoElemento tipo = new TipoElemento(
                tipoView.nome
        );
        tipoElementoDao.save(tipo, alteracao);
        return true;
    }

    @Override
    protected boolean AlterarInformacao(SincronizacaoView view, Alteracao alteracao) {
        TipoElementoView tipoView = deserializar(view.conteudo);
        TipoElemento tipo = tipoElementoDao.get((int)alteracao.getTipoId());
        if(tipo == null )
            return InserirInformacao(view, alteracao);
        tipo.setNome(tipoView.nome);

        return true;
    }

    @Override
    protected boolean RemoverInformacao(long id){
        TipoElemento tipo = tipoElementoDao.get((int)id);
        if(tipo == null)
            return true;

        tipoElementoDao.delete(tipo);
        return true;
    }

    private TipoElementoView deserializar(String conteudo){
        return new JsonParse().getParser().fromJson(conteudo, TipoElementoView.class);
    }
}
