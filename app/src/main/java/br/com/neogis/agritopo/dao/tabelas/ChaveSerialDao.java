package br.com.neogis.agritopo.dao.tabelas;

import java.util.List;

/**
 * Created by marci on 21/04/2018.
 */

public interface ChaveSerialDao {
    public List<ChaveSerial> getAll();

    public ChaveSerial get(int id);

    public void insert(ChaveSerial obj);

    public void update(ChaveSerial obj);

    public void delete(ChaveSerial obj);
}
