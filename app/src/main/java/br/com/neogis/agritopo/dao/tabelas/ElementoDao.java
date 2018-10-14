package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;

/**
 * Created by carlo on 15/10/2017.
 */

public interface ElementoDao {
    List<Elemento> getAll();

    List<Elemento> getByClasse(int classeid);

    List<Elemento> getAll(String orderBy);

    Elemento get(int id);

    void save(Elemento obj);

    void save(Elemento obj, Alteracao alteracao);

    void delete(Elemento obj);
}
