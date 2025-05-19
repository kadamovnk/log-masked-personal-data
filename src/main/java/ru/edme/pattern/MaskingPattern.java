package ru.edme.pattern;

import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MaskingPattern {
    EMAIL("([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)", "***@masked.com"),
    PHONE("(\\+7\\(\\d{3}\\))\\d{3}-\\d{2}-(\\d{2})", "$1***-**-$2"),
    SNILS("(\\d{3})-\\d{3}-\\d{3}-\\d{2}", "$1-***-***-**"),
    PASSPORT_NUMBER("\\b\\d{6}\\b", "******"),
    PASSPORT_SERIES("\\b(\\d{2})\\d{2}\\b", "$1**"),
    PASSPORT_ISSUED_BY("((?:ГУ|ОВД|МВД|ФМС)[^,\n]*)", "$1 ***"),
    SUBDIVISION_CODE("(\\d{3})-(\\d{3})", "$1-***"),
    DATE_DD_MM_YYYY("\\b(\\d{2})[-.](\\d{2})[-.](\\d{4})\\b", "**.**.$3"),
    DATE_YYYY_MM_DD("\\b(\\d{4})[-.](\\d{2})[-.](\\d{2})\\b", "$1-**-**"),
    INN_10_DIGITS("\\b(\\d{2})\\d{6}(\\d{2})\\b", "$1********$2"),
    INN_12_DIGITS("\\b(\\d{2})\\d{8}(\\d{2})\\b", "$1********$2"),
    POSTAL_CODE("\\b\\d{6}\\b", "******"),
    FULL_NAME("\\b[А-ЯЁ][а-яё]+\\b", "***"),
    ADDRESS("(.*)", matchResult -> {
        String address = matchResult.group(1);
        Pattern addressPattern = Pattern.compile(
                "(?<POSTCODE>\\b\\d{6}\\b)" +
                        "|(?<REGION>\\b[^,]*?(?:край|область|регион|республика)\\b[^,]*)" +
                        "|(?<DISTRICT>\\b[^,]*?район\\b[^,]*)" +
                        "|(?<STREET>(?i)(?:ул\\.?|улица|пер\\.?|переулок|проспект|пр-т)\\s*[^,]+)" +
                        "|(?<HOUSE>(?i)(?:д\\.?|дом)\\s*[^,]+)" +
                        "|(?<FLAT>(?i)(?:кв\\.?|квартира)\\s*[^,]+)",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );
        return addressPattern.matcher(address).replaceAll(mr -> {
            Matcher matcher = (Matcher) mr;
            if (matcher.group("POSTCODE") != null) return "******";
            if (matcher.group("REGION") != null) return "*** ***";
            if (matcher.group("DISTRICT") != null) return "*** р-н";
            // Keep the keyword, mask the detail
            return matcher.group().replaceFirst("\\s+.+", " ***");
        });
    });
    
    private final String regex;
    private final Object replacement;
    private final Pattern compiledPattern;
    
    MaskingPattern(String regex, String replacement) {
        this.regex = regex;
        this.replacement = replacement;
        this.compiledPattern = Pattern.compile(regex);
    }
    
    MaskingPattern(String regex, Function<MatchResult, String> replacement) {
        this.regex = regex;
        this.replacement = replacement;
        this.compiledPattern = Pattern.compile(regex);
    }
    
    public String getRegexPattern() {
        return regex;
    }
    
    public Object getReplacement() {
        return replacement;
    }
    
    public Pattern getCompiledPattern() {
        return compiledPattern;
    }
    
    public String applyTo(String input) {
        if (input == null) return null;
        
        if (replacement instanceof String) {
            return compiledPattern.matcher(input).replaceAll((String)replacement);
        } else {
            return compiledPattern.matcher(input)
                    .replaceAll((Function<MatchResult, String>)replacement);
        }
    }
}
