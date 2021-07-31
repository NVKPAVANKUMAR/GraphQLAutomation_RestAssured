package com.qa.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SpaceXUser {
    private UUID id;

    private String name;

    private String rocket;

}
