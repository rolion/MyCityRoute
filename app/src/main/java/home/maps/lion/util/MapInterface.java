package home.maps.lion.util;

import home.maps.lion.data.entidades.Marcador;

/**
 * Created by Lion on 07/06/15.
 */
public interface MapInterface {
    public void callOptionFragment();
    public void editMarker(Marcador m);
    public void setUserMarker(Marcador m);
    public void deleteMarker();
    public void callDialogFragment(byte estado);
}
