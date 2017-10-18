package br.com.neogis.agritopo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MBTilesFileArchive;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.View.VISIBLE;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener, MapEventsReceiver, MapView.OnFirstLayoutListener {

    //Mapas
    BoundingBox mInitialBoundingBox = null;
    float mGroundOverlayBearing = 0.0f;
    ModalAdicionarArea modalAdicionarArea;
    ModalAdicionarPonto modalAdicionarPonto;
    ItemizedOverlayWithFocus<OverlayItem> geoPointList;
    List<Area> areaList;
    private MyLocationNewOverlay mMyLocationNewOverlay;
    private CompassOverlay mCompassOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;
    private MapView map;
    //Outros
    private String caminhoPastaMapas = Environment.getExternalStorageDirectory().getAbsolutePath() + "/agritopo/";
    private File[] listaArquivosMapas;
    //Interface
    private FloatingActionMenu famNovo;
    private Context mContext;
    private Activity mActivity;
    private FloatingActionButton fabNovoPonto, fabNovaArea, fabCamadas, fabGPS, fabRotacao, fabConcluido, fabCancelar;
    private PopupWindow popupLayers;
    private ConstraintLayout layoutTelaPrincipal;
    private boolean exibirAreas;
    private IMapController mapController;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        layoutTelaPrincipal = (ConstraintLayout) findViewById(R.id.content_principal);
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
        mActivity = PrincipalActivity.this;

        inicializarMapas();
        inicializarBotoes();
    }

    public void inicializarBotoes() {
        famNovo = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        fabNovoPonto = (FloatingActionButton) findViewById(R.id.action_novo_ponto);
        fabNovaArea = (FloatingActionButton) findViewById(R.id.action_nova_area);
        fabGPS = (FloatingActionButton) findViewById(R.id.action_gps);
        fabCamadas = (FloatingActionButton) findViewById(R.id.action_layer);
        fabRotacao = (FloatingActionButton) findViewById(R.id.action_rotacao);
        fabConcluido = (FloatingActionButton) findViewById(R.id.action_concluido);
        fabCancelar = (FloatingActionButton) findViewById(R.id.action_cancelar);

        fabNovoPonto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (modalAdicionarPonto == null) {
                    modalAdicionarPonto = new ModalAdicionarPonto(map, geoPointList);
                    map.getOverlays().add(modalAdicionarPonto);
                    famNovo.close(false);
                    famNovo.setVisibility(View.INVISIBLE);
                    fabConcluido.setVisibility(View.VISIBLE);
                }
                map.invalidate();
            }
        });
        fabNovaArea.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (modalAdicionarArea == null) {
                    modalAdicionarArea = new ModalAdicionarArea(map);
                    famNovo.close(false);
                    famNovo.setVisibility(View.INVISIBLE);
                    fabConcluido.setVisibility(View.VISIBLE);
                    fabCancelar.setVisibility(View.VISIBLE);
                }
            }
        });
        fabGPS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mMyLocationNewOverlay.isFollowLocationEnabled()) {
                    mMyLocationNewOverlay.disableFollowLocation();
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_gps_not_fixed_white_24dp);
                } else {
                    mMyLocationNewOverlay.enableFollowLocation();
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_gps_fixed_white_24dp);
                    if (mMyLocationNewOverlay.getMyLocation() != null)
                        (map.getController()).setCenter(mMyLocationNewOverlay.getMyLocation());
                }
            }
        });
        fabCamadas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layoutTelaPrincipal/view
                View customView = inflater.inflate(R.layout.popup_layer, null);
                popupLayers = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                if (Build.VERSION.SDK_INT >= 21) {
                    popupLayers.setElevation(5.0f);
                }
                // Get a reference for the custom view close button
                ImageButton btnPopupLayerFechar = (ImageButton) customView.findViewById(R.id.btnPopupLayerFechar);
                ImageButton btnPopupLayerFecharTop = (ImageButton) customView.findViewById(R.id.btnPopupLayerFecharTop);
                ImageButton btnPopupLayerFecharBottom = (ImageButton) customView.findViewById(R.id.btnPopupLayerFecharBottom);
                CheckBox cbxPontosDeInteresse = (CheckBox) customView.findViewById(R.id.cbxPontosDeInteresse);
                CheckBox cbxAreas = (CheckBox) customView.findViewById(R.id.cbxGeometrias);
                CheckBox cbxCurvasDeNivel = (CheckBox) customView.findViewById(R.id.cbxCurvasDeNivel);

                cbxPontosDeInteresse.setChecked(map.getOverlays().contains(geoPointList));
                cbxAreas.setChecked(exibirAreas);

                cbxPontosDeInteresse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            map.getOverlays().add(geoPointList);
                            Utils.toast(getBaseContext(), "Pontos de Interesse: Ativado");
                        } else {
                            map.getOverlays().remove(geoPointList);
                            Utils.toast(getBaseContext(), "Pontos de Interesse: Desativado");
                        }
                        map.invalidate();
                    }
                });
                cbxAreas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        exibirAreas = isChecked;
                        List<Overlay> overlays = map.getOverlays();
                        if (areaList != null) {
                            for (Area area : areaList)
                                if (isChecked)
                                    overlays.add(area.poligono);
                                else
                                    overlays.remove(area.poligono);
                        }
                        map.invalidate();
                        if (isChecked) {
                            Utils.toast(getBaseContext(), "Áreas: Ativado");
                        } else {
                            Utils.toast(getBaseContext(), "Áreas: Desativado");
                        }
                    }
                });
                cbxCurvasDeNivel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //TODO fazer a ação layer curvas de nível
                        if (isChecked) {
                            Utils.toast(getBaseContext(), "Curvas de Nível: Ativado");
                        } else {
                            Utils.toast(getBaseContext(), "Curvas de Nível: Desativado");
                        }
                    }
                });
                btnPopupLayerFechar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupLayers.dismiss();
                    }
                });
                btnPopupLayerFecharTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupLayers.dismiss();
                    }
                });
                btnPopupLayerFecharBottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupLayers.dismiss();
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
                popupLayers.showAtLocation(layoutTelaPrincipal, Gravity.CENTER, 0, 0);
            }
        });
        fabRotacao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mRotationGestureOverlay.isEnabled()) {
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_rotation_blocked_white_24dp);
                    mRotationGestureOverlay.setEnabled(false);
                } else {
                    ((FloatingActionButton) v).setImageResource(R.drawable.ic_rotation_white_24dp);
                    mRotationGestureOverlay.setEnabled(true);
                }
            }
        });
        fabConcluido.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (modalAdicionarPonto != null) {
                    List<Overlay> overlays = map.getOverlays();
                    overlays.remove(modalAdicionarPonto);
                    modalAdicionarPonto = null;
                }
                if (modalAdicionarArea != null) {
                    Area novaArea = modalAdicionarArea.finalizar();
                    Log.d("Agritopo", "Nova área: " + novaArea.toString());
                    if (novaArea.ehValida()) {
                        Log.d("Agritopo", "Nova área é válida, adicionando à lista");
                        if (areaList == null)
                            areaList = new ArrayList<Area>();
                        areaList.add(novaArea);
                        if (exibirAreas)
                            map.getOverlays().add(novaArea.poligono);
                    } else {
                        Log.d("Agritopo", "Nova área é inválida, descartando");
                    }
                    if (map.getOverlays().contains(modalAdicionarArea)) {
                        map.getOverlays().remove(modalAdicionarArea);
                    }
                    modalAdicionarArea = null;
                }
                fabCancelar.setVisibility(View.INVISIBLE);
                fabConcluido.setVisibility(View.INVISIBLE);
                famNovo.setVisibility(View.VISIBLE);
                map.invalidate();
            }
        });
        fabCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (modalAdicionarArea != null) {
                    Log.d("Agritopo", "Nova área é inválida, descartando");
                    if (map.getOverlays().contains(modalAdicionarArea)) {
                        map.getOverlays().remove(modalAdicionarArea);
                    }
                    modalAdicionarArea = null;
                }
                fabCancelar.setVisibility(View.INVISIBLE);
                fabConcluido.setVisibility(View.INVISIBLE);
                famNovo.setVisibility(View.VISIBLE);
                map.invalidate();
            }
        });
    }

    public void inicializarMapas() {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        buscarMapasDoImovel();

        Configuration.getInstance().setMapViewHardwareAccelerated(true);
        map = (MapView) findViewById(R.id.map);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setTilesScaledToDpi(true);

        mRotationGestureOverlay = new RotationGestureOverlay(map);
        mRotationGestureOverlay.setEnabled(false);
        map.getOverlays().add(mRotationGestureOverlay);

        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(Utils.getDisplaySize(this).x / 2, 10);
        map.getOverlays().add(mScaleBarOverlay);

        mCompassOverlay = new CompassOverlay(this, map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay);

        mMyLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        mMyLocationNewOverlay.enableMyLocation();
        map.getOverlays().add(mMyLocationNewOverlay);

        mapController = map.getController();
        criarListaPontos();
        Configuration.getInstance().setDebugMode(true);

        if ((listaArquivosMapas != null) && (listaArquivosMapas.length > 0)) {
            carregarMapaDeArquivo(map, listaArquivosMapas[0]);
            map.getOverlays().add(geoPointList);
        } else {
            carregarMapaOnline();
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
        carregarMapasNoMenu(menu);
        return true;
    }

    public boolean carregarMapasNoMenu(Menu menu) {
        menu.add(0, -1, 0, "Online (OpenStreetMaps)");
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

        int id = item.getItemId();

        if (id == -1) {
            map.setUseDataConnection(true);
            carregarMapaOnline();
        } else {
            if (listaArquivosMapas != null) {
                Log.d("Agritopo", "Mapa selecionado: " + listaArquivosMapas[id].toString());
                carregarMapaDeArquivo(map, listaArquivosMapas[id]);
            }
        }
        map.invalidate();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cadastros) {

        } else if (id == R.id.nav_mapas) {
            Intent intent = new Intent(this, ElementoListActivity.class);
            //EditText editText = (EditText) findViewById(R.id.edit_message);
            //String message = editText.getText().toString();
            //intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);

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
        if (mMyLocationNewOverlay.isFollowLocationEnabled()) {
            if (mMyLocationNewOverlay.getMyLocation() != null)
                mapController.setCenter(mMyLocationNewOverlay.getMyLocation());
        }
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

    public void buscarMapasDoImovel() {
        File pasta_mapas = new File(caminhoPastaMapas);
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
        listaArquivosMapas = pasta_mapas.listFiles(filtro);
        if (listaArquivosMapas != null)
            for (File arquivo : listaArquivosMapas)
                Log.d("Agritopo", "Arquivo mapa: " + arquivo.toString());
    }

    public void carregarMapaOnline() {
        map.setUseDataConnection(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(10);
        if (mMyLocationNewOverlay.getMyLocation() != null)
            mapController.animateTo(mMyLocationNewOverlay.getMyLocation());
    }

    public void carregarMapaDeArquivo(MapView map, File arquivo) {
        MapaTiles am = new MapaTiles(arquivo);

        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(this);
        XYTileSource mbtilesRender = new XYTileSource(
                "mbtiles",
                am.zoomMin,
                am.zoomMax,
                256,
                am.formatoImagem,
                new String[]{}
        );
        IArchiveFile[] files = {MBTilesFileArchive.getDatabaseFileArchive(arquivo)};
        MapTileModuleProviderBase moduleProvider = new MapTileFileArchiveProvider(simpleReceiver, mbtilesRender, files);
        MapTileProviderArray mProvider = new MapTileProviderArray(mbtilesRender, null, new MapTileModuleProviderBase[]{moduleProvider});
        map.setTileProvider(mProvider);

        IMapController mapController = map.getController();
        mapController.setZoom(15);
        mapController.animateTo(am.pontoCentral);
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
        Utils.toast(this, "Ponto indicado em (" + p.getLatitude() + "," + p.getLongitude() + ")");
        InfoWindow.closeAllInfoWindowsOn(map);
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        //Toast.makeText(this, "Long press", Toast.LENGTH_SHORT).show();
        //17. Using Polygon, defined as a circle:
        //Polygon circle = new Polygon();
        //circle.setPoints(Polygon.pointsAsCircle(p, 2000.0));
        //circle.setFillColor(0x12121212);
        //circle.setStrokeColor(Color.RED);
        //circle.setStrokeWidth(2);
        //map.getOverlays().add(circle);
        //circle.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
        //circle.setTitle("Centralizado em " + p.getLatitude() + "," + p.getLongitude());

        //18. Using GroundOverlay
        //GroundOverlay myGroundOverlay = new GroundOverlay();
        //myGroundOverlay.setPosition(p);
        //Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_place_black_24dp, null);
        //myGroundOverlay.setImage(d.mutate());
        //myGroundOverlay.setDimensions(200.0f);
        //myGroundOverlay.setTransparency(0.25f);
        //myGroundOverlay.setBearing(mGroundOverlayBearing);
        //mGroundOverlayBearing += 20.0f;
        //map.getOverlays().add(myGroundOverlay);

        //PopupPonto popupPonto = new PopupPonto(map.getContext());
        geoPointList.addItem(new OverlayItem("Titulo exemplo", "Descrição exemplo\r\nem várias linhas", p));
        map.invalidate();
        return true;
    }

    void criarListaPontos() {
        // https://stackoverflow.com/questions/41090639/add-marker-to-osmdroid-5-5-map

        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        //GeoPoint pontoAeroporto = new GeoPoint(-27.1341, -52.6606);
        //GeoPoint pontoDesbravador = new GeoPoint(-27.1048003, -52.6145871);
        //items.add(new OverlayItem("Aeroporto de Chapecó", "Descrição", pontoAeroporto));
        //items.add(new OverlayItem("Desbravador", "Descrição", pontoDesbravador));

        //the overlay
        geoPointList = new ItemizedOverlayWithFocus<OverlayItem>(
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
        geoPointList.setFocusItemsOnTap(true);
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
            mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo).setVisibility(VISIBLE);
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
