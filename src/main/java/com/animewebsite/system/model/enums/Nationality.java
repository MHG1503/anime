package com.animewebsite.system.model.enums;

public enum Nationality {
    JAPANESE("JP"),
    ENGLISH("EN"),
    GERMAN("DE"),
    SPANISH("ES"),
    ITALIAN("IT"),
    PORTUGUESE("PT"),
    RUSSIAN("RU"),
    CHINESE("CN"),
    KOREAN("KR"),
    ARABIC("AR"),
    HINDI("HI"),
    TURKISH("TR"),
    DUTCH("NL"),
    SWEDISH("SE"),
    DANISH("DK"),
    NORWEGIAN("NO"),
    FINNISH("FI"),
    GREEK("EL"),
    HUNGARIAN("HU"),
    POLISH("PL"),
    CZECH("CZ"),
    ROMANIAN("RO"),
    THAI("TH"),
    VIETNAMESE("VN"),
    INDONESIAN("ID"),
    MALAY("MY"),
    FILIPINO("PH"),
    SWAHILI("SW");

    private final String code;

    Nationality(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
