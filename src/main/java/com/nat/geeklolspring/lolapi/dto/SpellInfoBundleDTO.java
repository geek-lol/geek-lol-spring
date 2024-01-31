package com.nat.geeklolspring.lolapi.dto;

import lombok.Data;

@Data
public class SpellInfoBundleDTO {
    private String name;
    private String description;
    private String cooldownBurn;
    private int summnerLevel;
    private String imageUrl;
}
