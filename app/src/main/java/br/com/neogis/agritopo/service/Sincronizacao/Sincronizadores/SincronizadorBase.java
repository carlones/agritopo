package br.com.neogis.agritopo.service.Sincronizacao.Sincronizadores;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;

import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.AlteracaoDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Integracao.TipoAlteracao;
import br.com.neogis.agritopo.parse.views.SincronizacaoView;
import br.com.neogis.agritopo.service.Sincronizacao.ISincronizador;
import br.com.neogis.agritopo.utils.DateUtils;

/**
 * Created by marci on 15/08/2018.
 */

public abstract class SincronizadorBase implements ISincronizador {
    protected Context context;

    public SincronizadorBase(Context context){
        this.context = context;
    }

    protected abstract boolean InserirInformacao(SincronizacaoView view, Alteracao alteracao);
    protected abstract boolean AlterarInformacao(SincronizacaoView view, Alteracao alteracao);
    protected abstract boolean RemoverInformacao(long id);

    @Override
    public boolean Sincronizar(SincronizacaoView view) {
        try{
            Alteracao alteracao = obterAlteracao(view);
            if(alteracao == null){
                if (view.excluido)
                    return true;

                alteracao = criarAlteracao(view);
                return InserirInformacao(view, alteracao);
            }else
            {
                Date dataAlteracao = DateUtils.convertoToDateddMMyyyyThhmmssZ(view.dataAlteracao);
                if(dataAlteracao.getTime() > alteracao.getData().getTime())
                {
                    alteracao.setData(dataAlteracao);
                    if(view.excluido)
                        return RemoverInformacao(alteracao.getTipoId());
                    else
                        return AlterarInformacao(view, alteracao);
                }else
                    return true;
            }
        }catch (Exception e){
            Log.e("SincronizadorBase", e.getMessage());
            return false;
        }
    }

    private Alteracao obterAlteracao(SincronizacaoView view){
        return new AlteracaoDaoImpl(context).getByTipoEGUID(TipoAlteracao.values()[view.tipo], view.guid);
    }

    private Alteracao criarAlteracao(SincronizacaoView view) throws ParseException {
        Alteracao alteracao = new Alteracao();
        alteracao.setGUID(view.guid);
        alteracao.setData(DateUtils.convertoToDateddMMyyyyThhmmssZ(view.dataAlteracao));
        alteracao.setTipo(TipoAlteracao.values()[view.tipo]);
        return alteracao;
    }
}
