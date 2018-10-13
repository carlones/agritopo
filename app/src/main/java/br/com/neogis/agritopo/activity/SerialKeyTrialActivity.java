package br.com.neogis.agritopo.activity;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.runnable.SerialKeyValidate;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.utils.Utils;

public class SerialKeyTrialActivity extends AppCompatActivity {

    private JSONObject dadosConta;
    private JSONObject dadosDispositivo;
    private JSONObject dadosPessoa;
    private SerialKeyService serialKeyService;

    int INFORMAR_EMAIL_CONTA = 1;
    int INFORMAR_DADOS_PESSOA = 2;

    // Android segue executando código mesmo após chamar o Intent.
    // com essa flag controlamos para executar apenas 1 intent por vez
    boolean processandoIntent;

    ProgressDialog carregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_key_trial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.content_serial_key_titulo));

        serialKeyService = new SerialKeyService(this, this);
    }

    public void registrar(View v) {
        processandoIntent = false;
        registrar();
    }
    public void registrar() {
        try {
            if( preencheuTodosDados() ) {
                requisitarChaveAoServidor();
            }
            else {
                getDadosDispositivo();
                getIdentificadorDispositivo();
                getEmailConta();
                getDadosPessoa(); // executar depois de pegar o email da conta, para trazer essa informação já preenchida
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean preencheuTodosDados() {
        return
            dadosDispositivo != null
            && dadosConta != null
            && dadosPessoa != null
            && dadosDispositivo.has("identificador")
        ;
    }
    private void getDadosDispositivo() throws Exception {
        if( dadosDispositivo != null ) return;
        dadosDispositivo = serialKeyService.getDadosDispositivo();
    }

    private void getEmailConta() {
        if( processandoIntent ) return;
        if( dadosConta != null ) return;
        processandoIntent = true;
        Intent intent = AccountManager.newChooseAccountIntent(
                null,
                null,
                new String[] { "com.google" },
                true,
                "A licença do Agritopo ficará vinculada a qual conta?",
                null,
                null,
                null
        );
        startActivityForResult(intent, INFORMAR_EMAIL_CONTA);
    }

    private void getIdentificadorDispositivo() throws Exception {
        if( dadosDispositivo == null ) return;
        if( dadosDispositivo.has("identificador") ) return;
        String idDispositivo = serialKeyService.getIdDispositivo();
        if( idDispositivo == null )
            throw new Exception("Você precisa permitir a identificação do seu aparelho");
        dadosDispositivo.put("identificador", idDispositivo);
    }

    private void getDadosPessoa() {
        if( processandoIntent ) return;
        if( dadosPessoa != null ) return;
        processandoIntent = true;
        Intent intent = new Intent(getBaseContext(), ColetarDadosPessoaActivity.class);
        try {
            intent.putExtra(ColetarDadosPessoaActivity.EMAIL, dadosConta.getString("email"));
        } catch (JSONException e) {} // fazer o que?
        startActivityForResult(intent, INFORMAR_DADOS_PESSOA);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if( requestCode == INFORMAR_EMAIL_CONTA ) {
                if (resultCode != android.app.Activity.RESULT_OK) {
                    Utils.toast(this, "Você precisa informar uma conta");
                }
                else {
                    dadosConta = new JSONObject();
                    dadosConta.put("email", data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
                    processandoIntent = false;
                    registrar();
                }
            }
            else if( requestCode == INFORMAR_DADOS_PESSOA ) {
                if (resultCode != android.app.Activity.RESULT_OK) {
                    Utils.toast(this, "Você precisa informar seus dados");
                }
                else {
                    dadosPessoa = new JSONObject();
                    dadosPessoa.put("nome", data.getStringExtra(ColetarDadosPessoaActivity.NOME));
                    dadosPessoa.put("email", data.getStringExtra(ColetarDadosPessoaActivity.EMAIL));
                    dadosPessoa.put("areaAtuacao", data.getStringExtra(ColetarDadosPessoaActivity.AREA_ATUACAO));
                    dadosPessoa.put("empresa", data.getStringExtra(ColetarDadosPessoaActivity.EMPRESA));
                    int municipioId = data.getIntExtra(ColetarDadosPessoaActivity.MUNICIPIO_ID, 0);
                    if( municipioId != 0 )
                        dadosPessoa.put("municipioId", municipioId);
                    processandoIntent = false;
                    registrar();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Contate o suporte: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        processandoIntent = false;
    }

    private void requisitarChaveAoServidor() throws JSONException {
        JSONObject dados = new JSONObject();
        dados.put("conta", dadosConta);
        dados.put("dispositivo", dadosDispositivo);
        dados.put("pessoa", dadosPessoa);

        carregando = new ProgressDialog(this);
        carregando.setTitle("Enviando informações");
        carregando.setMessage("Por favor aguarde");
        carregando.setCancelable(false);
        carregando.show();

        new CarregarEmBackground(serialKeyService, dados).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void mostrarRetorno(String msg_erro) {
        carregando.dismiss();
        if(msg_erro == null) {
            Toast.makeText(this, "Licença validada com sucesso!", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        }
        else {
            Toast.makeText(this, msg_erro, Toast.LENGTH_LONG).show();
        }
    }

    private class CarregarEmBackground extends AsyncTask<Void, Void, String>
    {
        private SerialKeyService serialKeyService;
        private JSONObject dados;
        private String erro;

        CarregarEmBackground(SerialKeyService serialKeyService, JSONObject dados) {
            this.serialKeyService = serialKeyService;
            this.dados = dados;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                new SerialKeyValidate(serialKeyService, dados).run();
            } catch (Exception e) {
                erro = e.getMessage();
            }
            return erro;
        }

        @Override
        protected void onPostExecute(String result) {
            mostrarRetorno(erro);
        }
    }

}
