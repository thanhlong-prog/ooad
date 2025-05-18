package com.ooad.code.enums;

public enum ReminderType {
    FIVE_MIN_BEFORE(5, "5 phút trước"),
    TEN_MIN_BEFORE(10, "10 phút trước"),
    THIRTY_MIN_BEFORE(30, "30 phút trước"),
    ONE_HOUR_BEFORE(60, "1 giờ trước");

    private final int minutes;
    private final String label;

    ReminderType(int minutes, String label) {
        this.minutes = minutes;
        this.label = label;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getLabel() {
        return label;
    }

    public static ReminderType fromMinutes(int minutes) {
        for (ReminderType type : ReminderType.values()) {
            if (type.minutes == minutes) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown minutes value: " + minutes);
    }
}
