package ru.edme.model;

public enum MaskingPattern {
    EMAIL("([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)", "***@masked.com"),
    PHONE("(\\+7\\(\\d{3}\\))\\d{3}-\\d{2}-(\\d{2})", "$1***-**-$2"),
    SNILS("(\\d{3})-\\d{3}-\\d{3}-\\d{2}", "$1-***-***-**"),
    PASSPORT_NUMBER("\\b\\d{6}\\b", "******"),
    PASSPORT_SERIES("\\b(\\d{2})\\d{2}\\b", "$1**"),
    ISSUED_BY("((?:ГУ|ОВД|МВД|ФМС)[^,\n]*)", "$1 ***"),
    SUBDIVISION_CODE("(\\d{3})-(\\d{3})", "$1-***"),
    DATE_DD_MM_YYYY("\\b\\d{2}\\.\\d{2}\\.(\\d{4})\\b", "**.**.$1"),
    DATE_YYYY_MM_DD("\\b(\\d{4})-(\\d{2})-(\\d{2})\\b", "$1-**-**"),
    INN_10_DIGITS("\\b(\\d{2})\\d{6}(\\d{2})\\b", "$1********$2"),
    INN_12_DIGITS("\\b(\\d{2})\\d{8}(\\d{2})\\b", "$1********$2"),
    POSTAL_CODE("\\b\\d{6}\\b", "******"),
    ADDRESS("(ул\\.|улица|пер\\.|проспект|пр-т|д\\.|дом|кв\\.|квартира)\\s+[^,\n]+", "$1 ***"),
    FULL_NAME("\\b[А-ЯЁ][а-яё]+\\b", "***");
    
    private final String regex;
    private final String replacement;
    
    MaskingPattern(String regex, String replacement) {
        this.regex = regex;
        this.replacement = replacement;
    }
    
    public String getMaskedValue(String input) {
        if (input == null) return null;
        return input.replaceAll(regex, replacement);
    }
}
