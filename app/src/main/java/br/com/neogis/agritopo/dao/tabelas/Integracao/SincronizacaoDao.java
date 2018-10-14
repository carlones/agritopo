package br.com.neogis.agritopo.dao.tabelas.Integracao;

import java.util.List;

import br.com.neogis.agritopo.dao.tabelas.Integracao.Sincronizacao;

/**
 * Created by marci on 06/08/2018.
 */

public interface SincronizacaoDao {
    List<Sincronizacao> getAll();

    Sincronizacao get(int id);

    void save(Sincronizacao obj);

    void insert(Sincronizacao obj);

    void update(Sincronizacao obj);

    void delete(Sincronizacao obj);
}
