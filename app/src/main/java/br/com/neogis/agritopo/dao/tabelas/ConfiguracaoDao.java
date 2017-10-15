package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ConfiguracaoDao {
    public List<Configuracao> getAll();

    public Configuracao get(int id);

    public void insert(Configuracao obj);

    public void update(Configuracao obj);

    public void delete(Configuracao obj);
}
