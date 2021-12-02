package com.example.pact;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
public class DateService {

    public DateResponse getValidDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            return DateResponse.builder()
                    .givenDate(localDate.toString())
                    .year(localDate.getYear())
                    .month(localDate.getMonth().getValue())
//                    .month("Feb")
                    .day(localDate.getDayOfMonth())
                    .isValidDate(true)
                    .message("date parsed successfully")
                    .meta("some useful info")
                    .build();
        } catch (DateTimeParseException e) {
            final DateResponse dateResponse = new DateResponse();
            dateResponse.setIsValidDate(false);
            dateResponse.setGivenDate(date);
            dateResponse.setMessage(date + " is not a valid date format");
            return dateResponse;
        }
    }
}
