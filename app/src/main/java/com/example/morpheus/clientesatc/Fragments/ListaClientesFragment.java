package com.example.morpheus.clientesatc.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.DisplayContext;
import android.icu.text.MessagePattern;
import android.os.Bundle;
import android.renderscript.Type;
import android.support.annotation.Nullable;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.morpheus.clientesatc.Adapters.ListaClientesAdapter;
import com.example.morpheus.clientesatc.Class.Cliente;
import com.example.morpheus.clientesatc.R;
import com.example.morpheus.clientesatc.Resources.IBasic;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.Proxy;
import java.security.KeyRep;

/**
 * Created by Morpheus on 06/01/2018.
 */

public class ListaClientesFragment extends Fragment implements IBasic, Response.Listener<JSONArray>, Response.ErrorListener, View.OnClickListener
{
    private static final String LLAVE_USUARIO = "USUARIO";
    private static final String LLAVE_VENTA = "PUNTOVENTA";
    private static String usuario_id;
    private static String puntoVenta_id;
    private EditText edtElemento;
    private TextView txtUltimaR;
    private ListView listaClientes;
    private ProgressDialog progressDialog;
    private boolean porNombre = true;

    //Instancia al mismo fragmento
    public static ListaClientesFragment getInstance(String usuario_id, String puntoVenta_id)
    {
        ListaClientesFragment fragment = new ListaClientesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LLAVE_USUARIO, usuario_id);
        bundle.putString(LLAVE_VENTA, puntoVenta_id);
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
            puntoVenta_id = getArguments().getString(LLAVE_VENTA);
        }
    }

    //Coloca el titulo apenas se crea el fragmento
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

        //Instancia los view
        Button btConsultar = (Button)view.findViewById(R.id.btConsultar);
        Switch swtElemento = (Switch)view.findViewById(R.id.swtElemento);
        edtElemento = (EditText)view.findViewById(R.id.edtBusqueda);
        listaClientes = (ListView)view.findViewById(R.id.listClientes);
        txtUltimaR = (TextView)view.findViewById(R.id.txtUltimaR);

        //Le da un hint al edittext
        edtElemento.setHint("Nombre");

        //Cuando cambia el switch
        swtElemento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                porNombre = b;
                edtElemento.setText("");
                if (b)
                {
                    edtElemento.setHint("Nombre");
                    edtElemento.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else
                {
                    edtElemento.setHint("Clave");
                    edtElemento.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            }
        });

        //Evento del boton
        btConsultar.setOnClickListener(this);

        //Arma el dialogo de carga
        //Crea el progressDialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Un momento...");
        progressDialog.show();

        //Arma la consulta para sacar la última R
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String consulta = "SELECT CONCAT(p.tipo, '-', cc.numero) AS clave FROM punto_venta p, clave_cliente cc WHERE p.id = cc.puntoVenta_id AND cc.puntoVenta_id = " +
                puntoVenta_id + " ORDER BY cc.numero DESC LIMIT 1";
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("urlUltima", url);

        //Arma la peticion y la pone en cola
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.PUT, url, null, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                progressDialog.hide();

                try
                {
                    txtUltimaR.setText(response.getJSONObject(0).getString("0"));
                }
                catch (JSONException e)
                {
                    txtUltimaR.setText("Valor no encontrado");
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.hide();

                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btConsultar:
                consultaWebService();
                edtElemento.setText("");
                break;

            default:
                break;
        }
    }

    private void consultaWebService()
    {
        //Muestra el dialogo de carga
        progressDialog.show();

        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String consulta;
        if (edtElemento.getText().toString().length() > 0)
        {
            if (porNombre)
            {
                consulta = "SELECT CONCAT(p.tipo, '-', cc.numero) AS clave, c.nombre, c.direccion, cd.nombre AS ciudad, c.telefono, cc.activo FROM punto_venta p, cliente c, " +
                        "clave_cliente cc, ciudad cd WHERE p.id = cc.puntoVenta_id AND c.id = cc.cliente_id AND c.ciudad_id = cd.id AND cc.puntoVenta_id = " + puntoVenta_id +
                        " AND c.nombre LIKE '" + edtElemento.getText().toString().trim() + "%25' ORDER BY cc.numero ASC;";
            } else
            {
                consulta = "SELECT CONCAT(p.tipo, '-', cc.numero) AS clave, c.nombre, c.direccion, cd.nombre AS ciudad, c.telefono, cc.activo FROM punto_venta p, cliente c, " +
                        "clave_cliente cc, ciudad cd WHERE p.id = cc.puntoVenta_id AND c.id = cc.cliente_id AND c.ciudad_id = cd.id AND cc.puntoVenta_id = " + puntoVenta_id +
                        " AND cc.numero LIKE '" + String.format("%1$03d", Integer.valueOf(edtElemento.getText().toString().trim())) + "%25' ORDER BY cc.numero ASC;";
            }
        }
        else
        {
            consulta = "SELECT CONCAT(p.tipo, '-', cc.numero) AS clave, c.nombre, c.direccion, cd.nombre AS ciudad, c.telefono, cc.activo FROM punto_venta p, cliente c, " +
                    "clave_cliente cc, ciudad cd WHERE p.id = cc.puntoVenta_id AND c.id = cc.cliente_id AND c.ciudad_id = cd.id AND cc.puntoVenta_id = " + puntoVenta_id +
                    " ORDER BY cc.numero ASC;";
        }

        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("urlLista", url);

        //Arma la peticion y la pone en cola
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.PUT, url, null, this, this);
        queue.add(request);
    }

    @Override
    public void onResponse(JSONArray response)
    {
        if (response.length() > 0)
        {
            ListaClientesAdapter adapter = new ListaClientesAdapter(getContext(), Cliente.sacarListaClientes(response));
            listaClientes.setAdapter(adapter);

            progressDialog.hide();
        }
        else
            Toast.makeText(getContext(), "No hay coincidencias", Toast.LENGTH_SHORT).show();
    }

    //Error en el webservice
    @Override
    public void onErrorResponse(VolleyError error)
    {
        progressDialog.hide();
        Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        Log.i("errorlista", error.toString());
    }


}
