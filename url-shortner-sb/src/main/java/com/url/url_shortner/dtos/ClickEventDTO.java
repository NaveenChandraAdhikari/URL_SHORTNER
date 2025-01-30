package com.url.url_shortner.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClickEventDTO {


//    be sure name the variable as it shows in the output
    private LocalDate clickDate;

    private Long count;



}



