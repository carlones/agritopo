package br.com.neogis.agritopo.singleton;

import java.io.FileInputStream;
import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import br.com.neogis.agritopo.utils.CryptoHandler;
import br.com.neogis.agritopo.utils.DateUtils;
import br.com.neogis.agritopo.utils.Utils;

import static br.com.neogis.agritopo.utils.Constantes.ARQUIVO_LICENCA;
import static br.com.neogis.agritopo.utils.Constantes.PRODUTO;
import static br.com.neogis.agritopo.utils.DateUtils.getCurrentDateFromGPS;

public class Licenca {
    private static Licenca INSTANCE = null;
    private static Context applicationContext;

    private String arquivoLicenca;
    private String dispositivoRegistrado;
    private String produtoRegistrado;
    private String emailRegistrado;
    private Date dataVencimentoRegistrado;
    private LicencaTipo tipoRegistrado;

    private Licenca() {

    }

    public static Licenca getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new Licenca();
            if(applicationContext != null) {
                try {
                    INSTANCE.LoadLicenca(applicationContext);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return INSTANCE;
    }

    public void LoadLicenca(Context context) throws ParseException {
        applicationContext = context.getApplicationContext();
        if (!lerArquivoLicenca())
            if (!lerConfiguracaoLicenca())
                atribuirLicencaGratuita();
    }

    private boolean lerConfiguracaoLicenca() {
        String resultado = CryptoHandler.getInstance().decrypt(Configuration.getInstance().Licenca);
        if (resultado != null)
            return decomporResultado(resultado);
        else
            return false;
    }

    private boolean lerArquivoLicenca() {
        arquivoLicenca = Configuration.getInstance().DiretorioLeituraArquivos + ARQUIVO_LICENCA;
        byte[] encText;
        String fileContents;
        FileInputStream encryptedTextFis = null;
        try {
            encryptedTextFis = new FileInputStream(arquivoLicenca);
            encText = new byte[encryptedTextFis.available()];
            encryptedTextFis.read(encText);
            encryptedTextFis.close();
            fileContents = Base64.encodeToString(encText, Base64.DEFAULT);
            String resultado = CryptoHandler.getInstance().decrypt(fileContents);
            if (resultado != null) {
                return decomporResultado(resultado);
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private void atribuirLicencaGratuita() throws ParseException {
        this.dispositivoRegistrado = "";
        this.produtoRegistrado = PRODUTO;
        this.dataVencimentoRegistrado = DateUtils.convertoToDateyyyyMMdd("2500-12-31");
        this.tipoRegistrado = LicencaTipo.Gratuito;
    }

    private boolean decomporResultado(String resultado) {
        try {
            JSONObject jsonObjectdecode = new JSONObject(resultado);
            this.dispositivoRegistrado = (String)jsonObjectdecode.get("device");
            this.produtoRegistrado = (String)jsonObjectdecode.get("produto");
            this.dataVencimentoRegistrado = DateUtils.convertoToDateyyyyMMdd((String)jsonObjectdecode.get("data_final"));
            this.tipoRegistrado = (
                    jsonObjectdecode.get("produto").equals(PRODUTO)?
                            LicencaTipo.Professional:
                            LicencaTipo.Gratuito
            );
            return true;
        } catch (JSONException e) {
            return false;
        } catch (ParseException e) {
            return false;
        }
    }

    public String getDispositivoRegistrado() {
        return dispositivoRegistrado;
    }

    public String getProdutoRegistrado() {
        return produtoRegistrado;
    }

    public String getEmailRegistrado() {
        return emailRegistrado;
    }

    public Date getDataVencimentoRegistrado() {
        return dataVencimentoRegistrado;
    }

    public LicencaTipo getTipoAutorizado() {
        Licenca.LicencaTipo r = Licenca.LicencaTipo.Gratuito;
        if (
                DateUtils.getDaysBetween(
                        getCurrentDateFromGPS(applicationContext),
                        getDataVencimentoRegistrado()
                ) >= 0
                        && getTipoRegistrado() == Licenca.LicencaTipo.Professional
                        && getDispositivoRegistrado().equals(Configuration.getInstance().Chave))
        {
            r = Licenca.LicencaTipo.Professional;
        }
        return r;
    }
    public LicencaTipo getTipoRegistrado() {
        return tipoRegistrado;
    }

    public enum LicencaTipo {
        Gratuito, //Gratuito com limitações
        Professional //Pago
    }
}

