package br.com.neogis.agritopo.parse.views;

import java.util.Date;

/**
 * Created by marci on 17/04/2018.
 */

public class SerialKeyView {
    public long id;
    public String key;
    public Date expiration;
    public String deviceId;
    public UserView user;
    public LicencaTipoView licencaTipo;
}
