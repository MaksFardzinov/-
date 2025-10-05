package org.example.statistics;

import org.example.MedicalAppointment;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.example.Util.DoctorSpecialty;
import org.example.generator.SimpleGenerator;

public class AppointmentAnalyzer {
    private final List<MedicalAppointment> appointments;

    public AppointmentAnalyzer(List<MedicalAppointment> appointments) {
        this.appointments = appointments;
    }

    public Map<DoctorSpecialty, SpecialtyStatistics> calculateBySpecialtyIterative() {
        long startTime = System.nanoTime();

        Map<DoctorSpecialty, Long> countBySpecialty = new HashMap<>();
        Map<DoctorSpecialty, Double> revenueBySpecialty = new HashMap<>();
        Map<DoctorSpecialty, Double> feeSumBySpecialty = new HashMap<>();

        for (MedicalAppointment appointment : appointments) {
            DoctorSpecialty specialty = appointment.getSpecialty();

            countBySpecialty.put(specialty, countBySpecialty.getOrDefault(specialty, 0L) + 1);

            revenueBySpecialty.put(specialty,
                    revenueBySpecialty.getOrDefault(specialty, 0.0) + appointment.getConsultationFee());

            feeSumBySpecialty.put(specialty,
                    feeSumBySpecialty.getOrDefault(specialty, 0.0) + appointment.getConsultationFee());
        }

        Map<DoctorSpecialty, SpecialtyStatistics> result = new HashMap<>();
        for (DoctorSpecialty specialty : countBySpecialty.keySet()) {
            long count = countBySpecialty.get(specialty);
            double totalRevenue = revenueBySpecialty.get(specialty);
            double averageFee = totalRevenue / count;
            result.put(specialty, new SpecialtyStatistics(specialty, count, totalRevenue, averageFee));
        }
        long endTime = System.nanoTime();
        printExecutionTime("Итерационный метод", startTime, endTime);
        return result;
    }

    public Map<DoctorSpecialty, SpecialtyStatistics> calculateBySpecialtyStream() {
        long startTime = System.nanoTime();

        Map<DoctorSpecialty, SpecialtyStatistics> result = appointments.stream()
                .collect(Collectors.toMap(
                        MedicalAppointment::getSpecialty,
                        appointment -> {
                            double fee = appointment.getConsultationFee();
                            return new SpecialtyStatistics(
                                    appointment.getSpecialty(),
                                    1L,
                                    fee,
                                    fee
                            );
                        },
                        (stats1, stats2) -> {
                            long totalCount = stats1.getAppointmentCount() + stats2.getAppointmentCount();
                            double totalRevenue = stats1.getTotalRevenue() + stats2.getTotalRevenue();
                            double averageFee = totalRevenue / totalCount;

                            return new SpecialtyStatistics(
                                    stats1.getSpecialty(),
                                    totalCount,
                                    totalRevenue,
                                    averageFee
                            );
                        }
                ));

        long endTime = System.nanoTime();
        printExecutionTime("Stream API метод", startTime, endTime);

        return result;
    }

    public Map<DoctorSpecialty, SpecialtyStatistics> calculateBySpecialtyCustomCollector() {
        long startTime = System.nanoTime();

        Map<DoctorSpecialty, SpecialtyStatistics> result = appointments.stream()
                .collect(Collectors.groupingBy(
                        MedicalAppointment::getSpecialty,
                        customSpecialtyCollector()
                ));

        long endTime = System.nanoTime();
        printExecutionTime("Кастомный коллектор", startTime, endTime);

        return result;
    }

    private Collector<MedicalAppointment, ?, SpecialtyStatistics> customSpecialtyCollector() {
        return Collector.of(
                StatisticsAccumulator::new,
                (acc, appointment) -> {
                    acc.count++;
                    acc.totalFee += appointment.getConsultationFee();
                    acc.specialty = appointment.getSpecialty();
                },
                (acc1, acc2) -> {
                    acc1.count += acc2.count;
                    acc1.totalFee += acc2.totalFee;
                    return acc1;
                },
                acc -> {
                    double averageFee = acc.totalFee / acc.count;
                    return new SpecialtyStatistics(acc.specialty, acc.count, acc.totalFee, averageFee);
                }
        );
    }
    private static class StatisticsAccumulator {
        long count = 0;
        double totalFee = 0.0;
        DoctorSpecialty specialty;
    }

    private void printExecutionTime(String methodName, long startTime, long endTime) {
        long durationNanos = endTime - startTime;
        long durationMillis = durationNanos / 1_000_000;
        System.out.printf("%s: %d нс (%d мс)%n", methodName, durationNanos, durationMillis);
    }
    public static void runPerformanceTests() {
        int[] sizes = {5000, 50000, 250000};
         SimpleGenerator generator = new SimpleGenerator();

        for (int size : sizes) {
            System.out.println("\n=== Тест для " + size + " записей ===");

            List<MedicalAppointment> appointments = generator.generateAppointments(size);
            AppointmentAnalyzer analyzer = new AppointmentAnalyzer(appointments);

            System.out.println("Размер коллекции: " + appointments.size());

            analyzer.calculateBySpecialtyIterative();
            analyzer.calculateBySpecialtyStream();
            analyzer.calculateBySpecialtyCustomCollector();
        }
    }
}
