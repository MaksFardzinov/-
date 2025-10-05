package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.example.Util.DoctorSpecialty;
import org.example.Util.DoctorInfo;
import org.example.Util.AppointmentStatus;

public class MedicalAppointment {
    private final long appointmentId;
    private final String patientName;
    private final LocalDateTime appointmentDateTime;
    private final DoctorSpecialty specialty;
    private final DoctorInfo doctorInfo;
    private final List<String> symptoms;
    private final Util.AppointmentStatus status;
    private final double consultationFee;

    public MedicalAppointment(long appointmentId, String patientName,
                              LocalDateTime appointmentDateTime, DoctorSpecialty specialty,
                              DoctorInfo doctorInfo, List<String> symptoms,
                              AppointmentStatus status, double consultationFee) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.appointmentDateTime = appointmentDateTime;
        this.specialty = specialty;
        this.doctorInfo = doctorInfo;
        this.symptoms = new ArrayList<>(symptoms);
        this.status = status;
        this.consultationFee = consultationFee;
    }
    public long getAppointmentId() { return appointmentId; }
    public String getPatientName() { return patientName; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public LocalDate getAppointmentDate() { return appointmentDateTime.toLocalDate(); }
    public DoctorSpecialty getSpecialty() { return specialty; }
    public DoctorInfo getDoctorInfo() { return doctorInfo; }
    public List<String> getSymptoms() { return new ArrayList<>(symptoms); }
    public AppointmentStatus getStatus() { return status; }
    public double getConsultationFee() { return consultationFee; }

    @Override
    public String toString() {
        return String.format("Запись #%d: %s к %s (%s) - %.2f руб.",
                appointmentId, patientName, specialty, appointmentDateTime, consultationFee);
    }
}
