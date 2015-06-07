package home.maps.lion.myframent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import home.maps.lion.mycityroute.R;
import home.maps.lion.util.MapInterface;


public class OptionFragment extends DialogFragment {

    private MapInterface mListener;
    private Button btnEditar;
    private Button btnEliminar;
    public static OptionFragment newInstance() {
        OptionFragment fragment = new OptionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public OptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_option, container, false);
        this.btnEditar= (Button) v.findViewById(R.id.btneditar);
        this.btnEditar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mListener!=null)
                    mListener.callDialogFragment(MarkerDialogFragment.MarkerDialog_editMarker);
                dismiss();
            }
        });
        this.btnEliminar= (Button) v.findViewById(R.id.btneliminar);
        this.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleteMarker();
                dismiss();
            }
        });
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MapInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
