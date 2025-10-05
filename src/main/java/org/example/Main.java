package org.example;

import org.example.generator.SimpleGenerator;
import org.example.statistics.AppointmentAnalyzer;
import org.example.statistics.SpecialtyStatistics;

import java.util.List;
import java.util.Map;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Лабораторная работа: Система записи пациентов к врачу ===\n");

        SimpleGenerator generator = new SimpleGenerator();
        List<MedicalAppointment> appointments = generator.generateAppointments(100);


        System.out.println("Примеры сгенерированных записей:");
        appointments.stream().limit(5).forEach(System.out::println);

        AppointmentAnalyzer analyzer = new AppointmentAnalyzer(appointments);

        System.out.println("\n=== Анализ данных ===");
        Map<Util.DoctorSpecialty, SpecialtyStatistics> iterativeResult = analyzer.calculateBySpecialtyIterative();
        Map<Util.DoctorSpecialty, SpecialtyStatistics> streamResult = analyzer.calculateBySpecialtyStream();
        Map<Util.DoctorSpecialty, SpecialtyStatistics> customResult = analyzer.calculateBySpecialtyCustomCollector();

        System.out.println("\nРезультаты (итерационный метод):");
        iterativeResult.values().forEach(System.out::println);

        System.out.println("\nРезультаты (метод stream api):");
        streamResult.values().forEach(System.out::println);

        System.out.println("\nРезультаты (кастомный итератор):");
        customResult.values().forEach(System.out::println);

        System.out.println("\n=== Тесты производительности ===");
        AppointmentAnalyzer.runPerformanceTests();
    }
}