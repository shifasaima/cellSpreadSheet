package com.example.myapplication.controller;

import com.example.myapplication.dto.CellDTO;
import com.example.myapplication.service.CellService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cell")
@RequiredArgsConstructor
@Slf4j
public class CellController {
    private final CellService cellService;

    @GetMapping("/{rowNumber}/{columnName}")
    public ResponseEntity<CellDTO> getCellValue(@PathVariable int rowNumber, @PathVariable String columnName) {
        log.info("Fetching cell value for row: {}, column: {}", rowNumber, columnName);
        return ResponseEntity.ok(cellService.getCellValue(rowNumber, columnName));
    }

    @PostMapping("/{rowNumber}/{columnName}")
    public ResponseEntity<CellDTO> updateCellValue(@PathVariable int rowNumber,
                                                   @PathVariable String columnName,
                                                   @RequestBody String cellValue) {
        log.info("Updating cell value for row: {}, column: {} with value: {}", rowNumber, columnName, cellValue);
        return ResponseEntity.ok(cellService.updateCellValue(rowNumber, columnName, cellValue));
    }


    @GetMapping("/operation")
    public ResponseEntity<Double> performOperation(@RequestParam String operation,
                                                   @RequestParam int startRow,
                                                   @RequestParam String startColumn,
                                                   @RequestParam int endRow,
                                                   @RequestParam String endColumn,
                                                   @RequestParam(required = false) String condition) {
        return ResponseEntity.ok(cellService.performOperation(operation, startRow, startColumn, endRow, endColumn, condition));
    }
}
