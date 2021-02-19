package br.com.neogis.agritopo.service;

import android.content.Context;

import java.util.Date;
import java.util.List;

import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.dao.tabelas.ChaveSerialDaoImpl;
import br.com.neogis.agritopo.utils.DateUtils;

import static br.com.neogis.agritopo.utils.DateUtils.getCurrentDateFromGPS;

/**
 * Created by marci on 21/04/2018.
 */

public class SerialKeyService {
    private Context contexto;
    private ChaveSerialDaoImpl chaveSerialDao;

    public SerialKeyService(Context contexto){
        this.contexto = contexto;
        chaveSerialDao = new ChaveSerialDaoImpl(contexto);
    }

    public boolean containsValidSerialKey(ChaveSerial.LicencaTipo licencaTipo) {
        Date currentDate = getCurrentDateFromGPS(contexto);
        ChaveSerial chaveSerial = chaveSerialDao.getByTipo(licencaTipo);
        if (chaveSerial == null)
            return false;

        return DateUtils.getDaysBetween(currentDate, chaveSerial.getDataexpiracao()) >= 0;
    }

    public ChaveSerial getValidChaveSerial() {
        ChaveSerial chaveSerial = null;
        Date currentDate = getCurrentDateFromGPS(contexto);
        List<ChaveSerial> list = chaveSerialDao.getAll();
        ChaveSerial.LicencaTipo licencaTipoAnterior = ChaveSerial.LicencaTipo.Gratuito;
        for (ChaveSerial c : list) {
            if (DateUtils.getDaysBetween(currentDate, c.getDataexpiracao()) >= 0) {
                chaveSerial = c;
                break;
            }
        }
        for (ChaveSerial c : list) {
            if (DateUtils.getDaysBetween(currentDate, c.getDataexpiracao()) >= 0) {
                if (c.getTipo().ordinal() > licencaTipoAnterior.ordinal()){
                    chaveSerial = c;
                }
            }
        }
        return chaveSerial;
    }

    public boolean containsChaveSerial() {
        boolean resultado = false;
        List<ChaveSerial> list = chaveSerialDao.getAll();
        for (ChaveSerial c : list) {
            resultado = true;
            break;
        }
        return resultado;
    }

    public void setChaveSerial(String nomeProduto) {
        ChaveSerial.LicencaTipo chaveLicencaTipo;
        usuario.setEmail(serial.user.email);
        ChaveSerial serialKey = chaveSerialDao.getBySerialKey(serial.key);
        if(serialKey==null)
            serialKey = new ChaveSerial();

        serialKey.setChave(serial.key);
        serialKey.setDataexpiracao(serial.expiration);
        switch (nomeProduto) {
            case "Agritopo-Standalone":
                chaveLicencaTipo = ChaveSerial.LicencaTipo.Pago_Standalone;
                break;//Professional
            default:
                chaveLicencaTipo = ChaveSerial.LicencaTipo.Gratuito; //Free
        }
        serialKey.setTipo(chaveLicencaTipo);
        serialKey.setUsuarioId(usuario.getUsuarioid());

        usuarioDao.update(usuario);
        chaveSerialDao.save(serialKey);
    }
}
