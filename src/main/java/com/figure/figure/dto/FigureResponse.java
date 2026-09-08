package com.figure.figure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FigureResponse {
        private Long id;
        private String name;
        private String manufacturerName;
        private List<String> characterNames;
}
