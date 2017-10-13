package br.com.neogis.agritopo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MBTilesFileArchive;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener, MapEventsReceiver, MapView.OnFirstLayoutListener {

    ItemizedOverlayWithFocus<OverlayItem> listaPontosOverlay;
    ModalAdicionarPonto modalAdicionarPonto;
    GeoPoint pontoAeroporto = new GeoPoint(-27.1341, -52.6606);
    //--- Stuff for setting the mapview on a box at startup:
    BoundingBox mInitialBoundingBox = null;
    float mGroundOverlayBearing = 0.0f;
    private MyLocationNewOverlay meuLocalGPS;
    private String caminhoPastaMapas = Environment.getExternalStorageDirectory().getAbsolutePath() + "/agritopo/";
    private File[] listaArquivosMapas;
    private MapView map;
    private MapController mc;
    private FloatingActionMenu famActions;
    private Context mContext;
    private Activity mActivity;
    private FloatingActionButton fabNovoPonto, fabNovaArea, fabCamadas, fabGPS;
    //GeoPoint pontoDesbravador = new GeoPoint(-27.1048003, -52.6145871);
    private PopupWindow mPopupWindow;
    private ConstraintLayout telaPrincipal;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        telaPrincipal = (ConstraintLayout) findViewById(R.id.content_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mContext = getApplicationContext();
        mActivity = Principal.this;

        inicializarMapas();
        inicializarBotoes();
    }

    public void inicializarBotoes() {
        famActions = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        fabNovoPonto = (FloatingActionButton) findViewById(R.id.action_novo_ponto);
        fabNovaArea = (FloatingActionButton) findViewById(R.id.action_nova_area);
        fabGPS = (FloatingActionButton) findViewById(R.id.action_gps);
        fabCamadas = (FloatingActionButton) findViewById(R.id.action_layer);

        fabNovoPonto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<Overlay> overlays = map.getOverlays();
                if (modalAdicionarPonto != null) {
                    overlays.remove(modalAdicionarPonto);
                    modalAdicionarPonto = null;
                } else {
                    modalAdicionarPonto = new ModalAdicionarPonto(map, listaPontosOverlay);
                    overlays.add(modalAdicionarPonto);
                }
                map.invalidate();
            }
        });
        fabNovaArea.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked

            }
        });
        fabGPS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleGPS();
            }
        });
        fabCamadas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked

                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.layer_popup_principal, null);
                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                if (Build.VERSION.SDK_INT >= 21) {
                    mPopupWindow.setElevation(5.0f);
                }
                // Get a reference for the custom view close button
                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.layer_popup_close_btn);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });
                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
                mPopupWindow.showAtLocation(telaPrincipal, Gravity.CENTER, 0, 0);
            }
        });
    }

    public void inicializarMapas() {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        buscarMapasDoImovel();
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

            criarListaPontos();
            alternarExibicaoPontosInteresse();
        } else {
            Toast.makeText(this, "Nenhum mapa encontrado", Toast.LENGTH_LONG).show();
        }
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        map.getOverlays().add(0, mapEventsOverlay);
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
        carregarMapasNoMenu(menu);
        return true;
    }

    public boolean carregarMapasNoMenu(Menu menu) {
        if (listaArquivosMapas != null) {
            for (int i = 0; i < listaArquivosMapas.length; i++) {
                String nomeMenu = listaArquivosMapas[i].getName();

                // remover extensão
                int pos_ponto_extensao = nomeMenu.lastIndexOf(".");
                if (pos_ponto_extensao > 0)
                    nomeMenu = nomeMenu.substring(0, pos_ponto_extensao);

                menu.add(0, i, 0, nomeMenu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //}

        int id = item.getItemId();

        if (listaArquivosMapas != null) {
            Log.d("Agritopo", "Mapa selecionado: " + listaArquivosMapas[id].toString());
            carregarArquivoMapa(this.map, listaArquivosMapas[id]);
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

        } else if (id == R.id.nav_exportacao) {

        } else if (id == R.id.nav_configuracao) {
            Intent intent = new Intent(this, ConfiguracaoActivity.class);
            //EditText editText = (EditText) findViewById(R.id.edit_message);
            //String message = editText.getText().toString();
            //intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
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

    void setInitialViewOn(BoundingBox bb) {
        if (map.getScreenRect(null).height() == 0) {
            mInitialBoundingBox = bb;
            map.addOnFirstLayoutListener(this);
        } else
            map.zoomToBoundingBox(bb, false);
    }

    @Override
    public void onFirstLayout(View v, int left, int top, int right, int bottom) {
        if (mInitialBoundingBox != null)
            map.zoomToBoundingBox(mInitialBoundingBox, false);
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        Toast.makeText(this, "Ponto indicado em (" + p.getLatitude() + "," + p.getLongitude() + ")", Toast.LENGTH_SHORT).show();
        InfoWindow.closeAllInfoWindowsOn(map);
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        //Toast.makeText(this, "Long press", Toast.LENGTH_SHORT).show();
        //17. Using Polygon, defined as a circle:
        Polygon circle = new Polygon();
        circle.setPoints(Polygon.pointsAsCircle(p, 2000.0));
        circle.setFillColor(0x12121212);
        circle.setStrokeColor(Color.RED);
        circle.setStrokeWidth(2);
        map.getOverlays().add(circle);
        circle.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
        circle.setTitle("Centralizado em " + p.getLatitude() + "," + p.getLongitude());

        //18. Using GroundOverlay
        GroundOverlay myGroundOverlay = new GroundOverlay();
        myGroundOverlay.setPosition(p);
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_place_black_24dp, null);
        myGroundOverlay.setImage(d.mutate());
        myGroundOverlay.setDimensions(200.0f);
        //myGroundOverlay.setTransparency(0.25f);
        myGroundOverlay.setBearing(mGroundOverlayBearing);
        mGroundOverlayBearing += 20.0f;
        map.getOverlays().add(myGroundOverlay);

        map.invalidate();
        return true;
    }

    void criarListaPontos() {
        // https://stackoverflow.com/questions/41090639/add-marker-to-osmdroid-5-5-map

        //your items
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(new OverlayItem("Aeroporto de Chapecó", "Descrição", pontoAeroporto));
        //items.add(new OverlayItem("Desbravador", "Descrição", pontoDesbravador));

        //the overlay
        listaPontosOverlay = new ItemizedOverlayWithFocus<OverlayItem>(
                this, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }
        );
        listaPontosOverlay.setFocusItemsOnTap(true);
    }

    void alternarExibicaoPontosInteresse() {
        List<Overlay> overlays = this.map.getOverlays();
        if (overlays.contains(listaPontosOverlay))
            overlays.remove(listaPontosOverlay);
        else
            overlays.add(listaPontosOverlay);
        this.map.invalidate();
    }

    void toggleGPS() {
        Log.d("Agritopo", "Alternando seguir GPS");

        if (meuLocalGPS.isFollowLocationEnabled()) {
            meuLocalGPS.disableFollowLocation();
            this.avisar("GPS desacoplado");
        } else {
            meuLocalGPS.enableFollowLocation();
            this.avisar("Seguindo GPS");
        }
    }

    void avisar(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    //0. Using the Marker and Polyline overlays - advanced options
    class OnMarkerDragListenerDrawer implements Marker.OnMarkerDragListener {
        ArrayList<GeoPoint> mTrace;
        Polyline mPolyline;

        OnMarkerDragListenerDrawer() {
            mTrace = new ArrayList<GeoPoint>(100);
            mPolyline = new Polyline();
            mPolyline.setColor(0xAA0000FF);
            mPolyline.setWidth(2.0f);
            mPolyline.setGeodesic(true);
            map.getOverlays().add(mPolyline);
        }

        @Override
        public void onMarkerDrag(Marker marker) {
            //mTrace.add(marker.getPosition());
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            mTrace.add(marker.getPosition());
            mPolyline.setPoints(mTrace);
            map.invalidate();
        }

        @Override
        public void onMarkerDragStart(Marker marker) {
            //mTrace.add(marker.getPosition());
        }
    }

    //7. Customizing the bubble behaviour
    class CustomInfoWindow extends MarkerInfoWindow {
        POI mSelectedPoi;

        public CustomInfoWindow(MapView mapView) {
            super(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, mapView);
            Button btn = (Button) (mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo));
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (mSelectedPoi.mUrl != null) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSelectedPoi.mUrl));
                        view.getContext().startActivity(myIntent);
                    } else {
                        Toast.makeText(view.getContext(), "Button clicked", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        @Override
        public void onOpen(Object item) {
            super.onOpen(item);
            mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
            Marker marker = (Marker) item;
            mSelectedPoi = (POI) marker.getRelatedObject();

            //8. put thumbnail image in bubble, fetching the thumbnail in background:
            if (mSelectedPoi.mThumbnailPath != null) {
                ImageView imageView = (ImageView) mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_image);
                mSelectedPoi.fetchThumbnailOnThread(imageView);
            }
        }
    }
}
