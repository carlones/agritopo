package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface GeradorIdDao {
    public List<GeradorId> getAll();

    public GeradorId get(int id);

    public void insert(GeradorId obj);

    public void update(GeradorId obj);

    public void delete(GeradorId obj);
}
