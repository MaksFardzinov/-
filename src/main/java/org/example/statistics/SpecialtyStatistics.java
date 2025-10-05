package org.example.statistics;


import org.example.Util.DoctorSpecialty;

public class SpecialtyStatistics {
    private final DoctorSpecialty specialty;
    private final long appointmentCount;
    private final double totalRevenue;
    private final double averageFee;

    public SpecialtyStatistics(DoctorSpecialty specialty, long appointmentCount,
                               double totalRevenue, double averageFee) {
        this.specialty = specialty;
        this.appointmentCount = appointmentCount;
        this.totalRevenue = totalRevenue;
        this.averageFee = averageFee;
    }
    public DoctorSpecialty getSpecialty() { return specialty; }
    public long getAppointmentCount() { return appointmentCount; }
    public double getTotalRevenue() { return totalRevenue; }
    public double getAverageFee() { return averageFee; }

    @Override
    public String toString() {
        return String.format("%s: %d записей, доход: %.2f руб., средняя цена: %.2f руб.",
                specialty, appointmentCount, totalRevenue, averageFee);
    }
}