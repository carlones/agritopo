package br.com.neogis.agritopo.dao.tabelas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;
import br.com.neogis.agritopo.dao.tabelas.Integracao.Alteracao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.AlteracaoDao;

/**
 * Created by carlo on 15/10/2017.
 */

public class TipoElementoDaoImpl extends DaoController implements TipoElementoDao {
    private AlteracaoDao alteracaoDao;
    public TipoElementoDaoImpl(Context context, AlteracaoDao alteracaoDao) {
        super(context);
        this.alteracaoDao = alteracaoDao;
    }

    private List<TipoElemento> getListaObjetos(Cursor cursor) {
        List<TipoElemento> l = new ArrayList<>();
        while (cursor.moveToNext()) {
            TipoElemento tipo = new TipoElemento(
                    cursor.getInt(cursor.getColumnIndex("tipoelementoid")),
                    cursor.getString(cursor.getColumnIndex("nome"))
            );
            l.add(tipo);
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
        try {
            return getListaObjetos(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<TipoElemento> getAllDescricao() {
        abrirLeitura();
        Cursor cursor = db.rawQuery(
                "SELECT tipoelementoid, nome\n" +
                        "FROM tipoelemento " +
                        "ORDER BY nome", new String[]{});
        try {
            return getListaObjetos(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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
        try {
            List<TipoElemento> l = getListaObjetos(cursor);
            fecharConexao();
            if (l.isEmpty())
                return null;
            else
                return l.get(0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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
        try {
            List<TipoElemento> l = getListaObjetos(cursor);
            fecharConexao();
            if (l.isEmpty())
                return null;
            else
                return l.get(0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void save(TipoElemento obj){
        if(obj.getId() == 0) {
            insert(obj);
            alteracaoDao.insert(obj);
        }
        else {
            update(obj);
            alteracaoDao.update(obj);
        }
    }

    @Override
    public void save(TipoElemento obj, Alteracao alteracao){
        if(obj.getId() == 0)
            insert(obj);
        else
            update(obj);

        alteracao.setTipoId(obj.getId());
        alteracaoDao.save(alteracao);
    }

    @Override
    public void delete(TipoElemento obj) {
        abrirGravacao();
        if (db.delete("tipoelemento", "tipoelementoid = ?", new String[]{(Integer.toString(obj.getTipoelementoid()))}) != 1) {
            new Exception("Erro ao excluir tipoelemento");
        }
        fecharConexao();
        alteracaoDao.update(obj);
    }

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

    public void update(TipoElemento obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("nome", obj.getNome());

        if (db.update("tipoelemento", cv, "tipoelementoid = ?", new String[]{(Integer.toString(obj.getTipoelementoid()))}) != 1) {
            new Exception("Erro ao atualizar tipoelemento");
        }
        fecharConexao();
    }
}
