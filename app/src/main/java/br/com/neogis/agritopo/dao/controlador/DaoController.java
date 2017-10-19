package br.com.neogis.agritopo.dao.controlador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by carlo on 15/10/2017.
 */

public class DaoController {
    protected SQLiteDatabase db;
    private BancoDeDadosSQLite banco;

    public DaoController(Context context) {
        banco = new BancoDeDadosSQLite(context);
    }

    public void abrirGravacao() {
        db = banco.getWritableDatabase();
    }

    public void abrirLeitura() {
        db = banco.getReadableDatabase();
    }

    public void fecharConexao() {
        db.close();
    }
}
