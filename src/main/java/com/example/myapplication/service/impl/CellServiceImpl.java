package com.example.myapplication.service.impl;

import com.example.myapplication.dto.CellDTO;
import com.example.myapplication.entity.CellEntity;
import com.example.myapplication.repository.CellRepository;
import com.example.myapplication.service.CellService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CellServiceImpl implements CellService {
    private final CellRepository cellRepository;
    private final ModelMapper modelMapper;

    @Override
    public CellDTO getCellValue(int rowNumber, String columnName) {
        log.info("Getting value for cell at row: {}, column: {}", rowNumber, columnName);
        CellEntity cell = cellRepository.findByRowNumberAndColumnName(rowNumber, columnName)
                .orElse(new CellEntity(null, rowNumber, columnName, ""));
        return modelMapper.map(cell, CellDTO.class);
    }

    @Override
    @Transactional
    public CellDTO updateCellValue(int rowNumber, String columnName, String cellValue) {
        log.info("Updating cell at row: {}, column: {} with value: {}", rowNumber, columnName, cellValue);
        CellEntity cell = cellRepository.findByRowNumberAndColumnName(rowNumber, columnName)
                .orElse(new CellEntity(null, rowNumber, columnName, ""));
        cell.setCellValue(cellValue);
        cellRepository.save(cell);
        return modelMapper.map(cell, CellDTO.class);
    }

    @Override
    public double performOperation(String operation, int startRow, String startColumn, int endRow, String endColumn, String condition) {
        log.info("Performing {} operation from row: {}, column: {} to row: {}, column: {}", operation, startRow, startColumn, endRow, endColumn);
        List<CellEntity> cells = cellRepository.findByRowNumberBetweenAndColumnNameBetween(startRow, endRow, startColumn, endColumn);
        return  cells.stream()
                .mapToDouble(cell -> {
                    try {
                        double value = Double.parseDouble(cell.getCellValue());
                        if(checkCondition(value, condition)) {
                            return Double.parseDouble(cell.getCellValue());
                        }
                    } catch (NumberFormatException e) {
                        log.warn("Invalid number format in cell at row: {}, column: {}", cell.getRowNumber(), cell.getColumnName());
                        return 0.0;
                    }
                    return 0.0;
                })
                .reduce((op1, op2) -> {
                    switch (operation.toLowerCase()) {
                        case "add":
                            return op1+op2;
                        case "subtract":
                            return op1-op2;
                        case "multiply":
                            return op1*op2;
                        default:
                            throw new IllegalArgumentException("Invalid operation");
                    }
                }).orElse(0.0);
    }

    private boolean checkCondition(double value, String condition) {
        if(condition == null || condition.isEmpty()) return true;

        String[] parts = condition.split(" ");
        if(parts.length !=2) return false;

        String operator = parts[0].trim();
        double conditionValue;
        try {
            conditionValue = Double.parseDouble(parts[1].trim());
        } catch (NumberFormatException e) {
            return false;
        }
        switch (operator) {
            case "<" :
                return value < conditionValue;
            case ">" :
                return value > conditionValue;
            case "==" :
                return value == conditionValue;
            default:
                return false;
        }
    }
}
