package com.nat.geeklolspring.lolapi.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerksDataResponseDTO {
    private final List<Style> styles;

    @JsonCreator
    public PerksDataResponseDTO(@JsonProperty("styles") List<Style> styles) {
        this.styles = styles;
    }

    @Data
    public static class Style {
        private final String description;
        private final List<Selection> selections;
        private final int style;

        @JsonCreator
        public Style(
                @JsonProperty("description") String description,
                @JsonProperty("selections") List<Selection> selections,
                @JsonProperty("style") int style
        ) {
            this.description = description;
            this.selections = selections;
            this.style = style;
        }
    }

    @Data
    public static class Selection {
        private final int perk;
        private final int var1;
        private final int var2;
        private final int var3;

        @JsonCreator
        public Selection(
                @JsonProperty("perk") int perk,
                @JsonProperty("var1") int var1,
                @JsonProperty("var2") int var2,
                @JsonProperty("var3") int var3
        ) {
            this.perk = perk;
            this.var1 = var1;
            this.var2 = var2;
            this.var3 = var3;
        }
    }
}
