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

public class TipoElementoDaoImpl extends DaoController implements TipoElementoDao {
    public TipoElementoDaoImpl(Context context) {
        super(context);
    }

    private List<TipoElemento> getListaObjetos(Cursor cursor) {
        List<TipoElemento> l = new ArrayList<>();
        while (cursor.moveToNext()) {
            l.add(new TipoElemento(
                    cursor.getInt(cursor.getColumnIndex("tipoelementoid")),
                    cursor.getString(cursor.getColumnIndex("nome"))
            ));
        }
        return l;
    }
    @Override
    public List<TipoElemento> getAll() {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT tipoelementoid, nome\n" +
                        "FROM tipoelemento " +
                        "ORDER BY nome", new String[]{});
        return getListaObjetos(cursor);
    }

    public List<TipoElemento> getAllDescricao() {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT tipoelementoid, nome\n" +
                        "FROM tipoelemento " +
                        "ORDER BY nome", new String[]{});
        return getListaObjetos(cursor);
    }

    @Override
    public TipoElemento get(int id) {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT tipoelementoid, nome " +
                        "FROM tipoelemento " +
                        "WHERE tipoelementoid = ?",
                new String[]{Integer.toString(id)}
        );
        List<TipoElemento> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public TipoElemento getByNome(String nome) {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT tipoelementoid, nome " +
                        "FROM tipoelemento " +
                        "WHERE nome = ?",
                new String[]{nome}
        );
        List<TipoElemento> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    @Override
    public void insert(TipoElemento obj) {
        abrirGravacao();
        obj.setTipoelementoid(getId("tipoelemento"));
        ContentValues cv = new ContentValues();
        cv.put("tipoelementoid", obj.getTipoelementoid());
        cv.put("nome", obj.getNome());
        if (db.insert("tipoelemento", null, cv) == -1) {
            new Exception("Erro ao inserir tipoelemento");
        }

        fecharConexao();
    }

    @Override
    public void update(TipoElemento obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("nome", obj.getNome());

        if (db.update("tipoelemento", cv, "tipoelementoid = ?", new String[]{(Integer.toString(obj.getTipoelementoid()))}) != 1) {
            new Exception("Erro ao atualizar tipoelemento");
        }
        fecharConexao();
    }

    @Override
    public void delete(TipoElemento obj) {
        abrirGravacao();
        if (db.delete("tipoelemento", "tipoelementoid = ?", new String[]{(Integer.toString(obj.getTipoelementoid()))}) != 1) {
            new Exception("Erro ao excluir tipoelemento");
        }
        fecharConexao();
    }
}
