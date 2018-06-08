package br.com.neogis.agritopo.dao.controlador;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

class ExemploGeradorIdDAO {

    private BancoDeDadosSQLite banco;

    public ExemploGeradorIdDAO(Context context) {
        banco = new BancoDeDadosSQLite(context);
    }

    public int proximoId(String tabela) {
        SQLiteDatabase db = banco.getReadableDatabase();
        String[] campos = new String[]{"tabela", "id_atual"};
        String[] where = new String[]{"tabela = " + tabela};
        Cursor cursor = db.query("GeradorId", campos, "id_atual", where, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor.getInt(1) + 1;
    }
}