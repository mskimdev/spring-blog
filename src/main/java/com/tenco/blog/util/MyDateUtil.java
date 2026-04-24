package com.tenco.blog.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Date;
import java.sql.Timestamp;

// SRP - 단일 책임의 원칙
// 날짜/시간 관련된 유틸리티 클래스
public class MyDateUtil {

    // 1. TimeStamp 포맷터
    public static String timestampFormat(Timestamp ts){

        // TimesStamp --> Date 형태로 변환
        // Date를 DateFormatUtils.format을 통해 2번째 매개변수로 변환
        return DateFormatUtils.format(new Date(ts.getTime()), "yyyy-MM-dd HH:mm");
    }
}
