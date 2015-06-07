package home.maps.lion.data.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import home.maps.lion.data.entidades.Marcador;
import home.maps.lion.mycityroute.R;

/**
 * Created by Lion on 03/06/15.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "marcador.db";
    private static final int DATABASE_VERSION = 1;
    private Dao<Marcador, Integer> marcadorDao;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Marcador.class);
        } catch (SQLException e) {
            Log.e(DataBaseHelper.class.getName(), "Unable to create datbases", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
    public Dao<Marcador, Integer> getMarcadorDao() throws SQLException {
        if (marcadorDao == null) {
            marcadorDao = getDao(Marcador.class);
        }
        return marcadorDao;
    }
}
