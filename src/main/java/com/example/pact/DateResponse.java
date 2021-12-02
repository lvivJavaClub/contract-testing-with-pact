package com.example.pact;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateResponse {
    private String givenDate;
    private int year;
    private int month;
//    private String month;
    private int day;
    private Boolean isValidDate = false;
    private String message;
    private String data;
    private String meta;
}
