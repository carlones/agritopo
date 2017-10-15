package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ImovelDao {
    public List<Imovel> getAll();

    public Imovel get(int id);

    public void insert(Imovel obj);

    public void update(Imovel obj);

    public void delete(Imovel obj);
}
