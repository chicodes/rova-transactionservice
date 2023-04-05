package com.rova.transactionService.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@Service
@Slf4j
public class Utility {
    public static Date getCurrentDate(){
        try {
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            return simpleDateFormat.parse(dateTimeFormat.format(now));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
