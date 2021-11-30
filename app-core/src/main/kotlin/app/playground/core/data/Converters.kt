package app.playground.core.data

import androidx.room.TypeConverter
import java.util.Date

// TODO support RFC date time format
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
