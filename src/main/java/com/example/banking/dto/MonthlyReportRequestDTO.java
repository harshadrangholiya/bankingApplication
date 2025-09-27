package com.example.banking.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportRequestDTO {

    private Long customerId;

    private Integer year;

    private Integer month;
}
