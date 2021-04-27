package br.com.neogis.agritopo.singleton;

import android.content.Context;
import android.os.Environment;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
            try {
                if(applicationContext != null)
                    INSTANCE.LoadLicenca(applicationContext);
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return INSTANCE;
    }

    public void LoadLicenca(Context context) throws NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, JSONException, ParseException {
        applicationContext = context.getApplicationContext();
        arquivoLicenca = Configuration.getInstance().DiretorioLeituraArquivos + "chave.ngs";
        File file = new File(arquivoLicenca);
        String fileContents = "";
        try {
            fileContents = FileUtils.readFileToString(file);
        } catch (IOException e) {
            this.dispositivoRegistrado = "";
            this.produtoRegistrado = "";
            this.dataVencimentoRegistrado = DateUtils.convertoToDateyyyyMMdd("2500-12-31");
            this.tipoRegistrado = LicencaTipo.Gratuito;
            return;
        }

        String resultado = CryptoHandler.getInstance().decrypt(fileContents);
        //"{produto: 'Agritopo-Standalone', email: 'carlos.migliavacca@gmail.com', device: 'ABC1234567', data_final: '2021-12-31' }
        JSONObject jsonObjectdecode = new JSONObject(resultado);
        this.dispositivoRegistrado = (String)jsonObjectdecode.get("device");
        this.produtoRegistrado = (String)jsonObjectdecode.get("produto");
        this.dataVencimentoRegistrado = DateUtils.convertoToDateyyyyMMdd((String)jsonObjectdecode.get("data_final"));
        this.tipoRegistrado = (
                jsonObjectdecode.get("produto").equals("Agritopo Standalone")?
                LicencaTipo.Professional:
                LicencaTipo.Gratuito
        );
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
                        && getDispositivoRegistrado().equals(Utils.getDeviceId(applicationContext)))
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

