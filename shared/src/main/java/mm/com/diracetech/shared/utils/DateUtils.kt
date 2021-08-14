package mm.com.diracetech.shared.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    val dd_MM_YYYY = "dd/MM/yyyy"
    val yyyy_MM_dd__G_at_HH_mm_ss__z = "yyyy.MM.dd G 'at' HH:mm:ss z" //2001.07.04 AD at 12:08:56 PDT
    val h_mm__a = "h:mm a"

    fun getDateFromMilliSeconds(milliSeconds: Long): String {
        val simpleDateFormat = SimpleDateFormat(dd_MM_YYYY, Locale.US)
        val result = Date(milliSeconds)
        return simpleDateFormat.format(result)
    }

    /**
     * Expected input = yyyy-mm-dd hh:mm:ss
     */
    fun getFormattedDateFromTimeStamp(inputDate: String?, format: String = dd_MM_YYYY): String {
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
            val date = simpleDateFormat.parse(inputDate)

            val formatDateForBooking = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            return formatDateForBooking.format(date)
        } catch (e: ParseException) {
            return ""
        }

    }

    /**
     * Expected input = yyyy-mm-dd hh:mm:ss
     * Expected output = 123456789
     */
    fun getTimestampFromDate(inputDate: String?): Long {
        if (inputDate == null) {
            return 0
        }
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
        val date = simpleDateFormat.parse(inputDate)

        return date.time
    }

    fun getTodayDateByFormat(format: String): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.US)
        val calendar = GregorianCalendar()
        return simpleDateFormat.format(calendar.time)
    }

    /*
    * Expected Input Format : 2018-09-04 00:00:00
    * Expected Output Format: 20180904000000
    * */
    fun formatServerDateStructureToInt(date: String): Long {
        return try {
            val value = date.replace("-", "").replace(":", "").replace(" ", "")
            value.toLong()
        } catch (e: NumberFormatException) {
            0
        }
    }
}