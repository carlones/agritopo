package br.com.neogis.agritopo.service.Sincronizacao.Sincronizadores;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.Classe;
import br.com.neogis.agritopo.dao.tabelas.ClasseDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Elemento;
import br.com.neogis.agritopo.dao.tabelas.ElementoDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaElementoDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaTipoElementoDao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.AlteracaoDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Integracao.TipoAlteracao;
import br.com.neogis.agritopo.dao.tabelas.TipoElemento;
import br.com.neogis.agritopo.dao.tabelas.TipoElementoDaoImpl;
import br.com.neogis.agritopo.parse.JsonParse;
import br.com.neogis.agritopo.parse.views.ElementoView;
import br.com.neogis.agritopo.parse.views.SincronizacaoView;

/**
 * Created by marci on 15/08/2018.
 */

public class ElementoSincronizador extends SincronizadorBase  {
    ElementoDao elementoDao;

    public ElementoSincronizador(Context context) {
        super(context);
        elementoDao = FabricaElementoDao.Criar(context);
    }

    @Override
    protected boolean InserirInformacao(SincronizacaoView view, Alteracao alteracao) {
        ElementoView elementoView = deserializar(view.conteudo);
        Elemento elemento = criarElemento(elementoView);
        elementoDao.save(elemento, alteracao);
        return true;
    }

    @Override
    protected boolean AlterarInformacao(SincronizacaoView view, Alteracao alteracao) {
        ElementoView elementoView = deserializar(view.conteudo);
        Elemento elemento = elementoDao.get((int)alteracao.getTipoId());
        if(elemento == null )
            return InserirInformacao(view, alteracao);
        setarElemento(elementoView, elemento);
        elementoDao.save(elemento, alteracao);
        return true;
    }

    @Override
    protected boolean RemoverInformacao(long id){
        Elemento elemento = elementoDao.get((int)id);
        if(elemento == null)
            return true;

        elementoDao.delete(elemento);
        return true;
    }

    private ElementoView deserializar(String conteudo){
        return new JsonParse().getParser().fromJson(conteudo, ElementoView.class);
    }

    private Elemento criarElemento(ElementoView view){
        Elemento elemento = new Elemento(
                0,
                getTipoElemento(view),
                getClasse(view),
                view.titulo,
                view.descricao,
                view.geometria,
                view.created_at,
                view.modified_at);
        return elemento;
    }

    private void setarElemento(ElementoView view, Elemento elemento){
        elemento.setTipoElemento(getTipoElemento(view));
        elemento.setClasse(getClasse(view));
        elemento.setGeometria(view.geometria);
        elemento.setCreated_at(view.created_at);
        elemento.setDescricao(view.descricao);
        elemento.setTitulo(view.titulo);

        elemento.setModified_at(view.modified_at);
    }

    private TipoElemento getTipoElemento(ElementoView view){
        Alteracao alteracao = new AlteracaoDaoImpl(context).getByTipoEGUID(TipoAlteracao.TipoElemento, view.tipoelementoguid);
        if(alteracao == null)
            return new TipoElemento("");

        return FabricaTipoElementoDao.Criar(context).get((int) alteracao.getTipoId());
    }

    private Classe getClasse(ElementoView view){
        return new ClasseDaoImpl(context).get(view.classe);
    }
}
