package com.github.nayasis.kotlin.jdbc.query

import com.github.nayasis.kotlin.basica.reflection.Reflector

open class BatchQuery(
    private val queryBase: QueryBase
) {

    @Suppress("PrivatePropertyName")
    private val params_back = ArrayList<Map<Int,BindParam>>()

    private val paramIndices = queryBase.paramStructs.mapIndexed { index, bindStruct ->
        bindStruct.key to index
    }.groupBy({ it.first }, {it.second})

    val params: List<Map<Int,BindParam>>
        get() = params_back

    val paramStructs: List<BindStruct>
        get() = queryBase.paramStructs

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
            val originalKeys = queryBase.paramStructs.map { it.key }.toMutableSet()
            val bindKeys = bindParams.map { it.value.key }.toSet()
            originalKeys.removeAll(bindKeys)
            throw IllegalArgumentException("Some keys are missing ($originalKeys)")
        }
        params_back.add(bindParams)
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
        params_back.clear()
        return this
    }

    val preparedQuery: String
        get() = queryBase.queries.joinToString("?")

    override fun toString(): String {
        return StringBuilder()
            .append(">> parameter count: ${params_back.size}")
            .append(">> query\n${queryBase.queries.joinToString("?")}")
            .toString()
    }

}

fun String.toBatchQuery(): BatchQuery = BatchQuery(this)

