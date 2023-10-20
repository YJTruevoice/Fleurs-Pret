package com.arthur.network.download

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface Service {
    @Streaming
    @GET
    fun download(@Url url: String?): Flowable<ResponseBody?>?
}