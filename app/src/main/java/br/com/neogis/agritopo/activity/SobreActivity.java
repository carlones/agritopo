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

import br.com.neogis.agritopo.R;
import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.service.SerialKeyService;

import static br.com.neogis.agritopo.utils.Constantes.PEGAR_SERIAL_KEY;

public class SobreActivity extends AppCompatActivity {
    private TextView aboutContent;
    private Button reativarLicenca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        aboutContent = (TextView) findViewById(R.id.aboutContent);
        reativarLicenca = (Button) findViewById(R.id.reativarLicenca);

        ConstruirAjuda();

        reativarLicenca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SerialKeyActivity.class);
                startActivityForResult(intent, PEGAR_SERIAL_KEY);
                finish();
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
        ChaveSerial chaveSerial = null;
        if (serialKeyService.containsValidSerialKey())
            chaveSerial = serialKeyService.getChaveSerial();
        else if (serialKeyService.containsFreeSerialKey())
            chaveSerial = serialKeyService.getChaveSerialTrial();

        String versao = "";
        String chave = "TRIAL";
        String data = "";
        String email = "";
        if (chaveSerial != null) {
            chave = chaveSerial.getChave();
            data = (new SimpleDateFormat("dd/MM/yyyy")).format(chaveSerial.getDataexpiracao());
        }
        if (serialKeyService.getUsuario() != null) {
            email = serialKeyService.getUsuario().getEmail();
        }
        if (version != null) {
            versao = version;
        }

        aboutContent.setText(Html.fromHtml("<h1>Agritopo</h1>\n" +
                "<p><b>Versão:</b> " + versao + "</p>\n" +
                //"<p><b>Edição:</b> Imobiliária</p>\n" +
                "<p><b>Licença:</b> " + chave + "</p>\n" +
                "<p><b>Data de expiração:</b> " + data + "</p>\n" +
                "<p><b>Licenciado para:</b> " + email + "</p>\n" +
                "<br>\n" +
                "<p><b>Suporte:</b> <a href=\"mailto:suporte@neogis.com.br\">suporte@neogis.com.br</a></p>\n" +
                "<br>\n" +
                "<p>Copyright© 2018</p>\n" +
                "<h2><a href=\"http://www.neogis.com.br\"><b>Neogis</b> Sistemas e Consultoria</a></h2>\n" +
                "<p>Todos os direitos reservados.</p>\n" +
                "<p>Chapecó - Santa Catarina - Brasil</p>\n" +
                "<p><a href=\"http://www.neogis.com.br/Home/PrivacyPolicy\">Política de Privacidade</a></p>\n" +
                "<hr>\n" +
                "<p>Este software é composto pelos seguintes componentes licenciados separadamente:</p>\n" +
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
                "<p><b><a href=\"https://github.com/osmdroid/osmdroid\">OSM Droid - org.osmdroid</a></b>: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache Licence v2</a></p>\n"
        ));
        aboutContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
