package com.myprojects.convo.Database_Setup;

import com.myprojects.convo.Values.Values;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit_init {

    private static Retrofit retrofit;


    private Retrofit_init()
    {
    }



    public static Retrofit getRetrofit()
    {
        if (retrofit == null)
        {
            retrofit = new  Retrofit
                            .Builder()
                            .baseUrl(Values.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


        }

        return retrofit;


    }



}
