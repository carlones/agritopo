package br.com.neogis.agritopo.dao.tabelas;

import android.content.Context;

import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ImagemDaoImpl extends DaoController implements ImagemDao {
    public ImagemDaoImpl(Context context) {
        super(context);
    }

    @Override
    public List<Imagem> getAll() {
        return null;
    }

    @Override
    public Imagem get(int id) {
        return null;
    }

    @Override
    public void insert(Imagem obj) {

    }

    @Override
    public void update(Imagem obj) {

    }

    @Override
    public void delete(Imagem obj) {

    }
}
