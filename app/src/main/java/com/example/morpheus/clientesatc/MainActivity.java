package com.example.morpheus.clientesatc;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.morpheus.clientesatc.Fragments.ListaClientesFragment;
import com.example.morpheus.clientesatc.Fragments.LoginFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.ILogin
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Coloca el contenedor de login por default
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contenedorFragment, new LoginFragment()).addToBackStack(null).commit();
    }

    //Se comunica con el fragmento Login
    @Override
    public void comunicacionConLoginFragment(String usuario_id, String puntoVenta_id)
    {
        //Inicia el siguiente fragmento
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedorFragment, ListaClientesFragment.getInstance(usuario_id, puntoVenta_id)).addToBackStack(null).commit();
    }
}
