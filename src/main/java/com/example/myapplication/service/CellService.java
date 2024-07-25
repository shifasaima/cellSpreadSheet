package com.example.myapplication.service;

import com.example.myapplication.dto.CellDTO;

public interface CellService {
    CellDTO getCellValue(int rowNumber, String columnName);
    CellDTO updateCellValue(int rowNumber, String columnName, String cellValue);
    double performOperation(String operation, int startRow, String startColumn, int endRow, String endColumn, String condition);
}
