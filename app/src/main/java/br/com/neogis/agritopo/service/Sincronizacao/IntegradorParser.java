package br.com.neogis.agritopo.service.Sincronizacao;

import android.content.Context;

import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.Sincronizacao;
import br.com.neogis.agritopo.parse.JsonParse;
import br.com.neogis.agritopo.parse.views.ISincronizacaoView;
import br.com.neogis.agritopo.parse.views.SincronizacaoView;
import br.com.neogis.agritopo.utils.DateUtils;

/**
 * Created by marci on 07/08/2018.
 */

public abstract class IntegradorParser implements ISincronizacaoParse {
    protected Alteracao alteracao;
    protected Context contexto;

    public IntegradorParser(Context contexto, Alteracao alteracao){
        this.alteracao = alteracao;
        this.contexto = contexto;
    }

    protected abstract ISincronizacaoView getParseValue(long id);

    @Override
    public SincronizacaoView Parse() {
        SincronizacaoView view = new SincronizacaoView();
        view.tipo = alteracao.getTipo().ordinal();
        view.guid = alteracao.getGUID();
        view.dataAlteracao = DateUtils.formatDateddMMyyyyThhmmssZ(alteracao.getData());
        view.conteudo = parseView(getParseValue(alteracao.getTipoId()));
        view.excluido = view.conteudo == "";
        return view;
    }

    private String parseView(ISincronizacaoView view){
        if(view == null)
            return "";

        return new JsonParse()
                .getParser()
                .toJson(view);
    }
}
