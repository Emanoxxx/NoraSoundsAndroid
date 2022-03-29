import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NoraAPI {
    @GET("http://192.168.100.4:8080/")
    fun getAllPost(): Call<List<Post>>

    @GET("http://192.168.100.4:8080/{id}")
    fun getPostByid(@Path("id") id:Int):Call<Post>

    @POST("http://192.168.100.4:8080/{id}")
    fun editPostById(@Path("id")id: Int, @Body post: Post?):Call<Post>
}