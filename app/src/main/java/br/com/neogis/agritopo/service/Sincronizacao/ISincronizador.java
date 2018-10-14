package br.com.neogis.agritopo.service.Sincronizacao;

import br.com.neogis.agritopo.parse.views.SincronizacaoView;

/**
 * Created by marci on 15/08/2018.
 */

public interface ISincronizador {
    boolean Sincronizar(SincronizacaoView view);
}
