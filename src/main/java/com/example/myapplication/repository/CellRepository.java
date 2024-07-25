package com.example.myapplication.repository;

import com.example.myapplication.entity.CellEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CellRepository extends JpaRepository<CellEntity, Long> {
    Optional<CellEntity> findByRowNumberAndColumnName(int rowNumber, String columnName);
    List<CellEntity> findByRowNumberBetweenAndColumnNameBetween(int startRow, int endRow, String startColumn, String endColumn);
}
