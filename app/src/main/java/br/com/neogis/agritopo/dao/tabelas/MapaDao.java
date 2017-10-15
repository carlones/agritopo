package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface MapaDao {
    public List<Mapa> getAll();

    public Mapa get(int id);

    public void insert(Mapa obj);

    public void update(Mapa obj);

    public void delete(Mapa obj);
}
