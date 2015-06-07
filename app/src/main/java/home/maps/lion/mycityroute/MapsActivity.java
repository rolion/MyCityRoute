package home.maps.lion.mycityroute;

import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import home.maps.lion.data.entidades.Marcador;
import home.maps.lion.data.orm.DataBaseHelper;
import home.maps.lion.myframent.MarkerDialogFragment;
import home.maps.lion.myframent.OptionFragment;
import home.maps.lion.util.MapInterface;

import static home.maps.lion.mycityroute.R.mipmap.icon_marker_green;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener, MapInterface{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List listaMarcador;
    private FragmentTransaction ft;
    private LatLng posmarker;
    private DataBaseHelper databaseHelper = null;
    private Marker selectedMarker;
    private DialogFragment actualDialog;
    private Dao<Marcador, Integer> marcadorDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Actividad","onCreate");
        setContentView(R.layout.activity_maps);
        setMap();

    }
    private void printAllMarker(){
        try {
            marcadorDao = getHelper().getMarcadorDao();
            List<Marcador> lmarcador=marcadorDao.queryForAll();
            for (Marcador o : lmarcador) {
                this.mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(o.getLatitud(),o.getlongitud()))
                        .title(o.getTitle())
                        .icon(BitmapDescriptorFactory.fromResource(icon_marker_green))
                        .anchor(0.5f, 1f));
                Log.i("Marcador",o.toString());
            }
            if(lmarcador==null || lmarcador.size()==0){
                Toast.makeText(getApplicationContext(),"no hay nada en la base de datos", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void saveMarker(Marcador m){

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Actividad","onResume");
    }

    private DataBaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        }
        return databaseHelper;
    }

    private void setMap() {
      ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap=googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-17.782902, -63.181854),16));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                setUpNotificacion();
                //Toast.makeText(getApplicationContext(),"cambio mi posisicon",Toast.LENGTH_LONG).show();
            }
        });
        this.listaMarcador=new ArrayList();
        mMap.setOnMapLongClickListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                selectedMarker=marker;
                callOptionFragment();
                Toast.makeText(getApplicationContext(),"Click en burbuja",Toast.LENGTH_LONG).show();
            }
        });
        printAllMarker();
        Log.i("Actividad","onReadyMap");
    }

    private void setUpNotificacion(){
        NotificationCompat.Builder mBuilder =
                 new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.icon_marker)
                        .setContentTitle("My notification")
                        .setContentText("cambio la posicion");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MapsActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MapsActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        this.posmarker=latLng;
        showDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
    private void showDialog(){
         MarkerDialogFragment dialog= MarkerDialogFragment.getInstance(MarkerDialogFragment.MarkerDialog_newMarker);
        dialog.show(getSupportFragmentManager(),"MyDialog");
    }



    @Override
    public void callOptionFragment() {
        actualDialog= OptionFragment.newInstance();
        actualDialog.show(getSupportFragmentManager(),"dialog");
    }

    @Override
    public void editMarker(Marcador m) {
        try {
            List lmarcadorUsuario=   getHelper().getMarcadorDao().queryForAll();
            for(int i=0; i<lmarcadorUsuario.size();i++){
                Marcador mAux= (Marcador) lmarcadorUsuario.get(i);
                if(mAux.getLatitud()==selectedMarker.getPosition().latitude
                        && mAux.getlongitud()==selectedMarker.getPosition().longitude){
                    mAux.setTitle(m.getTitle());
                    mAux.setDistancia(m.getDistancia());
                    getHelper().getMarcadorDao().update(mAux);
                    i=lmarcadorUsuario.size()+1;
                    selectedMarker.setTitle(mAux.getTitle());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserMarker(Marcador m) {

        m.setLatitud(this.posmarker.latitude);
        m.setlongitud(this.posmarker.longitude);
        Marker marcador=mMap.addMarker(new MarkerOptions()
                .position(this.posmarker)
                .title(m.getTitle())
                .icon(BitmapDescriptorFactory.fromResource(icon_marker_green))
                .anchor(0.5f, 1f));

        try {
            marcadorDao = getHelper().getMarcadorDao();
            marcadorDao.create(m);
           // marcadorDao.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Error",e.getMessage());
            Log.e("Error",e.getSQLState());
            Log.e("Error", String.valueOf(e.getErrorCode()));
            Log.e("Error", String.valueOf(e.getCause()));
        }

        listaMarcador.add(m);
        Toast.makeText(getApplicationContext(),"Nombre marcador="+m.getTitle()+"Distancia="+m.getDistancia(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteMarker() {
        try {
            List lmarcadorUsuario=   getHelper().getMarcadorDao().queryForAll();
            for(int i=0; i<lmarcadorUsuario.size();i++){
                Marcador mAux= (Marcador) lmarcadorUsuario.get(i);
                if(mAux.getLatitud()==selectedMarker.getPosition().latitude
                        && mAux.getlongitud()==selectedMarker.getPosition().longitude){
                    getHelper().getMarcadorDao().delete(mAux);
                    i=lmarcadorUsuario.size()+1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        selectedMarker.remove();
    }

    @Override
    public void callDialogFragment(byte estado) {
        actualDialog= MarkerDialogFragment.getInstance(estado);
        actualDialog.show(getSupportFragmentManager(),"dialog");
    }
}
