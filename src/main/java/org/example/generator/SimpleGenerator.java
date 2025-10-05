package org.example.generator;

import org.example.MedicalAppointment;
import org.example.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.example.Util.DoctorSpecialty;
import org.example.Util.DoctorInfo;
import org.example.Util.AppointmentStatus;

public class SimpleGenerator {

    private static final String[] FIRST_NAMES = {"Иван", "Мария", "Петр", "Анна", "Сергей", "Ольга", "Алексей", "Елена", "Максим","Аллександра"};
    private static final String[] LAST_NAMES = {"Фардзинов", "Петрова", "Сидоров", "Смирнова", "Кузнецов", "Попова", "Попов", "Кирякова", "Иванов","Смирнова"};
    private static final String[] COMMON_SYMPTOMS = {"головная боль", "температура", "кашель", "боль в животе",
            "головокружение", "слабость", "боль в груди", "тошнота"};
    private static final String[] DEPARTMENTS = {"Терапевтическое", "Хирургическое", "Неврологическое", "Кардиологическое"};

    private final Random random = new Random();
    private final Set<Long> usedIds = new HashSet<>();

    public List<MedicalAppointment> generateAppointments(int count) {
        List<MedicalAppointment> appointments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            appointments.add(generateAppointment());
        }
        return appointments;
    }

    private MedicalAppointment generateAppointment() {
        long appointmentId = generateUniqueId();
        String patientName = generatePatientName();
        LocalDateTime appointmentDateTime = generateDateTime();
        DoctorSpecialty specialty = generateSpecialty();
        Util.DoctorInfo doctorInfo = generateDoctorInfo();
        List<String> symptoms = generateSymptoms();
        AppointmentStatus status = generateStatus();
        double consultationFee = generateFee(specialty);

        return new MedicalAppointment(appointmentId, patientName, appointmentDateTime,
                specialty, doctorInfo, symptoms, status, consultationFee);
    }

    private long generateUniqueId() {
        long id;
        do {
            id = 100000 + random.nextInt(900000);
        } while (usedIds.contains(id));
        usedIds.add(id);
        return id;
    }

    private String generatePatientName() {
        int randomValue = random.nextInt(FIRST_NAMES.length);
        return FIRST_NAMES[randomValue] + " " +
                LAST_NAMES[randomValue];
    }

    private LocalDateTime generateDateTime() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        long randomDay = ThreadLocalRandom.current().nextLong(startDate.toEpochDay(), endDate.toEpochDay());
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);

        int hour = 8 + random.nextInt(12);
        int minute = random.nextInt(4) * 15;

        return LocalDateTime.of(randomDate, java.time.LocalTime.of(hour, minute));
    }

    private DoctorSpecialty generateSpecialty() {
        return DoctorSpecialty.values()[random.nextInt(DoctorSpecialty.values().length)];
    }

    private DoctorInfo generateDoctorInfo() {
        String licenseNumber = "LIC" + (1000 + random.nextInt(9000));
        int yearsOfExperience = 1 + random.nextInt(30);
        String department = DEPARTMENTS[random.nextInt(DEPARTMENTS.length)];
        return new DoctorInfo(licenseNumber, yearsOfExperience, department);
    }

    private List<String> generateSymptoms() {
        List<String> selectedSymptoms = new ArrayList<>();
        int symptomCount = 1 + random.nextInt(3);
        for (int i = 0; i < symptomCount; i++) {
            String symptom;
            do {
                symptom = COMMON_SYMPTOMS[random.nextInt(COMMON_SYMPTOMS.length)];
            } while (selectedSymptoms.contains(symptom));
            selectedSymptoms.add(symptom);
        }
        return selectedSymptoms;
    }

    private AppointmentStatus generateStatus() {
        return AppointmentStatus.values()[random.nextInt(AppointmentStatus.values().length)];
    }

    private double generateFee(DoctorSpecialty specialty) {
        return switch (specialty) {
            case SURGEON -> 2000 + random.nextInt(3000);
            case CARDIOLOGIST -> 1800 + random.nextInt(2500);
            case NEUROLOGIST -> 5000 + random.nextInt(2000);
            default -> 1000 + random.nextInt(1500);
        };
    }
}
