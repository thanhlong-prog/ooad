package com.ooad.code.dto;

public class AppointmentDTO {
    private Long id;
    private String name;
    private String location;
    private String startTime;
    private String endTime;
    private String reminder;

    public AppointmentDTO() {}

    public AppointmentDTO(com.ooad.code.model.Appointment appt) {
        this.id = appt.getId();
        this.name = appt.getName();
        this.location = appt.getLocation();
        this.startTime = appt.getStartTime() != null ? appt.getStartTime().toString() : null;
        this.endTime = appt.getEndTime() != null ? appt.getEndTime().toString() : null;
        if (appt.getReminders() != null && !appt.getReminders().isEmpty()) {
            this.reminder = appt.getReminders().get(0).getType();
        } else {
            this.reminder = "none";
        }
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getReminder() { return reminder; }
    public void setReminder(String reminder) { this.reminder = reminder; }
}
