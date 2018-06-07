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

public class ImagemDaoImpl extends DaoController implements ImagemDao {
    public ImagemDaoImpl(Context context) {
        super(context);
    }

    private List<Imagem> getListaObjetos(Cursor cursor) {
        List<Imagem> l = new ArrayList<>();
        while (cursor.moveToNext()) {
            l.add(new Imagem(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("arquivo"))
            ));
        }
        return l;
    }

    @Override
    public List<Imagem> getAll() {
        abrirLeitura();
        Cursor cursor = db.rawQuery("SELECT * FROM imagem", new String[]{});
        List<Imagem> objetos = getListaObjetos(cursor);
        fecharConexao();
        return objetos;
    }

    @Override
    public Imagem get(int id)
    {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM imagem WHERE id = ?"
                , new String[]{Integer.toString(id)});
        List<Imagem> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public int insert(Imagem obj) {
        abrirGravacao();
        obj.setImagemid(getId("imagem"));
        ContentValues cv = new ContentValues();
        cv.put("id", obj.getImagemid());
        cv.put("arquivo", obj.getArquivo());
        if (db.insert("imagem", null, cv) == -1) {
            new Exception("Erro ao inserir imagem");
        }
        fecharConexao();
        return obj.getImagemid();
    }

    @Override
    public void update(Imagem obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("arquivo", obj.getArquivo());

        if (db.update("imagem", cv, "id = ?", new String[]{(Integer.toString(obj.getImagemid()))}) != 1) {
            new Exception("Erro ao atualizar imagem");
        }
        fecharConexao();
    }

    @Override
    public void delete(Imagem obj) {
        abrirGravacao();
        if (db.delete("imagem", "id = ?", new String[]{(Integer.toString(obj.getImagemid()))}) != 1) {
            new Exception("Erro ao excluir imagem");
        }
        fecharConexao();
    }
}
