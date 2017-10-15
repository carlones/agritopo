package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ImovelElementoDao {
    public List<ImovelElemento> getAll();

    public ImovelElemento get(int id);

    public void insert(ImovelElemento obj);

    public void update(ImovelElemento obj);

    public void delete(ImovelElemento obj);
}
