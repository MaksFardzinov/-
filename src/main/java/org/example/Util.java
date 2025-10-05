package org.example;

public class Util {
    public enum DoctorSpecialty {
        THERAPIST, CARDIOLOGIST, NEUROLOGIST, SURGEON, PEDIATRICIAN, DENTIST
    }
    public enum AppointmentStatus {
        SCHEDULED, COMPLETED, CANCELLED, IN_PROGRESS
    }
    public record DoctorInfo(String licenseNumber, int yearsOfExperience, String department) {
        public DoctorInfo {
            if (yearsOfExperience < 0) {
                throw new IllegalArgumentException("Стаж не может быть отрицательным");
            }
        }
    }

}
