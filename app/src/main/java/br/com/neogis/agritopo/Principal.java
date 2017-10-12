package br.com.neogis.agritopo;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MBTilesFileArchive;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private MyLocationNewOverlay meuLocalGPS;
    private String caminhoPastaMapas = Environment.getExternalStorageDirectory().getAbsolutePath() + "/agritopo/";
    private File[] listaArquivosMapas;
    private MapView map;
    private MapController mc;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAcoes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Context ctx = getApplicationContext();
        //Configuration.getInstance().setDebugMode(true);
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        buscarMapasDoImovel();
        setContentView(R.layout.activity_principal);
        map = (MapView) findViewById(R.id.map);

        // TODO: nenhum mapa encontrado
        if( this.listaArquivosMapas.length > 0 ) {
            carregarArquivoMapa(map, this.listaArquivosMapas[0]);

            map.setBuiltInZoomControls(true);
            map.setMultiTouchControls(true);

            meuLocalGPS = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
            meuLocalGPS.enableMyLocation();
            map.getOverlays().add(meuLocalGPS);
            mc = (MapController) map.getController();
        } else {
            Toast.makeText(this, "Nenhum mapa encontrado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cadastros) {

        } else if (id == R.id.nav_mapas) {

        } else if (id == R.id.nav_ferramentas) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void buscarMapasDoImovel()
    {
        File pasta_mapas = new File(this.caminhoPastaMapas);
        FilenameFilter filtro = new FilenameFilter() {
            String[] extensoesValidas = {"mbtiles"};
            @Override
            public boolean accept(File dir, String name) {
                String extensao = name.substring(name.lastIndexOf(".") + 1);
                extensao = extensao.toLowerCase();
                Log.d("Agritopo", "extensao: " + extensao);
                return Arrays.asList(extensoesValidas).contains(extensao);
            }
        };
        this.listaArquivosMapas = pasta_mapas.listFiles(filtro);
        for(File arquivo: this.listaArquivosMapas)
            Log.d("Agritopo", "Arquivo mapa: " + arquivo.toString());
    }

    public void carregarArquivoMapa(MapView map, File arquivo)
    {
        // Offline, com arquivo específico
        // https://github.com/osmdroid/osmdroid/issues/610
        //
        Context ctx = getApplicationContext();
        Log.d("Agritopo", "abrindo arquivo de mapa " + arquivo.toString());
        Log.d("Agritopo", "File exist? : " + arquivo.exists());

        MapaLadrilho am = new MapaLadrilho(arquivo);

        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(this);
        XYTileSource mbtilesRender = new XYTileSource(
                "mbtiles",
                am.zoom_min, am.zoom_max,  // zoom min/max <- should be taken from metadata if available
                256, am.formatoImagem, new String[]{});
        IArchiveFile[] files = {MBTilesFileArchive.getDatabaseFileArchive(arquivo)};
        MapTileModuleProviderBase moduleProvider = new MapTileFileArchiveProvider(simpleReceiver, mbtilesRender, files);
        MapTileProviderArray mProvider = new MapTileProviderArray(mbtilesRender, null,
                new MapTileModuleProviderBase[]{moduleProvider}
        );
        map.setTileProvider(mProvider);
        map.setUseDataConnection(false);

        IMapController mapController = map.getController();
        mapController.setZoom(15);
        mapController.animateTo(am.pontoCentro);
    }
}
