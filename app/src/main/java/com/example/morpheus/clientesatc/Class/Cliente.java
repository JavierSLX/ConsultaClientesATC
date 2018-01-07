package com.example.morpheus.clientesatc.Class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morpheus on 06/01/2018.
 */

public class Cliente
{
    private String clave;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String telefono;
    private String activo;

    public Cliente(String clave, String nombre, String direccion, String ciudad, String telefono, String activo)
    {
        this.clave = clave;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.activo = activo;
    }

    public String getClave()
    {
        return clave;
    }

    public void setClave(String clave)
    {
        this.clave = clave;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getDireccion()
    {
        return direccion;
    }

    public void setDireccion(String direccion)
    {
        this.direccion = direccion;
    }

    public String getCiudad()
    {
        return ciudad;
    }

    public void setCiudad(String ciudad)
    {
        this.ciudad = ciudad;
    }

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public String getActivo()
    {
        return activo;
    }

    public void setActivo(String activo)
    {
        this.activo = activo;
    }

    public static List<Cliente> sacarListaClientes(JSONArray array)
    {
        List<Cliente> lista = new ArrayList<>();
        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                Cliente cliente = new Cliente(jsonObject.getString("0"), jsonObject.getString("1"), jsonObject.getString("2"), jsonObject.getString("3"),
                        jsonObject.getString("4"), jsonObject.getString("5"));
                lista.add(cliente);
            }
        }
        catch (JSONException e)
        {
            lista = null;
        }

        return lista;
    }
}
