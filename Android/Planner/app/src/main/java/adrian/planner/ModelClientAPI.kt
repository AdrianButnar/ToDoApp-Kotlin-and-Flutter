package adrian.planner

import io.reactivex.Completable
import io.reactivex.Observable
import io.realm.RealmResults
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ModelClientAPI {
    @GET("items") fun getItems(): Observable<List<ShoppingItem>>
    @POST("items") fun addItem(@Body Item: ShoppingItem): Completable
    @DELETE("items/{id}") fun deleteItem(@Path("id") id: Int) : Completable
    @PUT("items/{id}") fun updateItem(@Path("id")id: Int, @Body shoppingItem: ShoppingItem) : Completable

    companion object {

        fun create(): ModelClientAPI {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://10.0.2.2:8080/api/")
                .build()

            return retrofit.create(ModelClientAPI::class.java)
        }
    }
}