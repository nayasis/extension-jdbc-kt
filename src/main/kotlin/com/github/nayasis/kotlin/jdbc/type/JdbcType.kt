package com.github.nayasis.kotlin.jdbc.type

import com.github.nayasis.kotlin.jdbc.type.implement.*
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URL
import java.sql.RowId
import java.sql.Types
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.util.*
import kotlin.reflect.KClass

@Suppress("SpellCheckingInspection")
enum class JdbcType(
    val code: Int,
    val mapper: TypeMapper<*>,
) {
    TINYINT                 ( Types.TINYINT                 , ByteMapper           ) ,
    SMALLINT                ( Types.SMALLINT                , ByteMapper           ) ,
    BIT                     ( Types.BIT                     , ByteMapper           ) ,
    INTEGER                 ( Types.INTEGER                 , IntMapper            ) ,
    BIGINT                  ( Types.BIGINT                  , BigIntegerMapper     ) ,
    FLOAT                   ( Types.FLOAT                   , FloatMapper          ) ,
    REAL                    ( Types.REAL                    , BigDecimalMapper     ) ,
    DOUBLE                  ( Types.DOUBLE                  , DoubleMapper         ) ,
    NUMERIC                 ( Types.NUMERIC                 , DoubleMapper         ) ,
    DECIMAL                 ( Types.DECIMAL                 , BigDecimalMapper     ) ,
    CHAR                    ( Types.CHAR                    , CharacterMapper      ) ,
    VARCHAR                 ( Types.VARCHAR                 , StringMapper         ) ,
    LONGVARCHAR             ( Types.LONGVARCHAR             , StringMapper         ) ,
    DATE                    ( Types.DATE                    , DateMapper           ) ,
    DATETIME                ( Types.TIMESTAMP               , DateTimeMapper       ) ,
    TIME                    ( Types.TIME                    , TimeMapper           ) ,
    TIMESTAMP               ( Types.TIMESTAMP               , TimeStampMapper      ) ,
    ARRAY                   ( Types.ARRAY                   , ArrayMapper          ) ,
    BLOB                    ( Types.BLOB                    , BlobMapper           ) ,
    BINARY                  ( Types.BINARY                  , BlobMapper           ) ,
    VARBINARY               ( Types.VARBINARY               , BlobMapper           ) ,
    LONGVARBINARY           ( Types.LONGVARBINARY           , BlobMapper           ) ,
    CLOB                    ( Types.CLOB                    , ClobMapper           ) ,
    BOOLEAN                 ( Types.BOOLEAN                 , BooleanMapper        ) ,
    NULL                    ( Types.NULL                    , NullMapper           ) ,
    JAVA_OBJECT             ( Types.JAVA_OBJECT             , ObjectMapper         ) ,
    REF                     ( Types.REF                     , ObjectMapper         ) ,
    STRUCT                  ( Types.STRUCT                  , ObjectMapper         ) ,
    OTHER                   ( Types.OTHER                   , ObjectMapper         ) ,
    DISTINCT                ( Types.DISTINCT                , ObjectMapper         ) ,
    REF_CURSOR              ( Types.REF_CURSOR              , ResultSetMapper      ) ,
    DATALINK                ( Types.DATALINK                , UrlMapper            ) ,
    ROWID                   ( Types.ROWID                   , RowIdMapper          ) ,
    NVARCHAR                ( Types.NVARCHAR                , NStringMapper        ) ,
    NCHAR                   ( Types.NCHAR                   , NStringMapper        ) ,
    LONGNVARCHAR            ( Types.LONGNVARCHAR            , BlobMapper           ) ,
    NCLOB                   ( Types.NCLOB                   , NClobMapper          ) ,
    SQLXML                  ( Types.SQLXML                  , SqlXmlMapper         ) ,
    TIME_WITH_TIMEZONE      ( Types.TIME_WITH_TIMEZONE      , OffsetTimeMapper     ) ,
    TIMESTAMP_WITH_TIMEZONE ( Types.TIMESTAMP_WITH_TIMEZONE , OffsetDateTimeMapper ) ,
    DATETIMEOFFSET          (-155                     , OffsetDateTimeMapper       ) , // SQL Server 2008
    SYS_REFCURSOR           ( Int.MIN_VALUE + 1       , ResultSetMapper            ) , // oracle
    CURSOR                  ( Int.MIN_VALUE + 2       , ResultSetMapper            ) , // oracle
    RESULT_SET              ( Int.MIN_VALUE + 3       , ResultSetMapper            ) , // oracle
    ORACLE_RESULT_SET       ( Int.MIN_VALUE + 4       , ResultSetMapper            ) , // oracle
    TEXT                    ( Int.MIN_VALUE + 5       , StringMapper               ) ,
    LONGTEXT                ( Int.MIN_VALUE + 6       , ClobMapper                 ) ,
    UNDEFINED               ( Int.MIN_VALUE + 0       , UndefinedMapper            ) ,

    ;

    companion object {

        private val typeByName = values().associateBy { it.name }
        private val typeByCode = values().associateBy { it.code }
        private val typeByClass = mapOf(
            String::class to VARCHAR,
            StringBuilder::class to VARCHAR,
            StringBuffer::class to VARCHAR,
            Char::class to CHAR,
            Boolean::class to BOOLEAN,
            Int::class to INTEGER,
            Double::class to DOUBLE,
            Float::class to FLOAT,
            Byte::class to TINYINT,
            Short::class to SMALLINT,
            Long::class to BIGINT,
            BigInteger::class to BIGINT,
            BigDecimal::class to NUMERIC,
            ByteArray::class to BLOB,
            Date::class to TIMESTAMP,
            Calendar::class to TIMESTAMP,
            java.sql.Date::class to DATE,
            LocalDate::class to DATE,
            LocalDateTime::class to DATETIME,
            LocalTime::class to TIME,
            OffsetDateTime::class to TIMESTAMP_WITH_TIMEZONE,
            OffsetTime::class to TIMESTAMP_WITH_TIMEZONE,
            java.sql.Time::class to TIME,
            java.sql.Timestamp::class to TIMESTAMP,
            URL::class to DATALINK,
        )

        private val mapperByClass = mapOf(
            String::class to StringMapper,
            StringBuilder::class to CharSequenceMapper,
            StringBuffer::class to CharSequenceMapper,
            Char::class to CharacterMapper,
            Boolean::class to BooleanMapper,
            Int::class to IntMapper,
            Double::class to DoubleMapper,
            Float::class to FloatMapper,
            Byte::class to ByteMapper,
            Short::class to ShortMapper,
            Long::class to LongMapper,
            BigInteger::class to BigIntegerMapper,
            BigDecimal::class to BigDecimalMapper,
            ByteArray::class to ByteMapper,
            Date::class to DateMapper,
            Calendar::class to CalendarMapper,
            LocalDate::class to LocalDateMapper,
            LocalDateTime::class to LocalDateTimeMapper,
            LocalTime::class to LocalTimeMapper,
            URL::class to UrlMapper,
            RowId::class to RowIdMapper
        )

        fun of(code: Int): JdbcType = typeByCode[code] ?: UNDEFINED

        fun of(name: String): JdbcType = typeByName[name.uppercase()] ?: UNDEFINED

        fun of(klass: KClass<*>?): JdbcType? = typeByClass[klass]

        fun mapper(klass: KClass<*>?): TypeMapper<out Any> = mapperByClass[klass] ?: ObjectMapper

    }

}


