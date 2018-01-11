package com.example.morpheus.clientesatc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.morpheus.clientesatc.Class.Cliente;
import com.example.morpheus.clientesatc.R;

import java.util.List;

/**
 * Created by Morpheus on 06/01/2018.
 */

public class ListaClientesAdapter extends BaseAdapter
{
    private Context context;
    private List<Cliente> lista;

    public ListaClientesAdapter(Context context, List<Cliente> lista)
    {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount()
    {
        return lista.size();
    }

    @Override
    public Cliente getItem(int i)
    {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return lista.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View vista = view;
        if (view == null)
            vista = inflater.inflate(R.layout.item_cliente, null);

        //Referencia los view
        TextView txtIdentificador = (TextView)vista.findViewById(R.id.txtIdentificador);
        TextView txtNombre = (TextView)vista.findViewById(R.id.txtNombre);
        TextView txtTelefono = (TextView)vista.findViewById(R.id.txtTelefono);
        TextView txtDireccion = (TextView)vista.findViewById(R.id.txtDireccion);
        TextView txtCiudad = (TextView)vista.findViewById(R.id.txtCiudad);

        //Asigna los valores
        txtNombre.setText(getItem(i).getNombre());
        txtTelefono.setText(getItem(i).getTelefono());
        txtDireccion.setText(getItem(i).getDireccion());
        txtCiudad.setText(getItem(i).getCiudad());

        //Checa si el usuario está activo y cambia de color el identificador
        if (getItem(i).getActivo().equals("1"))
        {
            txtIdentificador.setText(getItem(i).getClave());
            txtIdentificador.setTextColor(vista.getResources().getColor(R.color.azul));
        }
        else
        {
            txtIdentificador.setText(getItem(i).getClave() + " (INACTIVO)");
            txtIdentificador.setTextColor(vista.getResources().getColor(R.color.rojo));
        }

        return vista;
    }
}
