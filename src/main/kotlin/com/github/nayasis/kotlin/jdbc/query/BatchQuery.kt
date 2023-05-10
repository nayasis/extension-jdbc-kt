package com.github.nayasis.kotlin.jdbc.query

import com.github.nayasis.kotlin.basica.reflection.Reflector

open class BatchQuery(
    private val queryBase: QueryBase
) {

    @Suppress("PrivatePropertyName")
    private val bindParams = ArrayList<Map<Int,BindParam>>()

    private val paramIndices = queryBase.paramIndices

    constructor(sql: String) : this(QueryBase(sql))

    fun addParam(vo: Any): BatchQuery {
        val param = try {
            Reflector.toMap(vo)
        } catch (e: Exception) {
            throw TypeCastException("Parameter muse be value object")
        }
        return addParam(param)
    }

    fun addParam(param: Map<String,Any?>): BatchQuery {
        val bindParams = HashMap<Int,BindParam>()
        param.forEach { (key, value) ->
            paramIndices[key]?.forEach { i ->
                val struct = queryBase.paramStructs[i]
                bindParams[i] = BindParam(value, struct)
            }
        }
        if(bindParams.size < queryBase.paramStructs.size) {
            val keysInQuery = queryBase.paramStructs.map { it.key }.toMutableSet()
            val keysInParam = bindParams.map { it.value.key }.toSet()
            keysInQuery.removeAll(keysInParam)
            throw IllegalArgumentException("Some keys are missing. ($keysInQuery)")
        }
        this.bindParams.add(bindParams)
        return this
    }

    @JvmName("addParamsViaVo")
    fun addParams(params: Collection<Any>): BatchQuery {
        params.forEach { addParam(it) }
        return this
    }

    fun addParams(params: Collection<Map<String,Any?>>): BatchQuery {
        params.forEach { addParam(it) }
        return this
    }

    fun reset(): BatchQuery {
        bindParams.clear()
        return this
    }

    val preparedQuery: String
        get() = queryBase.queries.joinToString("?")

    val preparedParams: List<Map<Int,BindParam>>
        get() = bindParams

    override fun toString(): String {
        return StringBuilder()
            .append(">> batch parameter count: ${bindParams.size}")
            .append(">> batch query\n$preparedQuery")
            .toString()
    }

}

fun String.toBatchQuery(): BatchQuery = BatchQuery(this)

