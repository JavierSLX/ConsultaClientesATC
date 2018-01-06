package com.example.morpheus.clientesatc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.morpheus.clientesatc.R;

/**
 * Created by Morpheus on 06/01/2018.
 */

public class ListaClientesFragment extends Fragment
{
    private static final String LLAVE_USUARIO = "USUARIO";
    private static final String LLAVE_PERMISO = "PERMISO";
    private static String usuario_id;
    private static String permiso_id;

    //Instancia al mismo fragmento
    public static ListaClientesFragment getInstance(String usuario_id, String permiso_id)
    {
        ListaClientesFragment fragment = new ListaClientesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LLAVE_USUARIO, usuario_id);
        bundle.putString(LLAVE_PERMISO, permiso_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    //Recupera los datos que se le pasan
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            usuario_id = getArguments().getString(LLAVE_USUARIO);
            permiso_id = getArguments().getString(LLAVE_PERMISO);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        getActivity().setTitle("Lista de Clientes");
    }

    //Infla la vista
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_lista_clientes, container, false);
    }

    //Hace referencia a los view

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Toast.makeText(getContext(), "Usuario: " + usuario_id + " Permiso: " + permiso_id, Toast.LENGTH_SHORT).show();
    }
}
