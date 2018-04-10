package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ImagemDao {
    public List<Imagem> getAll();

    public Imagem get(int id);

    public int insert(Imagem obj);

    public void update(Imagem obj);

    public void delete(Imagem obj);
}
