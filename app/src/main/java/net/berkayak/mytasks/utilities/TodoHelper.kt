package net.berkayak.mytasks.utilities

import java.text.SimpleDateFormat
import java.util.*

class TodoHelper {
    companion object TodoHelper{

        fun getDateTime(date: Long?): String {
            if (date == null)
                return SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().time)
            else {
                return SimpleDateFormat("dd-MM-yyyy").format(date)
            }
        }

        fun getDateTimeAsLong(date: String?): Long {
            if (date == null)
                return Calendar.getInstance().timeInMillis
            else{
                val date = SimpleDateFormat("dd-MM-yyyy").parse(date)
                return date.time
            }
        }
    }
}