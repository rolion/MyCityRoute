package home.maps.lion.myframent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import home.maps.lion.data.entidades.Marcador;
import home.maps.lion.mycityroute.R;
import home.maps.lion.util.MapInterface;

/**
 * Created by Lion on 02/06/15.
 */
public class MarkerDialogFragment extends DialogFragment {
    private MapInterface mDialogFramentInterface;
    private Context context;
    private EditText etnombre;
    private EditText etdistancia;
    private Button btnguardar;
    private Button btncancelar;
    private static MarkerDialogFragment f;
    public static byte MarkerDialog_newMarker=1;
    public static byte MarkerDialog_editMarker=2;
    private static final String nameParam="Estado";
    private byte estado;
    public static MarkerDialogFragment getInstance(byte estado){
        f=new MarkerDialogFragment();
        Bundle b=new Bundle();
        b.putByte(nameParam,estado);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_dialog, container, false);
        this.estado=this.getArguments().getByte(nameParam);
        etnombre= (EditText) v.findViewById(R.id.etnombremarcador);
        etdistancia= (EditText) v.findViewById(R.id.etdistancia);
        btnguardar= (Button) v.findViewById(R.id.btnguardar);
        btncancelar= (Button) v.findViewById(R.id.btncancelar);
        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialogFramentInterface!=null)
                    if (!etnombre.getText().toString().trim().equals("") &&
                            !etdistancia.getText().toString().trim().equals("")) {
                        Marcador m = new Marcador();
                        m.setTitle(etnombre.getText().toString());
                        m.setDistancia(Float.valueOf(etdistancia.getText().toString()));
                        switch (estado) {
                            case 1://New Markes
                                mDialogFramentInterface.setUserMarker(m);
                                break;
                            case 2://Edit Marker
                                mDialogFramentInterface.editMarker(m);
                                break;
                        }
                        f.dismiss();
                    } else
                        Toast.makeText(context, "Debe introducir valores", Toast.LENGTH_LONG).show();
            }
        });
        btncancelar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                f.dismiss();
            }
        });
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mDialogFramentInterface = (MapInterface) activity;
            context=activity.getApplicationContext();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public interface DialogFramentInterface{
        public void setUserMarker(String nombre,String distancia);

    }
}
