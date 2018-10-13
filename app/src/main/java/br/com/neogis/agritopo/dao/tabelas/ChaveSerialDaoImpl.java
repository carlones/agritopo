package br.com.neogis.agritopo.dao.tabelas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.neogis.agritopo.dao.controlador.DaoController;

/**
 * Created by marci on 21/04/2018.
 */

public class ChaveSerialDaoImpl extends DaoController implements ChaveSerialDao {
    public ChaveSerialDaoImpl(Context context) {
        super(context);
    }

    private List<ChaveSerial> getListaObjetos(Cursor cursor) {
        List<ChaveSerial> l = new ArrayList<>();
        while (cursor.moveToNext()) {
            ChaveSerial serial = new ChaveSerial(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("chave")),
                    new Date(cursor.getLong(cursor.getColumnIndex("dataexpiracao"))),
                    cursor.getInt(cursor.getColumnIndex("usuarioid")),
//                    ChaveSerial.ChaveSerialTipo.values()[cursor.getInt(cursor.getColumnIndex("tipo"))]
                    cursor.getInt(cursor.getColumnIndex("tipo"))
            );
            l.add(serial);
        }
        return l;
    }


    @Override
    public List<ChaveSerial> getAll() {
        abrirLeitura();
        Cursor cursor = db.rawQuery("" +
                "SELECT * FROM chaveserial", new String[]{});
        List<ChaveSerial> objetos = getListaObjetos(cursor);
        fecharConexao();
        return objetos;
    }

    @Override
    public ChaveSerial get(int id) {
        abrirLeitura();
        String sql = "" +
                "SELECT * FROM chaveserial WHERE id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(id)});
        List<ChaveSerial> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    public ChaveSerial getTrial() {
        abrirLeitura();
        String sql = "" +
                "SELECT * FROM chaveserial WHERE tipo = ?";
//        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(ChaveSerial.ChaveSerialTipo.Gratuito.ordinal())});
        Cursor cursor = db.rawQuery(sql, new String[]{"1"});
        List<ChaveSerial> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    public ChaveSerial getValid(long currentTime) {
        abrirLeitura();
//        String sql = "" +
//                "SELECT * FROM chaveserial WHERE tipo = ? and dataexpiracao > ?";
//        Cursor cursor = db.rawQuery(sql, new String[]{
//                Integer.toString(ChaveSerial.ChaveSerialTipo.Pago.ordinal()),
//                Long.toString(currentTime)});
        String sql = "SELECT * FROM chaveserial WHERE dataexpiracao > ? LIMIT 1";
        Cursor cursor = db.rawQuery(sql, new String[]{Long.toString(currentTime)});
        List<ChaveSerial> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    public ChaveSerial getBySerialKey(String serialKey) {
        abrirLeitura();
        String sql = "" +
                "SELECT * FROM chaveserial WHERE chave = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{serialKey});
        List<ChaveSerial> l = getListaObjetos(cursor);
        fecharConexao();
        if (l.isEmpty())
            return null;
        else
            return l.get(0);
    }

    public void save(ChaveSerial obj){
        if(obj.getSerialId() == 0)
            insert(obj);
        else
            update(obj);
    }

    @Override
    public void insert(ChaveSerial obj) {
        abrirGravacao();
        obj.setSerialId(getId("chaveserial"));
        ContentValues cv = new ContentValues();
        cv.put("id", obj.getSerialId());
        cv.put("chave", obj.getChave());
        cv.put("dataexpiracao", obj.getDataexpiracao().getTime());
        cv.put("usuarioid", obj.getUsuarioId());
//        cv.put("tipo", obj.getTipo().ordinal());
        cv.put("tipo", obj.getTipo());
        if (db.insert("chaveserial", null, cv) == -1) {
            new Exception("Erro ao inserir chaveserial");
        }
        fecharConexao();
    }

    @Override
    public void update(ChaveSerial obj) {
        abrirGravacao();
        ContentValues cv = new ContentValues();
        cv.put("id", obj.getSerialId());
        cv.put("chave", obj.getChave());
        cv.put("dataexpiracao", obj.getDataexpiracao().getTime());
        cv.put("usuarioid", obj.getUsuarioId());
//        cv.put("tipo", obj.getTipo().ordinal());
        cv.put("tipo", obj.getTipo());
        if (db.update("chaveserial", cv, "id = ?", new String[]{(Integer.toString(obj.getSerialId()))}) != 1) {
            new Exception("Erro ao atualizar chaveserial");
        }
        fecharConexao();
    }

    @Override
    public void delete(ChaveSerial obj) {
        abrirGravacao();
        if (db.delete("chaveserial", "id = ?", new String[]{(Integer.toString(obj.getSerialId()))}) != 1) {
            new Exception("Erro ao excluir chaveserial");
        } else

            fecharConexao();
    }
}