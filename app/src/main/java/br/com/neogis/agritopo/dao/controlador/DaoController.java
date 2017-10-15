package br.com.neogis.agritopo.dao.controlador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by carlo on 15/10/2017.
 */

public class DaoController {
    private SQLiteDatabase db;
    private BancoDeDadosSQLite banco;

    public DaoController(Context context) {
        banco = new BancoDeDadosSQLite(context);
    }

    private void abrirGravacao() {
        db = banco.getWritableDatabase();
    }

    private void abrirLeitura() {
        db = banco.getReadableDatabase();
    }

    private void fecharConexao() {
        db.close();
    }
}
