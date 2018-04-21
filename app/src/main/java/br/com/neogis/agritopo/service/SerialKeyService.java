package br.com.neogis.agritopo.service;

import android.content.Context;

import java.util.Date;

import br.com.neogis.agritopo.dao.tabelas.ChaveSerial;
import br.com.neogis.agritopo.dao.tabelas.ChaveSerialDaoImpl;
import br.com.neogis.agritopo.dao.tabelas.Usuario;
import br.com.neogis.agritopo.dao.tabelas.UsuarioDaoImpl;
import br.com.neogis.agritopo.parse.views.SerialKeyView;
import br.com.neogis.agritopo.utils.DateUtils;

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

    public boolean containsValidSerialKey(){
        Date currentDate = DateUtils.getCurrentDate();
        ChaveSerial serial = chaveSerialDao.getValid(currentDate.getTime());
        if(serial == null)
            return false;

        return DateUtils.getDaysBetween(currentDate, serial.getDataexpiracao()) >= 0;
    }

    public boolean containsFreeSerialKey(){
        return getFreeTimeDays() > 0;
    }

    public long getFreeTimeDays(){
        ChaveSerial serial = chaveSerialDao.getTrial();
        if(serial == null){
            serial = insertSerialTrial();
        }
        return DateUtils.getDaysBetween(DateUtils.getCurrentDate(), serial.getDataexpiracao());
    }

    public void saveSerialKey(SerialKeyView serial){
        Usuario usuario = getUsuario();
        usuario.setEmail(serial.user.email);
        ChaveSerial serialKey = chaveSerialDao.getBySerialKey(serial.key);
        if(serialKey==null)
            serialKey = new ChaveSerial();

        serialKey.setChave(serial.key);
        serialKey.setDataexpiracao(serial.expiration);
        serialKey.setTipo(ChaveSerial.ChaveSerialTipo.Pago);
        serialKey.setUsuarioId(usuario.getUsuarioid());

        usuarioDao.update(usuario);
        chaveSerialDao.save(serialKey);
    }

    private ChaveSerial insertSerialTrial(){
        Usuario usuario = getUsuario();
        ChaveSerial serial = new ChaveSerial(
                0,
                "trial",
                DateUtils.addDays(DateUtils.getCurrentDate(), 7),
                usuario.getUsuarioid(),
                ChaveSerial.ChaveSerialTipo.Gratuito
                );
        chaveSerialDao.insert(serial);
        return serial;
    }

    private Usuario getUsuario(){
        Usuario usuario = usuarioDao.get(1);
        if(usuario == null) {
            usuario = new Usuario(0, "", "", 0);
            new UsuarioDaoImpl(contexto).insert(usuario);
        }
        return usuario;
    }

}
