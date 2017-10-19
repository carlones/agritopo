package br.com.neogis.agritopo.dao.tabelas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.neogis.agritopo.dao.controlador.BancoDeDadosSQLite;

public class GeradorId {

    private SQLiteDatabase db;
    private BancoDeDadosSQLite banco;

    public GeradorId(Context context) {
        banco = new BancoDeDadosSQLite(context);
    }

    public int proximoId(String tabela) {
        db = banco.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_atual FROM geradorid WHERE tabela = ?", new String[] {tabela});

        int id = 0;
        if( cursor.moveToFirst() == true ) {
            id = cursor.getInt(0);
        }

        id++;

        ContentValues cv = new ContentValues();
        cv.put("id_atual", id);
        if( cursor.getCount() == 0 ) {
            cv.put("tabela", tabela);
            if( db.insert("geradorid", null, cv) == -1 ) {
                Log.e("Agritopo", "Erro ao inserir último id gerado");
            }
        }
        else {
            if( db.update("geradorid", cv, "tabela = ?", new String[] {tabela}) == 0 ) {
                Log.e("Agritopo", "Erro ao atualizar último id gerado");
            }
        }

        return id;
    }
}
