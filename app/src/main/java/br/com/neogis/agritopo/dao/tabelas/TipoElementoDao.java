package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface TipoElementoDao {
    List<TipoElemento> getAll();

    TipoElemento get(int id);

    TipoElemento getByNome(String nome);

    void insert(TipoElemento obj);

    void update(TipoElemento obj);

    void delete(TipoElemento obj);
}
