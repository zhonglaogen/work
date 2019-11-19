package com.rd.hcb.procon;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zlx
 * @date 2019-11-01 16:22
 */
public class AllUtils {

    //模拟浏览器客户端
    public static String getRandomUserAgent() {
        List<String> userAgents = new ArrayList<String>();
        // 360/chrome
        userAgents.add(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36");
        // Safari mac
        userAgents.add(
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        // Safari windows
        userAgents.add(
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        // IE9
        userAgents.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0");
        // 火狐mac
        userAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv,2.0.1) Gecko/20100101 Firefox/4.0.1");
        // 火狐windows
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; rv,2.0.1) Gecko/20100101 Firefox/4.0.1");
        // 遨游
        userAgents.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Maxthon 2.0)");
        // 搜狗
        userAgents.add(
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)");
        Random random = new Random();
        int randomNum = random.nextInt(userAgents.size());
        return userAgents.get(randomNum);
    }

    /**
     * 获取近七天的数据
     *
     * @param datesCount 相差的天数
     * @return
     */
    public static void findSevenData(JSONArray datas, int datesCount) throws Exception {
        if (datas == null) {
            throw new Exception("json 数组为空，无法计算近七天内的数据");
        }
        //线程安全的移除list中的数据
        Iterator<Object> iterator = datas.iterator();
        while (iterator.hasNext()) {
            JSONObject jsonObject = (JSONObject) iterator.next();
            String createTime = jsonObject.getString("createTime");

            //格式化日期，方便比较
            Date createDate = getFormatTime(createTime, "yyyy-MM-dd HH:mm:ss");
            //小于0，小于当前日期
            //与当前日期-7作比较，如果小于，就remove掉
            int subtraction = createDate.compareTo(AllUtils.currentDate(datesCount));
            if (subtraction < 0) {
                iterator.remove();
            }
        }

    }

    /**
     * 获得当天日期-dates的日期
     *
     * @param dates 相差天数
     * @return
     */
    public static Date currentDate(int dates) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, dates);
        return calendar.getTime();

    }


    /**
     * 将字符串转换为Date
     *
     * @param time    时间字符串
     * @param pattern 字符串格式
     * @return
     * @throws ParseException
     */
    public static Date getFormatTime(String time, String pattern) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.parse(time);


    }




}
