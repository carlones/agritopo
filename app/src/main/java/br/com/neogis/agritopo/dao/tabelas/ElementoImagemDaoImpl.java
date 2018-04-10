package br.com.neogis.agritopo.dao.tabelas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by carlo on 15/10/2017.
 */

public class ElementoImagemDaoImpl extends DaoController implements ElementoImagemDao {
    public ElementoImagemDaoImpl(Context context) {
        super(context);
    }

    private List<ElementoImagem> getListaObjetos(Cursor cursor) {
        List<ElementoImagem> l = new ArrayList<>();
        ImagemDao imagemDao = new ImagemDaoImpl(context);
        while (cursor.moveToNext()) {
            l.add(new ElementoImagem(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("elementoid")),
                    imagemDao.get(cursor.getInt(cursor.getColumnIndex("imagemid")))
            ));
        }
        return l;
    }

    @Override
    public List<ElementoImagem> getAll() {
        abrirLeitura();
        Cursor cursor = db.rawQuery("SELECT * FROM elementoimagem", new String[]{});
        List<ElementoImagem> objetos = getListaObjetos(cursor);
        fecharConexao();
        return objetos;
    }

    public List<ElementoImagem> getByElemento(int id){
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM elementoimagem WHERE elementoid = ?"
                , new String[]{Integer.toString(id)});
        List<ElementoImagem> l = getListaObjetos(cursor);
        fecharConexao();
        return l;
    }

    @Override
    public ElementoImagem get(int id) {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM elementoimagem WHERE id = ?"
                , new String[]{Integer.toString(id)});
        List<ElementoImagem> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public void insert(ElementoImagem obj) {
        abrirGravacao();
        obj.setId(getId("elementoimagem"));
        ContentValues cv = new ContentValues();
        cv.put("id", obj.getId());
        cv.put("elementoid", obj.getIdElemento());
        cv.put("imagemid", obj.getImagem().getImagemid());
        if (db.insert("elementoimagem", null, cv) == -1) {
            new Exception("Erro ao inserir ElementoImagem");
        }
        fecharConexao();
    }

    @Override
    public void update(ElementoImagem obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("elementoid", obj.getIdElemento());
        cv.put("imagemid", obj.getImagem().getImagemid());

        if (db.update("elementoimagem", cv, "id = ?", new String[]{(Integer.toString(obj.getId()))}) != 1) {
            new Exception("Erro ao atualizar ElementoImagem");
        }
        fecharConexao();
    }

    @Override
    public void delete(ElementoImagem obj) {
        abrirGravacao();
        if (db.delete("elementoimagem", "id = ?", new String[]{(Integer.toString(obj.getId()))}) != 1) {
            new Exception("Erro ao excluir ElementoImagem");
        }
        fecharConexao();
    }
}
