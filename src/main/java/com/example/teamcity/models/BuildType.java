package com.example.teamcity.models;

import com.example.teamcity.annotations.Optional;
import com.example.teamcity.annotations.Parametrizable;
import com.example.teamcity.annotations.Random;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class BuildType extends BaseModel {
    @Random
    private String id;
    @Random
    private String name;
    @Parametrizable
    private Project project;
    @Optional
    private Steps steps;
}
