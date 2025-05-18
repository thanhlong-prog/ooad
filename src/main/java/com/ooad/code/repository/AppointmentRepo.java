package com.ooad.code.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ooad.code.model.Appointment;
import com.ooad.code.model.User;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUserAndStartTimeLessThanAndEndTimeGreaterThan(
            User user, LocalDateTime startTime, LocalDateTime endTime);

    Optional<Appointment> findFirstByUserAndStartTimeLessThanAndEndTimeGreaterThan(
    User user, LocalDateTime endTime, LocalDateTime startTime);
    List<Appointment> findByUser(User user);
    List<Appointment> findByParticipantsContaining(User user);
}
