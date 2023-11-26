package com.facile.immediate.electronique.fleurs.pret.common.json

import com.facile.immediate.electronique.fleurs.pret.net.BaseResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface JsonUploadApi {
    @POST("/fleurspret/spirit/followDearSalad")
    suspend fun jsonFeature(@Body requestBody: RequestBody): BaseResponse<Any?>
}