package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.dao.tabelas.UsuarioDaoImpl;
import br.com.neogis.agritopo.parse.views.LicencaTipoView;
import br.com.neogis.agritopo.parse.views.SerialKeyView;
import br.com.neogis.agritopo.parse.views.UserView;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.utils.DateUtils;

import static br.com.neogis.agritopo.utils.Constantes.ARG_SERIALKEY_CHAVE;
import static br.com.neogis.agritopo.utils.Constantes.ARG_SERIALKEY_EMAIL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_SERIALKEY_MANUAL;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_CADASTRO_USUARIO;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_SERIAL_KEY;

public class SeletorLicencaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seletor_licenca);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean manual = (getIntent().getIntExtra(ARG_SERIALKEY_MANUAL, 0) == 1);
        String email = getIntent().getStringExtra(ARG_SERIALKEY_EMAIL);
        String chave = getIntent().getStringExtra(ARG_SERIALKEY_CHAVE);

        final SerialKeyService serialKeyService = new SerialKeyService(getBaseContext());
        ChaveSerial chaveSerial = serialKeyService.getValidChaveSerial();

        Button btnInformarChave = (Button) findViewById(R.id.btnInformarChave);
        btnInformarChave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SerialKeyActivity.class);
                startActivityForResult(intent, PEGAR_SERIAL_KEY);
            }
        });
        Button btnChaveTrial = (Button) findViewById(R.id.btnChaveTrial);
        btnChaveTrial.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegistrarUsuarioActivity.class);
                startActivityForResult(intent, PEGAR_CADASTRO_USUARIO);
            }
        });
        Button btnVersaoGratuita = (Button) findViewById(R.id.btnVersaoGratuita);
        btnVersaoGratuita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SerialKeyService serialKeyService = new SerialKeyService(getApplicationContext());
                SerialKeyView serialKeyView = new SerialKeyView();
                serialKeyView.id = 0;
                serialKeyView.licencaTipo = new LicencaTipoView();
                serialKeyView.licencaTipo.id = 1;
                serialKeyView.licencaTipo.descricao = "Gratuito";
                serialKeyView.licencaTipo.validadeDias = 999999;
                serialKeyView.key = "GRATUITO";
                serialKeyView.user = new UserView();
                serialKeyView.user.email = "contato@neogis.com.br";
                try {
                    serialKeyView.expiration = DateUtils.convertoToDateddMMyyyy("31/12/2500");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                serialKeyService.setChaveSerial(serialKeyView);
                finalizar(true);
            }
        });

        if (manual) {
            if (!(chave.equals("TRIAL") || chave.equals("GRATUITO"))) {
                Intent intent = new Intent(getBaseContext(), SerialKeyActivity.class);
                intent.putExtra(ARG_SERIALKEY_EMAIL, email);
                intent.putExtra(ARG_SERIALKEY_CHAVE, chave);
                intent.putExtra(ARG_SERIALKEY_MANUAL, 1);
                startActivityForResult(intent, PEGAR_SERIAL_KEY);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PEGAR_SERIAL_KEY:
                if (resultCode == RESULT_OK) {
                    finalizar(true);
                }
                if (resultCode == RESULT_CANCELED) {
                    //finalizar(false);
                }
                break;
            case PEGAR_CADASTRO_USUARIO:
                if (resultCode == RESULT_OK) {
                    finalizar(true);
                }
                if (resultCode == RESULT_CANCELED) {
                    //finalizar(false);
                }
                break;
        }
    }

    public void finalizar(boolean sucesso) {
        Intent intent = new Intent();
        if (sucesso)
            setResult(RESULT_OK, intent);
        else
            setResult(RESULT_CANCELED, intent);
        finish();
        try {
            onBackPressed();
        } catch (Exception e) {

        }
    }

}
