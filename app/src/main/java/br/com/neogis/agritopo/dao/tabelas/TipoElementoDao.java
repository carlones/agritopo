package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface TipoElementoDao {
    public List<TipoElemento> getAll();

    public TipoElemento get(int id);

    public void insert(TipoElemento obj);

    public void update(TipoElemento obj);

    public void delete(TipoElemento obj);
}
