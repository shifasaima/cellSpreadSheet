package com.example.myapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellDTO {
    private int rowNumber;
    private String columnName;
    private String cellValue;
}
