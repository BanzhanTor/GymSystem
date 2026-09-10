package gym.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类：封装日期的格式化、计算与比较，体现常用类 Date 的使用。
 */
public class DateUtil {

    private static final String DATE_FMT = "yyyy-MM-dd";
    private static final String DATE_TIME_FMT = "yyyy-MM-dd HH:mm:ss";

    private DateUtil() {
    }

    /** 获取当前日期，格式 yyyy-MM-dd */
    public static String currentDate() {
        return new SimpleDateFormat(DATE_FMT).format(new Date());
    }

    /** 获取当前日期时间，格式 yyyy-MM-dd HH:mm:ss */
    public static String currentDateTime() {
        return new SimpleDateFormat(DATE_TIME_FMT).format(new Date());
    }

    /**
     * 在指定日期字符串上增加 days 天，返回新的日期字符串。
     */
    public static String addDays(String dateStr, int days) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT);
            Date date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, days);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            return dateStr;
        }
    }

    /**
     * 计算目标日期距离今天还有多少天（负数表示已过期）。
     */
    public static long daysUntil(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT);
            Date target = sdf.parse(dateStr);
            Date today = sdf.parse(sdf.format(new Date()));
            long diff = target.getTime() - today.getTime();
            return diff / (1000L * 60 * 60 * 24);
        } catch (Exception e) {
            return 0;
        }
    }
}