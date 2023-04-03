import com.github.nayasis.kotlin.jdbc.type.TypeMapper
import com.github.nayasis.kotlin.jdbc.type.implement.*
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URL
import java.sql.Types
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.util.*
import kotlin.reflect.KClass

enum class JdbcType(
    val code: Int,
    val mapper: TypeMapper<*>,
) {
    TINYINT                 ( Types.TINYINT, ByteMapper() ) ,
    SMALLINT                ( Types.SMALLINT, TINYINT.mapper ) ,
    BIT                     ( Types.BIT, TINYINT.mapper ) ,
    INTEGER                 ( Types.INTEGER, IntMapper() ) ,
    BIGINT                  ( Types.BIGINT,  BigIntegerMapper() ) ,
    FLOAT                   ( Types.FLOAT, FloatMapper() ) ,
    REAL                    ( Types.REAL, BigDecimalMapper() ) ,
    DOUBLE                  ( Types.DOUBLE, DoubleMapper() ) ,
    NUMERIC                 ( Types.NUMERIC, REAL.mapper ) ,
    DECIMAL                 ( Types.DECIMAL, REAL.mapper ) ,
    CHAR                    ( Types.CHAR, CharacterMapper() ) ,
    VARCHAR                 ( Types.VARCHAR, StringMapper() ) ,
    LONGVARCHAR             ( Types.LONGVARCHAR, StringMapper() ) ,
    DATE                    ( Types.DATE, DateMapper() ) ,
    DATETIME                ( Types.TIMESTAMP, DateTimeMapper() ) ,
    TIME                    ( Types.TIME, TimeMapper() ) ,
    TIMESTAMP               ( Types.TIMESTAMP, TimeStampMapper() ) ,
    ARRAY                   ( Types.ARRAY, ArrayMapper() ) ,
    BLOB                    ( Types.BLOB, BlobMapper() ) ,
    BINARY                  ( Types.BINARY, BLOB.mapper ) ,
    VARBINARY               ( Types.VARBINARY, BLOB.mapper ) ,
    LONGVARBINARY           ( Types.LONGVARBINARY, BLOB.mapper ),
    CLOB                    ( Types.CLOB, ClobMapper() ) ,
    BOOLEAN                 ( Types.BOOLEAN, BooleanMapper() ) ,
    NULL                    ( Types.NULL, NullMapper() ) ,
    JAVA_OBJECT             ( Types.JAVA_OBJECT, ObjectMapper() ),
    SYS_REFCURSOR           ( Int.MIN_VALUE + 1001, JAVA_OBJECT.mapper ) , // oracle
    CURSOR                  ( Int.MIN_VALUE + 1002, JAVA_OBJECT.mapper ) , // oracle
    RESULTSET               ( Int.MIN_VALUE + 1003, ResultSetMapper() ) , // oracle
    REF                     ( Types.REF, JAVA_OBJECT.mapper ) ,
    STRUCT                  ( Types.STRUCT, JAVA_OBJECT.mapper ) ,
    OTHER                   ( Types.OTHER, JAVA_OBJECT.mapper ) ,
    DISTINCT                ( Types.DISTINCT, JAVA_OBJECT.mapper ) ,
    REF_CURSOR              ( Types.REF_CURSOR, JAVA_OBJECT.mapper ) ,
    DATALINK                ( Types.DATALINK, UrlMapper() ) ,
    ROWID                   ( Types.ROWID, RowIdMapper() ) ,
    NVARCHAR                ( Types.NVARCHAR, NStringMapper() ) ,
    NCHAR                   ( Types.NCHAR, NVARCHAR.mapper ) ,
    LONGNVARCHAR            ( Types.LONGNVARCHAR, BLOB.mapper ) ,
    NCLOB                   ( Types.NCLOB, NClobMapper() ) ,
    SQLXML                  ( Types.SQLXML, SqlXmlMapper() ) ,
    TIME_WITH_TIMEZONE      ( Types.TIME_WITH_TIMEZONE, OffsetTimeMapper() ) ,
    TIMESTAMP_WITH_TIMEZONE ( Types.TIMESTAMP_WITH_TIMEZONE, OffsetDateTimeMapper() ) ,
    TEXT                    ( Int.MIN_VALUE + 1004, VARCHAR.mapper ) ,
    LONGTEXT                ( Int.MIN_VALUE + 1005, CLOB.mapper ) ,
    DATETIMEOFFSET          (-155, OffsetDateTimeMapper() ) , // SQL Server 2008
    UNDEFINED               ( Int.MIN_VALUE + 9999, UndefinedMapper() ) ,
    ;

    companion object {

        private val byName = values().associateBy { it.name }
        private val byCode = values().associateBy { it.code }
        private val byClass = mapOf(
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

        fun of(code: Int): JdbcType = byCode[code] ?: UNDEFINED

        fun of(name: String): JdbcType = byName[name] ?: UNDEFINED

        fun of(klass: KClass<*>?): JdbcType? = byClass[klass]

    }

}


