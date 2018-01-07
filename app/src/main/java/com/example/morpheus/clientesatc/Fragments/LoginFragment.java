package com.example.morpheus.clientesatc.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.morpheus.clientesatc.R;
import com.example.morpheus.clientesatc.Resources.IBasic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Morpheus on 06/01/2018.
 */

public class LoginFragment extends Fragment implements IBasic, Response.Listener<JSONArray>, Response.ErrorListener, View.OnClickListener
{
    private EditText edtUsuario, edtPass;
    private Button btAceptar;
    private ProgressDialog progressDialog;
    private static ILogin comunicacion;

    //Interfaz que permite comunicar con la actividad
    public static interface ILogin
    {
        void comunicacionConLoginFragment(String usuario_id, String puntoVenta_id);
    }

    //Coloca el titulo del Fragmento y castea la actividad
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        getActivity().setTitle("Login");

        try
        {
            comunicacion = (ILogin)getActivity();
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(getActivity().toString() + " debe implementar ILogin");
        }
    }

    //Se crea la vista y la infla
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    //Referencia los view
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        edtUsuario = (EditText)view.findViewById(R.id.edtUsuario);
        edtPass = (EditText)view.findViewById(R.id.edtPass);
        btAceptar = (Button)view.findViewById(R.id.btAceptar);

        btAceptar.setOnClickListener(this);
    }

    //Cuando se le da click sobre un view
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            //Botón Aceptar
            case R.id.btAceptar:
                consultaWebService();
                break;

            default:
                break;
        }
    }

    //Consulta el webservice
    private void consultaWebService()
    {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Un momento...");
        progressDialog.show();

        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "SELECT p.usuario_id, ptu.puntoVenta_id FROM permiso p, puntoventa_usuario ptu, usuario u WHERE u.id = ptu.usuario_id AND p.usuario_id = u.id " +
                "AND p.nick = '" + edtUsuario.getText().toString().trim() + "' AND p.pwd = '" + edtPass.getText().toString().trim() + "';" ;
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("urlLogin", url);

        //Arma la peticion y la pone en cola
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.PUT, url, null, this, this);
        queue.add(request);
    }

    //La respuesta del webservice
    @Override
    public void onResponse(JSONArray response)
    {
        progressDialog.hide();
        try
        {
            if (response.length() > 0)
            {
                JSONObject jsonObject = response.getJSONObject(0);
                comunicacion.comunicacionConLoginFragment(jsonObject.getString("0"), jsonObject.getString("1"));
            }
            else
            {
                Toast.makeText(getContext(), "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e)
        {
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Error en el webservice
    @Override
    public void onErrorResponse(VolleyError error)
    {
        progressDialog.hide();
        Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
