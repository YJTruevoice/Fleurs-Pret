package com.arthur.commonlib.utils.json.adapter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

/**
 * 处理服务端不靠谱的返回值
 */
class GsonAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson?, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType as Class<T>
        //针对不同类型返回自定义的Adapter
        return when(rawType) {
            String::class.java -> StringAdapter() as TypeAdapter<T>
            Long::class.java -> LongAdapter() as TypeAdapter<T>
            Int::class.java -> IntAdapter() as TypeAdapter<T>
            Double::class.java -> DoubleAdapter() as TypeAdapter<T>
            Boolean::class.java -> BooleanAdapter() as TypeAdapter<T>
            else -> null
        }
    }

    class LongAdapter : TypeAdapter<Long?>() {
        @Throws(IOException::class)
        override fun read(reader: JsonReader): Long {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return 0L
            }
            if (reader.peek() == JsonToken.STRING) {
                val string = reader.nextString()
                var result = 0L
                try {
                    result = string.toLong()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return result
            }
            return reader.nextLong()
        }

        //序列化用到的，这里我们实现默认的代码就行
        @Throws(IOException::class)
        override fun write(writer: JsonWriter, value: Long?) {
            writer.value(value)
        }
    }

    class IntAdapter : TypeAdapter<Int?>() {
        @Throws(IOException::class)
        override fun read(reader: JsonReader): Int {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return 0
            }
            if (reader.peek() == JsonToken.STRING) {
                val string = reader.nextString()
                var result = 0
                try {
                    result = string.toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return result
            }
            return reader.nextInt()
        }

        //序列化用到的，这里我们实现默认的代码就行
        @Throws(IOException::class)
        override fun write(writer: JsonWriter, value: Int?) {
            writer.value(value)
        }
    }

    class DoubleAdapter : TypeAdapter<Double?>() {
        @Throws(IOException::class)
        override fun read(reader: JsonReader): Double {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return 0.0
            }
            if (reader.peek() == JsonToken.STRING) {
                val string = reader.nextString()
                var result = 0.0
                try {
                    result = string.toDouble()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return result
            }
            return reader.nextDouble()
        }

        //序列化用到的，这里我们实现默认的代码就行
        @Throws(IOException::class)
        override fun write(writer: JsonWriter, value: Double?) {
            writer.value(value)
        }
    }

    class StringAdapter : TypeAdapter<String?>() {
        @Throws(IOException::class)
        override fun read(reader: JsonReader): String {
            //如果值为null，返回空字符串
            if (reader.peek() === JsonToken.NULL) {
                reader.nextNull()
                return ""
            }
            return reader.nextString()
        }

        //序列化用到的，这里我们实现默认的代码就行
        @Throws(IOException::class)
        override fun write(writer: JsonWriter, value: String?) {
            writer.value(value)
        }
    }

    class BooleanAdapter : TypeAdapter<Boolean?>() {
        @Throws(IOException::class)
        override fun read(reader: JsonReader): Boolean {
            //如果值为null，默认返回false
            if (reader.peek() === JsonToken.NULL) {
                reader.nextNull()
                return false
            }
            if (reader.peek() == JsonToken.STRING) {
                val string = reader.nextString()
                var result = false
                try {
                    result = string.toBoolean()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return result
            }
            return reader.nextBoolean()
        }

        //序列化用到的，这里我们实现默认的代码就行
        @Throws(IOException::class)
        override fun write(writer: JsonWriter, value: Boolean?) {
            writer.value(value)
        }
    }
}