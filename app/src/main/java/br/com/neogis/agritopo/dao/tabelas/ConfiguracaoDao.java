package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

interface ConfiguracaoDao {
    List<Configuracao> getAll();

    Configuracao get(int id);

    void insert(Configuracao obj);

    void update(Configuracao obj);

    void delete(Configuracao obj);
}
