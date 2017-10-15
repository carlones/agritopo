package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface PontoImagemDao {
    public List<PontoImagem> getAll();

    public PontoImagem get(int id);

    public void insert(PontoImagem obj);

    public void update(PontoImagem obj);

    public void delete(PontoImagem obj);
}
