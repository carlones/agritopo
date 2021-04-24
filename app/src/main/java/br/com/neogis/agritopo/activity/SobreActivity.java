package br.com.neogis.agritopo.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import br.com.neogis.agritopo.holder.AdicionarAreaHolder;
import br.com.neogis.agritopo.singleton.Licenca;
import br.com.neogis.agritopo.utils.DateUtils;
import br.com.neogis.agritopo.utils.Utils;

import static android.view.View.INVISIBLE;
import static br.com.neogis.agritopo.utils.Constantes.LICENCA_GRATUITA_LIMITE_ELEMENTOS;
import static br.com.neogis.agritopo.utils.Constantes.PEGAR_SERIAL_KEY;

public class SobreActivity extends AppCompatActivity {
    private TextView aboutContent;
    private Button atualizarLicenca;
    private String licenca = "";
    private String versao = "";
    private String idDispositivo = "";
    private String dataValidade = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        aboutContent = (TextView) findViewById(R.id.aboutContent);
        atualizarLicenca = (Button) findViewById(R.id.atualizarLicenca);

        licenca = Licenca.getInstance().getTipoAutorizado().name();
        idDispositivo = Utils.getDeviceId(getApplicationContext());

        ConstruirAjuda();

        if (licenca.equals("Gratuito")) {
            atualizarLicenca.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String titulo = "Solicitação de licença Agritopo";
                    String mensagem =
                            "<p>Suporte Neogis,</p><br/>\r\n" +
                            "<p>O cliente com  Id do dispositivo <b>" + idDispositivo + "</b> solicitou uma nova licença!</p>\r\n" +
                            "<p>Favor entrar em contato, solicitar mais informações e licenciar.</p></br>\r\n" +
                            "<p>Obrigado</p>";

                    composeEmail(new String[]{"suporte@neogis.com.br"}, titulo, mensagem);
                    Utils.toast(getApplicationContext(), "Entraremos em contato em até 1 dia útil.");
                }
            });
        } else {
            atualizarLicenca.setVisibility(INVISIBLE);
        }
    }

    private void ConstruirAjuda() {
        String version = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        dataValidade = (new SimpleDateFormat("dd/MM/yyyy")).format(Licenca.getInstance().getDataVencimentoRegistrado());
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
                                getString(R.string.device_id),
                                idDispositivo,
                                getString(R.string.data_validade),
                                dataValidade,
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

    public void composeEmail(String[] addresses, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
