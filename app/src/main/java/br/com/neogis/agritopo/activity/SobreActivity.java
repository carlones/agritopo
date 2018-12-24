package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.service.SerialKeyService;
import br.com.neogis.agritopo.utils.DateUtils;

import static br.com.neogis.agritopo.utils.Constantes.ARG_SERIALKEY_CHAVE;
import static br.com.neogis.agritopo.utils.Constantes.ARG_SERIALKEY_EMAIL;
import static br.com.neogis.agritopo.utils.Constantes.ARG_SERIALKEY_MANUAL;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_SERIAL_KEY;

public class SobreActivity extends AppCompatActivity {
    private TextView aboutContent;
    private Button atualizarLicenca;
    private String email = "";
    private String chave = "TRIAL";
    private String licenca = "";
    private String versao = "";
    private String data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        aboutContent = (TextView) findViewById(R.id.aboutContent);
        atualizarLicenca = (Button) findViewById(R.id.atualizarLicenca);

        ConstruirAjuda();

        atualizarLicenca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SeletorLicencaActivity.class);
                intent.putExtra(ARG_SERIALKEY_EMAIL, email);
                intent.putExtra(ARG_SERIALKEY_CHAVE, chave);
                intent.putExtra(ARG_SERIALKEY_MANUAL, 1);
                startActivityForResult(intent, PEGAR_SERIAL_KEY);
            }
        });
    }

    private void ConstruirAjuda() {
        String version = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SerialKeyService serialKeyService = new SerialKeyService(getApplicationContext());
        ChaveSerial chaveSerial = serialKeyService.getValidChaveSerial();

        if (chaveSerial != null) {
            chave = chaveSerial.getChave();
            licenca = chaveSerial.getTipo().name();
            data = (new SimpleDateFormat("dd/MM/yyyy")).format(chaveSerial.getDataexpiracao());
        }
        if (serialKeyService.getUsuario() != null) {
            email = serialKeyService.getUsuario().getEmail();
        }
        if (version != null) {
            versao = version;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.getCurrentDate());
        int ano = cal.get(Calendar.YEAR);

        aboutContent.setText(
                Html.fromHtml(
                        String.format("<h1>Agritopo</h1>\n" +
                                        "<p><b>%s:</b> %s</p>\n" +
                                        "<p><b>%s:</b> %s</p>\n" +
                                        "<p><b>%s:</b> %s</p>\n" +
                                        "<p><b>%s:</b> %s</p>\n" +
                                        "<p><b>%s:</b> %s</p>\n" +
                                        "<br>\n" +
                                        "<p><b>%s:</b> <a href=\"mailto:%s\">%s</a></p>\n<br>\n<p>Copyright© %d</p>\n" +
                                        "<h2><a href=\"http://www.neogis.com.br\"><b>Neogis</b> Sistemas e Consultoria em Tecnologia Ltda</a></h2>\n" +
                                        "<p>%s.</p>\n" +
                                        "<p>Chapecó - Santa Catarina - Brasil</p>\n" +
                                        "<p><a href=\"%s\">%s</a></p>\n" +
                                        "<p><a href=\"%s\">%s</a></p>\n" +
                                        "<hr>\n" +
                                        "<p>%s:</p>\n" +
                                        "<p><b><a href=\"https://github.com/Angads25/android-filepicker\">Android-FilePicker - com.github.angads25</a></b>: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache Licence v2</a></p>\n" +
                                        "<p><b><a href=\"https://github.com/jayschwa/AndroidSliderPreference\">AndroidSliderPreference - com.github.jayschwa</a></b>: <a href=\"https://opensource.org/licenses/MIT\">MIT License</a></p>\n" +
                                        "<p><b><a href=\"https://github.com/wtffly/DroidBinding_AndroidTreeView\">AndroidTreeView - com.github.bmelnychuk</a></b>: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache Licence v2</a></p>\n" +
                                        "<p><b><a href=\"https://commons.apache.org/\">Apache Commons - org.apache.commons</a></b>: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache Licence v2</a></p>\n" +
                                        "<p><b><a href=\"https://github.com/apache/commons-io\">Apache Commons-io - commons-io</a></b>: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache Licence v2</a></p>\n" +
                                        "<p><b><a href=\"https://github.com/danielnilsson9/color-picker-view\">Color-picker-view - com.github.danielnilsson9</a></b>: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache Licence v2</a></p>\n" +
                                        "<p><b><a href=\"https://github.com/egslava/edittext-mask\">EditText-Mask - ru.egslava</a></b>: <a href=\"https://opensource.org/licenses/MIT\">MIT License</a></p>\n" +
                                        "<p><b><a href=\"http://flexjson.sourceforge.net/\">Flex JSON - flexjson 2.1</a></b>: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache Licence v2</a></p>\n" +
                                        "<p><b><a href=\"https://github.com/Clans/FloatingActionButton\">FloatingActionButton - com.github.clans</a></b>: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache Licence v2</a></p>\n" +
                                        "<p><b><a href=\"https://github.com/google/gson\">Google Gson - com.google.code.gson</a></b>: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache Licence v2</a></p>\n" +
                                        "<p><b><a href=\"https://github.com/johnkil/Print\">Johnkil Print - com.github.johnkil.print</a></b>: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache Licence v2</a></p>\n" +
                                        "<p><b><a href=\"https://github.com/MKergall/osmbonuspack\">OSM Bonus Pack - osmbonuspack_6.4</a></b>: <a href=\"https://www.gnu.org/licenses/lgpl-3.0.en.html\">GNU Lesser v3.0</a></p>\n" +
                                        "<p><b><a href=\"https://github.com/osmdroid/osmdroid\">OSM Droid - org.osmdroid</a></b>: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache Licence v2</a></p>\n",
                                getString(R.string.versao),
                                versao,
                                getString(R.string.tipo_licenca),
                                licenca,
                                getString(R.string.chave),
                                chave,
                                getString(R.string.data_expiracao),
                                data,
                                getString(R.string.licenciado_para),
                                email,
                                getString(R.string.suporte),
                                getString(R.string.neogis_suporte_email),
                                getString(R.string.neogis_suporte_email),
                                ano,
                                getString(R.string.todos_os_direitos_reservados),
                                getString(R.string.neogis_site_politica_privacidade),
                                getString(R.string.politica_privacidade),
                                getString(R.string.site_neogis_ajuda),
                                getString(R.string.ajuda),
                                getString(R.string.este_software_e_composto_pelos_seguintes_componentes_licenciados_separadamente)
                        )
                )
        );
        aboutContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PEGAR_SERIAL_KEY) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                //intent.putExtra(ARG_IMPORTAR_NOME_ARQUIVO, edtArquivoImportar.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
