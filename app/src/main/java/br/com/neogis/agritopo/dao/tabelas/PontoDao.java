package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface PontoDao {
    public List<Ponto> getAll();

    public Ponto get(int id);

    public void insert(Ponto obj);

    public void update(Ponto obj);

    public void delete(Ponto obj);
}
