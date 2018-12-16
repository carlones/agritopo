package br.com.neogis.agritopo.service.Sincronizacao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaAlteracaoDao;
import br.com.neogis.agritopo.dao.tabelas.Fabricas.FabricaSincronizacaoDao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.Sincronizacao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.TipoAlteracao;
import br.com.neogis.agritopo.dao.tabelas.Usuario;
import br.com.neogis.agritopo.dao.tabelas.UsuarioDaoImpl;
import br.com.neogis.agritopo.parse.views.SincronizacaoView;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.service.Sincronizacao.Parser.FabricaParser;
import br.com.neogis.agritopo.service.Sincronizacao.Sincronizadores.FabricaSincronizador;

/**
 * Created by marci on 06/08/2018.
 */

public class IntegradorGeral extends AsyncTask<Void, Void, Void> {
    private Context contexto;
    private Completed status;
    private boolean sucess;

    public IntegradorGeral(Context contexto, Completed status){
        this.contexto = contexto;
        this.status = status;
        sucess = false;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.i("IntegradorGeral","run");

        try {
            Date inicioIntegracao = new Date();
            ChaveSerial serial = obterChaveSerial();
            if(serial == null)
                return null;
            Usuario usuario = obterUsuario(serial);
            Sincronizacao ultimaSincronizacao = obterUltimaSincronizacao();

 //           if(!ReceberAlteracoesRecentes(usuario, ultimaSincronizacao))
 //               return null;

            if(!EnviarAlteracoesPendentes(usuario, ultimaSincronizacao))
                return null;

            AtualizarUltimaSincronizacao(ultimaSincronizacao, inicioIntegracao);

            status.onProgress(100);
            sucess = true;
        }catch (Exception e){
            Log.e("IntegradorGeral", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        status.onComplete(sucess);
    }

    private boolean ReceberAlteracoesRecentes(Usuario usuario, Sincronizacao ultimaSincronizacao){
        status.onProgress(10);
        List<SincronizacaoView> alteracoes = new IntegradorRecepcao().Recepcionar(usuario.getEmail(), ultimaSincronizacao.getData());
        if(alteracoes == null)
            return false;

        if(alteracoes.size() == 0)
            return true;

        for(TipoAlteracao tipo : obterOrdem()) { //Define a ordem de envio de entidades, para enviar primeiro entidades dependentes de outras;
            for(SincronizacaoView view : alteracoes){
                if(view.tipo != tipo.ordinal())
                    continue;

                ISincronizador sincronizador = FabricaSincronizador.Criar(contexto, tipo);
                if(sincronizador == null)
                    return false;

                if (!sincronizador.Sincronizar(view))
                    return false;
            }
        }
        return true;
    }

    private boolean EnviarAlteracoesPendentes(Usuario usuario, Sincronizacao ultimaSincronizacao){
        status.onProgress(50);
        List<Alteracao> alteracoes = obterAlteracoesPendentes(ultimaSincronizacao);
        if(alteracoes.size() == 0)
            return true;
        for(TipoAlteracao tipo : obterOrdem()) { //Define a ordem de envio de entidades, para enviar primeiro entidades dependentes de outras;
            for (Alteracao alteracao : alteracoes) {
                if(alteracao.getTipo() != tipo)
                    continue;

                ISincronizacaoParse parser = FabricaParser.Criar(contexto, alteracao);
                if (parser == null)
                    return false;

                SincronizacaoView view = parser.Parse();
                view.usuarioEmail = usuario.getEmail();
                if (!new IntegradorTransmissao().Transmitir(view))
                    return false;
            }
        }
        return true;
    }

    private Sincronizacao obterUltimaSincronizacao(){
        Sincronizacao sincronizacao = FabricaSincronizacaoDao.Criar(contexto).get(Sincronizacao.ID);
        if(sincronizacao == null){
            sincronizacao = new Sincronizacao();
            sincronizacao.setId(Sincronizacao.ID);
            sincronizacao.setData(new Date(0));
        }
        return sincronizacao;
    }

    private List<Alteracao> obterAlteracoesPendentes(Sincronizacao ultimaSincronizacao){
        return FabricaAlteracaoDao.Criar(contexto)
                .getByData(ultimaSincronizacao.getData());
    }

    private List<TipoAlteracao> obterOrdem(){
        List<TipoAlteracao> tipos = new ArrayList<TipoAlteracao>();
        tipos.add(TipoAlteracao.TipoElemento);
        tipos.add(TipoAlteracao.Elemento);
        return tipos;
    }

    private ChaveSerial obterChaveSerial(){
        SerialKeyService service = new SerialKeyService(contexto);
        return service.getValidChaveSerial();
    }

    private Usuario obterUsuario(ChaveSerial serial){
        return new UsuarioDaoImpl(contexto).get(serial.getUsuarioId());
    }

    private void AtualizarUltimaSincronizacao(Sincronizacao sincronizacao, Date data){
        status.onProgress(90);
        sincronizacao.setData(data);
        FabricaSincronizacaoDao.Criar(contexto).save(sincronizacao);
    }

    public interface Completed {
        void onComplete(boolean sucess);
        void onProgress(int progress);
    }
}
