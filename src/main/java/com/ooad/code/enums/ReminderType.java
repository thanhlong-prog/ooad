package com.ooad.code.enums;

public enum ReminderType {
    FIVE_MIN_BEFORE("5 phút trước"),
    FIFTEEN_MIN_BEFORE("15 phút trước"),
    ONE_HOUR_BEFORE("1 giờ trước"),
    ONE_DAY_BEFORE("1 ngày trước");

    private final String label;

    ReminderType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // Có thể dùng để parse từ String nếu cần
    public static ReminderType fromLabel(String label) {
        for (ReminderType type : ReminderType.values()) {
            if (type.label.equals(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown label: " + label);
    }
}
