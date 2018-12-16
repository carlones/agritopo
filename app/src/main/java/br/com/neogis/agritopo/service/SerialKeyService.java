package br.com.neogis.agritopo.service;

import android.content.Context;

import java.util.Date;
import java.util.List;

import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.dao.tabelas.ChaveSerialDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Usuario;
import br.com.neogis.agritopo.dao.tabelas.UsuarioDaoImpl;
import br.com.neogis.agritopo.parse.views.SerialKeyView;
import br.com.neogis.agritopo.utils.Constantes;
import br.com.neogis.agritopo.utils.DateUtils;

import static br.com.neogis.agritopo.utils.DateUtils.getCurrentDate;
import static br.com.neogis.agritopo.utils.DateUtils.getCurrentDateFromGPS;

/**
 * Created by marci on 21/04/2018.
 */

public class SerialKeyService {
    private Context contexto;
    private ChaveSerialDaoImpl chaveSerialDao;
    private UsuarioDaoImpl usuarioDao;

    public SerialKeyService(Context contexto){
        this.contexto = contexto;
        chaveSerialDao = new ChaveSerialDaoImpl(contexto);
        usuarioDao = new UsuarioDaoImpl(contexto);
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
        for (ChaveSerial c : list) {
            if (DateUtils.getDaysBetween(currentDate, c.getDataexpiracao()) >= 0) {
                chaveSerial = c;
                break;
            }
        }
        return chaveSerial;
    }

    public void saveSerialKeyView(SerialKeyView serial) {
        Usuario usuario = getUsuario();
        ChaveSerial.LicencaTipo chaveLicencaTipo;
        usuario.setEmail(serial.user.email);
        ChaveSerial serialKey = chaveSerialDao.getBySerialKey(serial.key);
        if(serialKey==null)
            serialKey = new ChaveSerial();

        serialKey.setChave(serial.key);
        serialKey.setDataexpiracao(serial.expiration);
        switch (serial.licencaTipo.descricao) {
            case "Trial":
                chaveLicencaTipo = ChaveSerial.LicencaTipo.Trial;
                break;//Trial de 15 dias, com registro via web
            case "Professional":
                chaveLicencaTipo = ChaveSerial.LicencaTipo.Pago_Standalone;
                break;//Professional
            case "Project":
                chaveLicencaTipo = ChaveSerial.LicencaTipo.Pago;
                break;//Project
            case "Business":
                chaveLicencaTipo = ChaveSerial.LicencaTipo.Pago;
                break;//Business
            case "Enterprise":
                chaveLicencaTipo = ChaveSerial.LicencaTipo.Pago;
                break;//Enterprise
            default:
                chaveLicencaTipo = ChaveSerial.LicencaTipo.Gratuito; //Free
        }
        serialKey.setTipo(chaveLicencaTipo);
        serialKey.setUsuarioId(usuario.getUsuarioid());

        usuarioDao.update(usuario);
        chaveSerialDao.save(serialKey);
    }

    private ChaveSerial insertSerialTrial(){
        Usuario usuario = getUsuario();
        ChaveSerial serial = new ChaveSerial(
                0,
                Constantes.CHAVE_TESTE_TRIAL,
                DateUtils.addDays(getCurrentDate(), Constantes.CHAVE_TESTE_TRIAL_DIAS),
                usuario.getUsuarioid(),
                ChaveSerial.LicencaTipo.Trial
                );
        chaveSerialDao.insert(serial);
        return serial;
    }

    public Usuario getUsuario() {
        Usuario usuario = usuarioDao.get(1);
        if(usuario == null) {
            usuario = new Usuario(0, "", "", 0);
            new UsuarioDaoImpl(contexto).insert(usuario);
        }
        return usuario;
    }

}
