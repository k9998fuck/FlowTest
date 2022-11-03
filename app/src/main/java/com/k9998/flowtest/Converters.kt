package com.k9998.flowtest

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.getOrSet

/**
 * https://developer.android.google.cn/reference/kotlin/androidx/room/ColumnInfo?hl=en#typeAffinity:kotlin.Int
 * https://www.sqlite.org/datatype3.html
 * https://www.sqlite.org/lang_datefunc.html
 */
class Converters {

    private val threadLocal = ThreadLocal<SimpleDateFormat>()

    @TypeConverter
    open fun fromTimestamp(value: String?): Date? {
        val simpleDateFormat = threadLocal.getOrSet { SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA) }
        return value?.let { simpleDateFormat.parse(it) }
    }

    @TypeConverter
    open fun dateToTimestamp(date: Date?): String? {
        val simpleDateFormat = threadLocal.getOrSet { SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA) }
        return date?.let { simpleDateFormat.format(it) }
    }
}