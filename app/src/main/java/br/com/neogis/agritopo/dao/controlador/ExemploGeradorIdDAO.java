package br.com.neogis.agritopo.dao.controlador;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExemploGeradorIdDAO {

    private SQLiteDatabase db;
    private BancoDeDadosSQLite banco;

    public ExemploGeradorIdDAO(Context context) {
        banco = new BancoDeDadosSQLite(context);
    }

    public int proximoId(String tabela) {
        db = banco.getReadableDatabase();
        String[] campos = new String[]{"tabela", "id_atual"};
        String[] where = new String[]{"tabela = " + tabela};
        Cursor cursor = db.query("GeradorId", campos, "id_atual", where, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return 0;
    }
}