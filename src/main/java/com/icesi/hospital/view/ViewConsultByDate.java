package com.icesi.hospital.view;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ViewConsult
 */
@Data
@NoArgsConstructor
public class ViewConsultByDate<T> {

    private String type;

    @DateTimeFormat(pattern = "yyyy-MM-dd")  
    private Date date;

    private List<T> results;

}