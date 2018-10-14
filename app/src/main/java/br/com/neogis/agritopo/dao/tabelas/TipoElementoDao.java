package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;

/**
 * Created by carlo on 15/10/2017.
 */

public interface TipoElementoDao {
    List<TipoElemento> getAll();

    TipoElemento get(int id);

    TipoElemento getByNome(String nome);

    void save(TipoElemento obj);

    void save(TipoElemento obj, Alteracao alteracao);

    void delete(TipoElemento obj);
}
