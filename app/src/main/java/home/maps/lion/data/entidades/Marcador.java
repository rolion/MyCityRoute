package home.maps.lion.data.entidades;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Lion on 03/06/15.
 */
@DatabaseTable(tableName = "marcador")
public class Marcador {
    @DatabaseField(generatedId = true)
    private int _id;
    @DatabaseField
    private String title;
    @DatabaseField
    private double latitud;
    @DatabaseField
    private double longitud;
    @DatabaseField
    private float distancia;

    public Marcador(int _id, String title, double latitud, double longitud, float distancia) {
        this._id = _id;
        this.title = title;
        this.latitud = latitud;
        this.longitud = longitud;
        this.distancia = distancia;
    }
    public Marcador( String title, double latitud, double longitud, float distancia) {
        this.title = title;
        this.latitud = latitud;
        this.longitud = longitud;
        this.distancia = distancia;
    }

    public Marcador() {

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getlongitud() {
        return longitud;
    }

    public void setlongitud(double longitud) {
        this.longitud = longitud;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    @Override
    public String toString() {
        return "Marcador{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", distancia=" + distancia +
                '}';
    }
}
