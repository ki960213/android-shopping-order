package woowacourse.shopping.data.product

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import woowacourse.shopping.data.RemoteHost
import woowacourse.shopping.domain.Product
import java.io.IOException

class ProductRemoteService(private val host: RemoteHost) : ProductDataSource {
    private val client = OkHttpClient()

    override fun findAll(onFinish: (List<Product>) -> Unit) {
        val path = "/products"
        val request = Request.Builder().url(host.url + path).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: return
                val json = JSONArray(body)
                val products = (0 until json.length()).map {
                    val jsonObject = json.getJSONObject(it)
                    parseToProduct(jsonObject)
                }
                onFinish(products)
            }
        })
    }

    override fun findAll(limit: Int, offset: Int, onFinish: (List<Product>) -> Unit) {
        val path = "/products?limit=$limit&offset=$offset"
        val request = Request.Builder().url(host.url + path).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: return
                val json = JSONArray(body)
                val products = (0 until json.length()).map {
                    val jsonObject = json.getJSONObject(it)
                    parseToProduct(jsonObject)
                }
                onFinish(products)
            }
        })
    }

    override fun countAll(onFinish: (Int) -> Unit) {
        findAll {
            onFinish(it.size)
        }
    }

    override fun findById(id: Long, onFinish: (Product?) -> Unit) {
        val path = "/products/$id"
        val request = Request.Builder().url(host.url + path).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: return
                val product = parseToProduct(JSONObject(body))
                onFinish(product)
            }
        })
    }

    private fun parseToProduct(jsonObject: JSONObject): Product {
        val id = jsonObject.getLong("id")
        val name = jsonObject.getString("name")
        val price = jsonObject.getInt("price")
        val imageUrl = jsonObject.getString("imageUrl")
        return Product(id, imageUrl, name, price)
    }
}