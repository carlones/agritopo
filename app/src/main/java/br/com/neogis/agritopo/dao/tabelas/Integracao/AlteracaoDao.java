package br.com.neogis.agritopo.dao.tabelas.Integracao;

import java.util.Date;
import java.util.List;

/**
 * Created by marci on 06/08/2018.
 */

public interface AlteracaoDao {
    List<Alteracao> getAll();

    List<Alteracao> getByData(Date data);

    Alteracao get(long id);

    Alteracao getByTipoEId(TipoAlteracao tipo, long id);

    Alteracao getByTipoEGUID(TipoAlteracao tipo, String guid);

    void save(Alteracao obj);

    void delete(Alteracao obj);

    void insert(ISincronizavel sinc);

    void update(ISincronizavel sinc);
}
