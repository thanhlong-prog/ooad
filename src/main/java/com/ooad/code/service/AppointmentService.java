package com.ooad.code.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ooad.code.model.Appointment;
import com.ooad.code.model.User;
import com.ooad.code.repository.AppointmentRepo;
import com.ooad.code.repository.UserRepo;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepo appointmentRepository;

    @Autowired
    private UserRepo userRepository;

    public boolean hasConflict(User user, LocalDateTime startTime, LocalDateTime endTime) {
        List<Appointment> overlaps = appointmentRepository
                .findByUserAndStartTimeLessThanAndEndTimeGreaterThan(user, endTime, startTime);
        return !overlaps.isEmpty();
    }

    public Appointment findFirstAvailableAppointment(User user, LocalDateTime startTime, LocalDateTime endTime) {
        return appointmentRepository
                .findFirstByUserAndStartTimeLessThanAndEndTimeGreaterThan(user, endTime, startTime)
                .orElse(null);
    }

    public boolean isValid(Appointment appointment) {
        return appointment.getName() != null && !appointment.getName().trim().isEmpty()
                && appointment.getLocation() != null && !appointment.getLocation().trim().isEmpty()
                && appointment.getStartTime() != null
                && appointment.getEndTime() != null
                && appointment.getStartTime().isBefore(appointment.getEndTime());
    }

    public Appointment findMatchingGroupMeeting(Appointment appointment) {
        return appointmentRepository.findAll().stream()
                .filter(a -> a.getName().equalsIgnoreCase(appointment.getName())
                        && a.getDurationMinutes() == appointment.getDurationMinutes())
                .findFirst()
                .orElse(null);
    }

    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getUserAppointments(User user) {
        return appointmentRepository.findByUser(user);
    }

    public List<Appointment> getAllAppointmentsForUser(User user) {
        List<Appointment> ownedAppointments = appointmentRepository.findByUser(user);
        List<Appointment> joinedAppointments = appointmentRepository.findByParticipantsContaining(user);
        Set<Appointment> combinedSet = new HashSet<>();
        combinedSet.addAll(ownedAppointments);
        combinedSet.addAll(joinedAppointments);

        return new ArrayList<>(combinedSet);
    }

    public int joinGroup(Long appointmentId, String username) {
        User user = userRepository.findByUsername(username);
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            if (appointment.getParticipants() == null) {
                appointment.setParticipants(new ArrayList<>());
            }

            if (appointment.getParticipants().contains(user)) {
                return 0;
            }

            boolean hasConflict = !appointmentRepository
                    .findByUserAndStartTimeLessThanAndEndTimeGreaterThan(
                            user, appointment.getEndTime(), appointment.getStartTime())
                    .isEmpty();

            if (!hasConflict && !appointment.getParticipants().contains(user)) {
                appointment.getParticipants().add(user);
                appointmentRepository.save(appointment);
                return 1;
            }
        }

        return 0;
    }

    public void replace(Long conflictAppId, Appointment newAppointment, User user) {
        Appointment conflictAppoint = appointmentRepository.findById(conflictAppId)
                .orElseThrow(() -> new RuntimeException("Appointment conflict không tồn tại"));

        if (!conflictAppoint.getUser().equals(user)) {
            throw new RuntimeException("Bạn không có quyền thay thế appointment này");
        }

        conflictAppoint.setName(newAppointment.getName());
        conflictAppoint.setLocation(newAppointment.getLocation());
        conflictAppoint.setStartTime(newAppointment.getStartTime());
        conflictAppoint.setEndTime(newAppointment.getEndTime());
        conflictAppoint.setReminders(newAppointment.getReminders());
        conflictAppoint.setParticipants(newAppointment.getParticipants());

        appointmentRepository.save(conflictAppoint);
    }

}
