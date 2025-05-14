package ru.edme.other;

import ru.edme.annotation.SensitiveField;

import java.time.LocalDate;

public class Masking {
    public static String mask(String input) {
        if (input == null) return null;
        
        // Email
        input = input.replaceAll("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+", "***@masked.com");
        
        // Phone number (e.g., +7(918)140-54-69 → +7(918)***-**-69)
        input = input.replaceAll("(\\+7\\(\\d{3}\\))\\d{3}-\\d{2}-(\\d{2})", "$1***-**-$2");
        
        // SNILS (e.g. 314-565-256-20 → 314-***-***-**)
        input = input.replaceAll("(\\d{3})-\\d{3}-\\d{3}-\\d{2}", "$1-***-***-**");
        
        // Passport number (6 digits → ******)
        input = input.replaceAll("\\b\\d{6}\\b", "******");
        
        // Passport series (4 digits → 56**)
        input = input.replaceAll("\\b(\\d{2})\\d{2}\\b", "$1**");
        
        // Issued by (e.g. ГУ МВД РОССИИ ПО... → ГУ МВД ***)
        input = input.replaceAll("((?:ГУ|ОВД|МВД|ФМС)[^,\n]*)", "$1 ***");
        
        // Subdivision code (e.g. 023-230 → 023-***)
        input = input.replaceAll("(\\d{3})-(\\d{3})", "$1-***");
        
        // Mask dates in dd.MM.yyyy format
        input = input.replaceAll("\\b\\d{2}\\.\\d{2}\\.(\\d{4})\\b", "**.**.$1");
        
        // Mask dates in yyyy-MM-dd format
        input = input.replaceAll("\\b(\\d{4})-(\\d{2})-(\\d{2})\\b", "$1-**-**");
        
        // INN 10 digits (юрлица): 1020304050 → 10********50
        input = input.replaceAll("\\b(\\d{2})\\d{6}(\\d{2})\\b", "$1********$2");
        
        // INN 12 digits (физлица): 607080901000 → 60********00
        input = input.replaceAll("\\b(\\d{2})\\d{8}(\\d{2})\\b", "$1********$2");
        
        // Postal code (e.g. 123456 → ******)
        input = input.replaceAll("\\b\\d{6}\\b", "******");
        
        // Address masking (e.g. улица, дом, квартира)
        input = input.replaceAll("(ул\\.|улица|пер\\.|проспект|пр-т|д\\.|дом|кв\\.|квартира)\\s+[^,\n]+", "$1 ***");
        
        // Full name (Фамилия, Имя, Отчество) – catches simple capitalized words in Russian
        input = input.replaceAll("\\b[А-ЯЁ][а-яё]+\\b", "***");
        
        return input;
    }
    
    public static void maskSensitiveFields(Object obj) {
        try {
            for (var field : obj.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(SensitiveField.class)) {
                    field.setAccessible(true);
                    Object val = field.get(obj);
                    if (val instanceof String) {
                        field.set(obj, mask((String) val));
                    } else if (val instanceof LocalDate) {
                        // Keep the year but mask the month and day
                        LocalDate date = (LocalDate) val;
                        field.set(obj, LocalDate.of(date.getYear(), 1, 1));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
