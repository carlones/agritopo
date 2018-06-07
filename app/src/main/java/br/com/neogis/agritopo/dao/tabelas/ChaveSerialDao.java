package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by marci on 21/04/2018.
 */

interface ChaveSerialDao {
    List<ChaveSerial> getAll();

    ChaveSerial get(int id);

    void insert(ChaveSerial obj);

    void update(ChaveSerial obj);

    void delete(ChaveSerial obj);
}
