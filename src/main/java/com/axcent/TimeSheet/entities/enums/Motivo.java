package com.axcent.TimeSheet.entities.enums;

public enum Motivo {
    FERIE,
    MALATTIA,
    PERMESSO,
    ASSENZA,
    PART_TIME;

    @Override
    public String toString() {

        switch (this) {
            case PART_TIME: return "Part-Time";
            default: return name().charAt(0) + name().substring(1).toLowerCase();
        }

    }
}