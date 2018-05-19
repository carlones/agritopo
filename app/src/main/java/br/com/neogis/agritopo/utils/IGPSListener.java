package br.com.neogis.agritopo.utils;

import android.location.Location;

// O MapActivity precisa do LocationListener e do IMyLocationProvider
// para fazer funcionar o "seguir GPS" quando cadastra um elemento.
//
// Só que assim teríamos que passar cada listener como um parâmetro separado para MyGpsMyLocationProvider(),
// e implementar IMyLocationProvider são 4 métodos, então vamos criar nossa interface com apenas
// o que precisamos
//
public interface IGPSListener {
    void onLocationChanged(Location location);
    void startLocationProvider();
    void stopLocationProvider();
}
