package br.com.neogis.agritopo.dao.controlador;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.UUID;

import br.com.neogis.agritopo.dao.tabelas.Integracao.Sincronizacao;
import br.com.neogis.agritopo.dao.tabelas.Integracao.TipoAlteracao;

/**
 * Created by carlo on 14/10/2017.
 */

public class BancoDeDadosSQLite extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "neogis_agritopo.db";
    private static final int VERSAO = 8;
    private Context contexto;

    public BancoDeDadosSQLite(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("Agritopo", "BancoDeDadosSQLite: criando banco");

        db.execSQL("\n" +
                "\n" +
                "CREATE TABLE imovel (\n" +
                " imovelid INT NOT NULL PRIMARY KEY,\n" +
                " descricao VARCHAR(200)\n" +
                ");\n" +
                "\n" +
                "\n");
        db.execSQL("CREATE TABLE mapa (\n" +
                " mapaid INT NOT NULL PRIMARY KEY,\n" +
                " descricao VARCHAR(200),\n" +
                " local VARCHAR(200),\n" +
                " sincronizado CHAR(10),\n" +
                " tipo VARCHAR(50)\n" +
                ");\n");
        db.execSQL("CREATE TABLE usuario (\n" +
                " usuarioid INT NOT NULL PRIMARY KEY,\n" +
                " email VARCHAR(300),\n" +
                " senha VARCHAR(30),\n" +
                " estado INT\n" +
                ");");

        db.execSQL("CREATE TABLE classe (\n" +
                " classeid INT NOT NULL PRIMARY KEY,\n" +
                " nome VARCHAR(150)\n" +
                ");\n");
        db.execSQL("CREATE TABLE configuracao (\n" +
                " configuracaoid VARCHAR(50) NOT NULL PRIMARY KEY,\n" +
                " nome VARCHAR(50),\n" +
                " tipo VARCHAR(50),\n" +
                " valor VARCHAR(250)\n" +
                ");\n");
        db.execSQL("CREATE TABLE geradorid (\n" +
                " tabela VARCHAR(80) NOT NULL PRIMARY KEY,\n" +
                " id_atual INT\n" +
                ");\n");
        db.execSQL("CREATE TABLE imagem (\n" +
                " id INT NOT NULL PRIMARY KEY,\n" +
                " arquivo VARCHAR(200)\n" +
                ");\n");
        db.execSQL("CREATE TABLE tipoelemento (\n" +
                " tipoelementoid INT NOT NULL PRIMARY KEY,\n" +
                " nome VARCHAR(50)\n" +
                ");\n");
        db.execSQL("CREATE TABLE campodinamico (\n" +
                " campodinamicoid INT NOT NULL,\n" +
                " tipoelementoid INT NOT NULL,\n" +
                " nome VARCHAR(200),\n" +
                " tipocomponente VARCHAR(100),\n" +
                "\n" +
                " PRIMARY KEY (campodinamicoid,tipoelementoid),\n" +
                "\n" +
                " FOREIGN KEY (tipoelementoid) REFERENCES tipoelemento (tipoelementoid)\n" +
                ");\n");
        db.execSQL("CREATE TABLE campodinamicovalores (\n" +
                " campodinamicoid INT NOT NULL,\n" +
                " tipoelementoid INT NOT NULL,\n" +
                " valorpermitido VARCHAR(200),\n" +
                "\n" +
                " PRIMARY KEY (campodinamicoid,tipoelementoid),\n" +
                "\n" +
                " FOREIGN KEY (campodinamicoid,tipoelementoid) REFERENCES campodinamico (campodinamicoid,tipoelementoid)\n" +
                ");\n");
        db.execSQL("CREATE TABLE elemento (\n" +
                " elementoid INT NOT NULL PRIMARY KEY,\n" +
                " classeid INT NOT NULL,\n" +
                " tipoelementoid INT NOT NULL,\n" +
                " titulo VARCHAR(200),\n" +
                " descricao VARCHAR(500),\n" +
                " geometria TEXT,\n" +
                " created_at TIMESTAMP,\n" +
                " modified_at TIMESTAMP,\n" +
                "\n" +
                " FOREIGN KEY (classeid) REFERENCES classe (classeid),\n" +
                " FOREIGN KEY (tipoelementoid) REFERENCES tipoelemento (tipoelementoid)\n" +
                ");\n");
        db.execSQL("CREATE TABLE elementoimagem (\n" +
                " id INT NOT NULL PRIMARY KEY,\n" +
                " elementoid INT NOT NULL,\n" +
                " imagemid INT NOT NULL\n" +
                ");\n");
        db.execSQL("CREATE TABLE elementocampodinamico (\n" +
                " campodinamicoid INT NOT NULL,\n" +
                " tipoelementoid INT NOT NULL,\n" +
                " elementoid INT NOT NULL,\n" +
                " valor VARCHAR(200),\n" +
                "\n" +
                " PRIMARY KEY (campodinamicoid,tipoelementoid,elementoid),\n" +
                "\n" +
                " FOREIGN KEY (campodinamicoid,tipoelementoid) REFERENCES campodinamico (campodinamicoid,tipoelementoid),\n" +
                " FOREIGN KEY (elementoid) REFERENCES elemento (elementoid)\n" +
                ");\n");
        db.execSQL("\n" +
                "CREATE TABLE imovelelemento (\n" +
                " imovelid INT NOT NULL,\n" +
                " elementoid INT NOT NULL,\n" +
                "\n" +
                " PRIMARY KEY (imovelid,elementoid),\n" +
                "\n" +
                " FOREIGN KEY (imovelid) REFERENCES imovel (imovelid),\n" +
                " FOREIGN KEY (elementoid) REFERENCES elemento (elementoid)\n" +
                ");\n" +
                "\n" +
                "\n");
        db.execSQL("CREATE TABLE imovelmapa (\n" +
                " mapaid INT NOT NULL,\n" +
                " imovelid INT NOT NULL,\n" +
                "\n" +
                " PRIMARY KEY (mapaid,imovelid),\n" +
                "\n" +
                " FOREIGN KEY (mapaid) REFERENCES mapa (mapaid),\n" +
                " FOREIGN KEY (imovelid) REFERENCES imovel (imovelid)\n" +
                ");\n" +
                "\n" +
                "\n");
        db.execSQL("CREATE TABLE imovelusuario (\n" +
                " usuarioid INT NOT NULL,\n" +
                " imovelid INT NOT NULL,\n" +
                " permissao INT,\n" +
                "\n" +
                " PRIMARY KEY (usuarioid,imovelid),\n" +
                "\n" +
                " FOREIGN KEY (usuarioid) REFERENCES usuario (usuarioid),\n" +
                " FOREIGN KEY (imovelid) REFERENCES imovel (imovelid)\n" +
                ");\n" +
                "\n" +
                "\n");
        db.execSQL("CREATE TABLE chaveserial (\n" +
                " id INT NOT NULL PRIMARY KEY,\n" +
                " chave VARCHAR(8) NOT NULL,\n" +
                " dataexpiracao LONG NOT NULL,\n" +
                " usuarioid int NOT NULL,\n" +
                " proprietarioid INT,\n" +
                " tipo int NOT NULL,\n" +
                "\n" +
                " FOREIGN KEY (usuarioid) REFERENCES usuario (usuarioid)\n" +
                ");" +
                "\n");

        db.execSQL("CREATE TABLE sincronizacao (\n" +
                " id INT NOT NULL PRIMARY KEY,\n" +
                " data LONG NOT NULL);" +
                "\n");

        db.execSQL("CREATE TABLE alteracao (\n" +
                " id INT NOT NULL PRIMARY KEY,\n" +
                " tipoid LONG NOT NULL, \n" +
                " data LONG NOT NULL, \n" +
                " tipo INT NOT NULL, \n" +
                " guid VARCHAR(36) NOT NULL );" +
                "\n");

        db.execSQL("CREATE UNIQUE INDEX \n" +
                " idx_alteracao_tipo ON \n" +
                " alteracao(tipo, tipoid);" +
                "\n");

        db.execSQL("CREATE UNIQUE INDEX \n" +
                " idx_alteracao_guid ON \n" +
                " alteracao(tipo, guid);" +
                "\n");

        // Android 4.0 não entende múltiplos VALUES numúnico INSERT
        db.execSQL("INSERT INTO classe (classeid, nome) VALUES (1, 'Ponto')");
        db.execSQL("INSERT INTO classe (classeid, nome) VALUES (2, 'Área')");
        db.execSQL("INSERT INTO classe (classeid, nome) VALUES (3, 'Distância')");

        db.execSQL("INSERT INTO tipoelemento (tipoelementoid, nome) VALUES (1, 'Coleta de solo')");
        db.execSQL("INSERT INTO tipoelemento (tipoelementoid, nome) VALUES (2, 'Vertente')");
        db.execSQL("INSERT INTO tipoelemento (tipoelementoid, nome) VALUES (3, 'Terreno')");
        db.execSQL("INSERT INTO tipoelemento (tipoelementoid, nome) VALUES (4, 'Açude')");
        db.execSQL("INSERT INTO tipoelemento (tipoelementoid, nome) VALUES (5, 'Distância')");

        db.execSQL("INSERT INTO geradorid (tabela, id_atual) VALUES ('classe', 3)");
        db.execSQL("INSERT INTO geradorid (tabela, id_atual) VALUES ('tipoelemento', 5)");

        db.execSQL("INSERT INTO sincronizacao (id, data) VALUES (" + Integer.toString(Sincronizacao.ID) + ", 0)");

        //Insere dados de alterações, para sincronizar com o servidor
        InserirAlteracoesDaVersao7(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Agritopo", "BancoDeDadosSQLite: atualizando da versão " + Integer.toString(oldVersion) + " para a versão " + Integer.toString(newVersion));
        if (newVersion > oldVersion) {
            if(oldVersion <= 5){
                db.execSQL("CREATE TABLE chaveserial ( \n" +
                        " id INT NOT NULL PRIMARY KEY, \n" +
                        " chave VARCHAR(8) NOT NULL, \n" +
                        " dataexpiracao LONG NOT NULL, \n" +
                        " usuarioid int NOT NULL, \n" +
                        " tipo int NOT NULL, \n" +
                        "\n" +
                        " FOREIGN KEY (usuarioid) REFERENCES usuario (usuarioid)\n" +
                        ");" +
                        "\n");
            }

            if(oldVersion <= 6){
                db.execSQL("CREATE TABLE sincronizacao (\n" +
                        " id INT NOT NULL PRIMARY KEY,\n" +
                        " data LONG NOT NULL);" +
                        "\n");

                db.execSQL("CREATE TABLE alteracao (\n" +
                        " id INT NOT NULL PRIMARY KEY,\n" +
                        " tipoid LONG NOT NULL, \n" +
                        " data LONG NOT NULL, \n" +
                        " tipo INT NOT NULL, \n" +
                        " guid VARCHAR(36) NOT NULL );" +
                        "\n");

                db.execSQL("CREATE UNIQUE INDEX \n" +
                        " idx_alteracao_tipo ON \n" +
                        " alteracao(tipo, tipoid);" +
                        "\n");

                db.execSQL("CREATE UNIQUE INDEX \n" +
                        " idx_alteracao_guid ON \n" +
                        " alteracao(tipo, guid);" +
                        "\n");

                db.execSQL("INSERT INTO sincronizacao (id, data) VALUES (" + Integer.toString(Sincronizacao.ID) + ", 0)");

                InserirAlteracoesDaVersao7(db);
            }

            if(oldVersion <= 8){
                db.execSQL("ALTER TABLE chaveserial ADD proprietarioid INT;");
                db.execSQL("UPDATE chaveserial SET proprietarioid = usuarioid;");
            }

            Log.i("Agritopo", "BancoDeDadosSQLite: versão atualizada de " + oldVersion + " para " + newVersion);
        }
    }

    private void InserirAlteracoesDaVersao7(SQLiteDatabase db){
        //TipoElemento
        db.execSQL("INSERT INTO alteracao (id, tipoid, data, tipo, guid) VALUES(?, ?, ?, ?, ?)",
                new String[]{
                        Integer.toString(1),
                        Integer.toString(1),
                        Long.toString((new Date()).getTime()),
                        Integer.toString(TipoAlteracao.TipoElemento.ordinal()),
                        "45cef9cc-fd9b-4c07-aec4-af9b1556d134"
                });
        db.execSQL("INSERT INTO alteracao (id, tipoid, data, tipo, guid) VALUES(?, ?, ?, ?, ?)",
                new String[]{
                        Integer.toString(2),
                        Integer.toString(2),
                        Long.toString((new Date()).getTime()),
                        Integer.toString(TipoAlteracao.TipoElemento.ordinal()),
                        "ee7a34c2-e39b-454d-805b-c3466df94e69"
                });
        db.execSQL("INSERT INTO alteracao (id, tipoid, data, tipo, guid) VALUES(?, ?, ?, ?, ?)",
                new String[]{
                        Integer.toString(3),
                        Integer.toString(3),
                        Long.toString((new Date()).getTime()),
                        Integer.toString(TipoAlteracao.TipoElemento.ordinal()),
                        "327b6c80-fae4-4be7-adb3-235e905bd721"
                });
        db.execSQL("INSERT INTO alteracao (id, tipoid, data, tipo, guid) VALUES(?, ?, ?, ?, ?)",
                new String[]{
                        Integer.toString(4),
                        Integer.toString(4),
                        Long.toString((new Date()).getTime()),
                        Integer.toString(TipoAlteracao.TipoElemento.ordinal()),
                        "c5b01843-b84d-4c66-aade-1489ce0ab263"
                });
        db.execSQL("INSERT INTO alteracao (id, tipoid, data, tipo, guid) VALUES(?, ?, ?, ?, ?)",
                new String[]{
                        Integer.toString(5),
                        Integer.toString(5),
                        Long.toString((new Date()).getTime()),
                        Integer.toString(TipoAlteracao.TipoElemento.ordinal()),
                        "32482808-be36-41ce-bdc1-b54f704b8b89"
                });

        int count = 6;

        //Elementos
        Cursor cursor = db.rawQuery("SELECT elementoid from elemento", new String[]{});
        while (cursor.moveToNext()) {
            db.execSQL("INSERT INTO alteracao (id, tipoid, data, tipo, guid) VALUES(?, ?, ?, ?, ?)",
                    new String[]{
                            Integer.toString(count),
                            Integer.toString(cursor.getInt(cursor.getColumnIndex("elementoid"))),
                            Long.toString((new Date()).getTime()),
                            Integer.toString(TipoAlteracao.Elemento.ordinal()),
                            UUID.randomUUID().toString()
                    });
            count += 1;
        }


        db.execSQL("INSERT INTO geradorid (tabela, id_atual) VALUES ('alteracao', " + Integer.toString(count) + " )");
    }
}
