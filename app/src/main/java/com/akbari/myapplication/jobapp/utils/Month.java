package com.akbari.myapplication.jobapp.utils;

/**
 * @author Akbari
 * @version ${VERSION}
 * @since 12/06/2016
 */

public enum Month {

    FARVARDIN, ORDIBEHESHT, KHORDAD,
    TIR, MORDAD, SHAHRIVAR,
    MEHR, ABAN, AZAR,
    DEY, BAHMAN, ESFAND;

    private final int value;

    Month() {
        this.value = ordinal();
    }

    @Override
    public String toString() {
        switch (this) {
            case FARVARDIN:
                return "فروردین";
            case ORDIBEHESHT:
                return "اردیبهشت";
            case KHORDAD:
                return "خرداد";
            case TIR:
                return "تیر";
            case MORDAD:
                return "مرداد";
            case SHAHRIVAR:
                return "شهریور";
            case MEHR:
                return "مهر";
            case ABAN:
                return "آبان";
            case AZAR:
                return "آذر";
            case DEY:
                return "دی";
            case BAHMAN:
                return "بهمن";
            case ESFAND:
                return "اسفند";
            default:
                return "";
        }
    }
}
