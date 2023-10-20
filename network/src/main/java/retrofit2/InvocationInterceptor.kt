package retrofit2

import com.arthur.commonlib.ability.Logger
import com.arthur.network.scope.manager.RequestScopeManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.ParameterizedType

/**
 * ## 网络请求信息分发拦截器，利用retrofit的[Invocation]TAG的能力
 * * 此拦截器调用了[retrofit2.Utils]的方法，此工具局限于包访问
 */
class InvocationInterceptor : Interceptor {

    val tag: String = this::class.java.simpleName

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        Logger.logI(tag, "${request.hashCode()} $request")
        request.tag(Invocation::class.java)?.apply {
            method().let { method ->
                Logger.logI(
                    tag, String.format(
                        "\n\n%s.%s params: %s%n",
                        method.declaringClass.simpleName,
                        method.name,
                        this.arguments()
                    )
                )

                val parameterTypes = method.genericParameterTypes
                val responseType = Utils.getParameterLowerBound(
                    0,
                    parameterTypes[parameterTypes.size - 1] as ParameterizedType
                )
                RequestScopeManager.getObserver()?.onMethodInvoke(
                    request.url.toString(),
                    responseType,
                    request.method,
                    request.body,
                    request.headers
                )
            }
        }

        return chain.proceed(request)
    }
}