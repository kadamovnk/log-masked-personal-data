package ru.edme.demo;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import ru.edme.model.Address;
import ru.edme.model.Person;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class Demo {
    private final Logging logging;
    
    @PostConstruct
    public void build() {
        Person person = new Person();
        Address address = new Address();
        person.setFirstName("Галина");
        person.setLastName("Искрева");
        person.setMiddleName("Петровна");
        person.setBirthDate(LocalDate.of(2000, 12, 20));
        person.setPassportSeries("5657");
        person.setPassportNumber("656565");
        person.setPassportIssuedBy("ГУ МВД РОССИИ ПО КРАСНОДАРСКОМУ КРАЮ");
        person.setPassportIssuedDate(LocalDate.of(2015, 11,8));
        person.setPassportExpiryDate(LocalDate.of(2021, 11, 8));
        person.setPassportSubdivisionCode("023-230");
        person.setAddress("123456, Российская Федерация, Краснодарский край, Темрюкский район, ул. Ленина, д. 4, кв. 22");
        person.setPhone("+7(918)140-54-69");
        person.setEmail("boor_yonk@mail.ru");
        person.setInn("607080901000");
        person.setSnils("314-565-256-20");
        person.setStringDate("2023-10-20 12:00:00, 10.10.2022, 10-10-2020, 2021.10.10");
        address.setApartment("д. 4");
        address.setHouse("кв. 22");
        address.setStreet("ул. Ленина");
        person.setAddresss(address);
        
        logging.log(person);
        
        System.exit(0);
    }
}
