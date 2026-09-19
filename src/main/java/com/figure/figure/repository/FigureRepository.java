package com.figure.figure.repository;

import com.figure.figure.model.Figure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FigureRepository extends JpaRepository<Figure, Long> {

    List<Figure> findByNameContaining(String keyword);

    boolean existsByManufacturer_Id(Long manufacturerId);

    @Query("""

            SELECT f
                FROM Figure f
                JOIN FETCH f.manufacturer
                WHERE (
            :keyword = ''
            OR f.name LIKE CONCAT('%', :keyword, '%')
            OR EXISTS (
                SELECT fc.id
                FROM FigureCharacter fc
                WHERE fc.figure = f
                  AND fc.character.name LIKE CONCAT('%', :keyword, '%')
            )
        )
        AND (
            :manufacturerId IS NULL
            OR f.manufacturer.id = :manufacturerId
        )
        """)
    List<Figure> findByFilters(
            @Param("keyword") String keyword,
            @Param("manufacturerId") Long manufacturerId
    );
}