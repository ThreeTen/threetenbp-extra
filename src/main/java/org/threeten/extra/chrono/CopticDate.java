/*
 * Copyright (c) 2007-2013, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.threeten.extra.chrono;

import static org.threeten.bp.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static org.threeten.bp.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static org.threeten.bp.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static org.threeten.bp.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static org.threeten.bp.temporal.ChronoField.DAY_OF_MONTH;
import static org.threeten.bp.temporal.ChronoField.MONTH_OF_YEAR;

import java.io.Serializable;

import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;
import org.threeten.bp.Year;
import org.threeten.bp.chrono.ChronoLocalDate;
import org.threeten.bp.jdk8.DefaultInterfaceChronoLocalDate;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalAmount;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalUnit;
import org.threeten.bp.temporal.ValueRange;

/**
 * A date in the Coptic calendar system.
 * <p>
 * This implements {@code ChronoLocalDate} for the {@link CopticChronology Coptic calendar}.
 *
 * <h3>Specification for implementors</h3>
 * This class is immutable and thread-safe.
 */
public final class CopticDate
        extends DefaultInterfaceChronoLocalDate<CopticDate>
        implements ChronoLocalDate<CopticDate>, Serializable {

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = -7920528871688876868L;
    /**
     * The difference between the Coptic and Coptic epoch day count.
     */
    private static final int EPOCH_DAY_DIFFERENCE = 574971 + 40587;

    /**
     * The proleptic year.
     */
    private final int prolepticYear;
    /**
     * The month.
     */
    private final short month;
    /**
     * The day.
     */
    private final short day;

    //-----------------------------------------------------------------------
    /**
     * Creates a date in Coptic calendar system from the Era, year-of-era,
     * month-of-year and day-of-month.
     *
     * @param era  the CopticEra, not null
     * @param year  the calendar system year-of-era
     * @param month  the calendar system month-of-year
     * @param dayOfMonth  the calendar system day-of-month
     * @return the date in this calendar system, not null
     */
    public static CopticDate of(CopticEra era, int year, int month, int dayOfMonth) {
        return (CopticDate)CopticChronology.INSTANCE.date(era, year, month, dayOfMonth);
    }

    /**
     * Creates an instance.
     *
     * @param epochDay  the epoch day to convert based on 1970-01-01 (ISO)
     * @return the Coptic date, not null
     * @throws DateTimeException if the date is invalid
     */
    public static CopticDate ofEpochDay(long epochDay) {
        // TODO: validate
//        if (epochDay < MIN_EPOCH_DAY || epochDay > MAX_EPOCH_DAY) {
//            throw new CalendricalRuleException("Date exceeds supported range for CopticDate", Coptic.YEAR);
//        }
        epochDay += EPOCH_DAY_DIFFERENCE;
        int prolepticYear = (int) (((epochDay * 4) + 1463) / 1461);
        int startYearEpochDay = (prolepticYear - 1) * 365 + (prolepticYear / 4);
        int doy0 = (int) (epochDay - startYearEpochDay);
        int month = doy0 / 30 + 1;
        int dom = doy0 % 30 + 1;
        return new CopticDate(prolepticYear, month, dom);
    }

    private static CopticDate resolvePreviousValid(int prolepticYear, int month, int day) {
        if (month == 13 && day > 5) {
            day = CopticChronology.INSTANCE.isLeapYear(prolepticYear) ? 6 : 5;
        }
        return new CopticDate(prolepticYear, month, day);
    }

    //-----------------------------------------------------------------------
    /**
     * Creates an instance.
     *
     * @param prolepticYear  the Coptic proleptic-year
     * @param month  the Coptic month, from 1 to 13
     * @param dayOfMonth  the Coptic day-of-month, from 1 to 30
     * @throws DateTimeException if the date is invalid
     */
    CopticDate(int prolepticYear, int month, int dayOfMonth) {
        CopticChronology.MOY_RANGE.checkValidValue(month, MONTH_OF_YEAR);
        ValueRange range;
        if (month == 13) {
            range = CopticChronology.INSTANCE.isLeapYear(prolepticYear) ? CopticChronology.DOM_RANGE_LEAP : CopticChronology.DOM_RANGE_NONLEAP;
        } else {
            range = CopticChronology.DOM_RANGE;
        }
        range.checkValidValue(dayOfMonth, DAY_OF_MONTH);

        this.prolepticYear = prolepticYear;
        this.month = (short) month;
        this.day = (short) dayOfMonth;
    }

    /**
     * Validates the object.
     *
     * @return the resolved date, not null
     */
    private Object readResolve() {
        // TODO: validate
        return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public CopticChronology getChronology() {
        return CopticChronology.INSTANCE;
    }

    //-----------------------------------------------------------------------
    @Override
    public int lengthOfMonth() {
        switch (month) {
            case 13:
                return (isLeapYear() ? 6 : 5);
            default:
                return 30;
        }
    }

    @Override
    public boolean isSupported(TemporalField field) {
        if (field instanceof ChronoField) {
            return ((ChronoField) field).isDateField();
        }
        return field != null && field.isSupportedBy(this);
    }

    @Override
    public ValueRange range(TemporalField field) {
        if (field instanceof ChronoField) {
            if (isSupported(field)) {
                ChronoField f = (ChronoField) field;
                switch (f) {
                    case DAY_OF_MONTH: return ValueRange.of(1, lengthOfMonth());
                    case DAY_OF_YEAR: return ValueRange.of(1, lengthOfYear());
                    case ALIGNED_WEEK_OF_MONTH: return ValueRange.of(1, month == 13 ? 1 : 5);
                    case YEAR:
                    case YEAR_OF_ERA: return (prolepticYear <= 0 ?
                            ValueRange.of(1, Year.MAX_VALUE + 1) : ValueRange.of(1, Year.MAX_VALUE));  // TODO
                }
                return getChronology().range(f);
            }
            throw new DateTimeException("Unsupported field: " + field.getName());
        }
        return field.rangeRefinedBy(this);
    }

    @Override
    public long getLong(TemporalField field) {
        if (field instanceof ChronoField) {
            switch ((ChronoField) field) {
                case DAY_OF_WEEK: return getDayOfWeek();
                case ALIGNED_DAY_OF_WEEK_IN_MONTH: return ((day - 1) % 7) + 1;
                case ALIGNED_DAY_OF_WEEK_IN_YEAR: return ((getDayOfYear() - 1) % 7) + 1;
                case DAY_OF_MONTH: return day;
                case DAY_OF_YEAR: return getDayOfYear();
                case EPOCH_DAY: return toEpochDay();
                case ALIGNED_WEEK_OF_MONTH: return ((day - 1) / 7) + 1;
                case ALIGNED_WEEK_OF_YEAR: return ((getDayOfYear() - 1) / 7) + 1;
                case MONTH_OF_YEAR: return month;
                case YEAR_OF_ERA: return (prolepticYear >= 1 ? prolepticYear : 1 - prolepticYear);
                case YEAR: return prolepticYear;
                case ERA: return (prolepticYear >= 1 ? 1 : 0);
            }
            throw new DateTimeException("Unsupported field: " + field.getName());
        }
        return field.getFrom(this);
    }

    private int getDayOfYear() {
        return (month - 1) * 30 + day;
    }

    private int getDayOfWeek() {
        return Jdk8Methods.floorMod(toEpochDay() + 3, 7) + 1;
    }

    //-------------------------------------------------------------------------
    @Override
    public CopticDate with(TemporalAdjuster adjuster) {
        return (CopticDate) adjuster.adjustInto(this);
    }

    @Override
    public CopticDate with(TemporalField field, long newValue) {
        if (field instanceof ChronoField) {
            ChronoField f = (ChronoField) field;
            f.checkValidValue(newValue);        // TODO: validate value
            int nvalue = (int) newValue;
            switch (f) {
                case DAY_OF_WEEK: return plusDays(newValue - getDayOfWeek());
                case ALIGNED_DAY_OF_WEEK_IN_MONTH: return plusDays(newValue - getLong(ALIGNED_DAY_OF_WEEK_IN_MONTH));
                case ALIGNED_DAY_OF_WEEK_IN_YEAR: return plusDays(newValue - getLong(ALIGNED_DAY_OF_WEEK_IN_YEAR));
                case DAY_OF_MONTH: return resolvePreviousValid(prolepticYear, month, nvalue);
                case DAY_OF_YEAR: return resolvePreviousValid(prolepticYear, ((nvalue - 1) / 30) + 1, ((nvalue - 1) % 30) + 1);
                case EPOCH_DAY: return ofEpochDay(nvalue);
                case ALIGNED_WEEK_OF_MONTH: return plusDays((newValue - getLong(ALIGNED_WEEK_OF_MONTH)) * 7);
                case ALIGNED_WEEK_OF_YEAR: return plusDays((newValue - getLong(ALIGNED_WEEK_OF_YEAR)) * 7);
                case MONTH_OF_YEAR: return resolvePreviousValid(prolepticYear, nvalue, day);
                case YEAR_OF_ERA: return resolvePreviousValid(prolepticYear >= 1 ? nvalue : 1 - nvalue, month, day);
                case YEAR: return resolvePreviousValid(nvalue, month, day);
                case ERA: return resolvePreviousValid(1 - prolepticYear, month, day);
            }
            throw new DateTimeException("Unsupported field: " + field.getName());
        }
        return field.adjustInto(this, newValue);
    }

    //-----------------------------------------------------------------------
    @Override
    public CopticDate plus(TemporalAmount amount) {
        return (CopticDate) super.plus(amount);
    }

    @Override
    public CopticDate plus(long amountToAdd, TemporalUnit unit) {
        if (unit instanceof ChronoUnit) {
            ChronoUnit f = (ChronoUnit) unit;
            switch (f) {
                case DAYS: return plusDays(amountToAdd);
                case WEEKS: return plusDays(Jdk8Methods.safeMultiply(amountToAdd, 7));
                case MONTHS: return plusMonths(amountToAdd);
                case YEARS: return plusYears(amountToAdd);
                case DECADES: return plusYears(Jdk8Methods.safeMultiply(amountToAdd, 10));
                case CENTURIES: return plusYears(Jdk8Methods.safeMultiply(amountToAdd, 100));
                case MILLENNIA: return plusYears(Jdk8Methods.safeMultiply(amountToAdd, 1000));
//                case ERAS: throw new DateTimeException("Unable to add era, standard calendar system only has one era");
//                case FOREVER: return (period == 0 ? this : (period > 0 ? LocalDate.MAX_DATE : LocalDate.MIN_DATE));
            }
            throw new DateTimeException(unit.getName() + " not valid for CopticDate");
        }
        return unit.addTo(this, amountToAdd);
    }

    //-----------------------------------------------------------------------
    private CopticDate plusYears(long years) {
        return plusMonths(Jdk8Methods.safeMultiply(years, 13));
    }

    private CopticDate plusMonths(long months) {
        if (months == 0) {
            return this;
        }
        long curEm = prolepticYear * 13L + (month - 1);
        long calcEm = Jdk8Methods.safeAdd(curEm, months);
        int newYear = Jdk8Methods.safeToInt(Jdk8Methods.floorDiv(calcEm, 13));
        int newMonth = Jdk8Methods.floorMod(calcEm, 13) + 1;
        return resolvePreviousValid(newYear, newMonth, day);
    }

    private CopticDate plusDays(long days) {
        if (days == 0) {
            return this;
        }
        return CopticDate.ofEpochDay(Jdk8Methods.safeAdd(toEpochDay(), days));
    }

    //-------------------------------------------------------------------------
    @Override
    public CopticDate minus(TemporalAmount amount) {
        return (CopticDate) super.minus(amount);
    }

    @Override
    public CopticDate minus(long amountToSubtract, TemporalUnit unit) {
        return (CopticDate) super.minus(amountToSubtract, unit);
    }

//    public CopticDate withEra(Era era) {
//        return with(ChronoField.ERA, era.getValue());
//    }
//
//    public CopticDate withYear(int year) {
//        return with(ChronoField.YEAR_OF_ERA, year);
//    }
//
//    public CopticDate withMonth(int month) {
//        return with(ChronoField.MONTH_OF_YEAR, month);
//    }
//
//    public CopticDate withDayOfMonth(int dayOfMonth) {
//        return with(ChronoField.DAY_OF_MONTH, month);
//    }
//
//    public CopticDate withDayOfYear(int dayOfYear) {
//        return with(ChronoField.DAY_OF_YEAR, month);
//    }
//
//    public CopticDate minusYears(long yearsToSubtract) {
//        return (yearsToSubtract == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-yearsToSubtract));
//    }
//
//    public CopticDate minusMonths(long monthsToSubtract) {
//        return (monthsToSubtract == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1) : plusMonths(-monthsToSubtract));
//    }
//
//    public CopticDate minusWeeks(long weeksToSubtract) {
//        return  (weeksToSubtract == Long.MIN_VALUE ? plusWeeks(Long.MAX_VALUE).plusWeeks(1) : plusWeeks(-weeksToSubtract));
//    }
//
//    public CopticDate minusDays(long daysToSubtract) {
//        return (daysToSubtract == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1) : plusDays(-daysToSubtract));
//    }

    @Override
    public long periodUntil(Temporal endDateTime, TemporalUnit unit) {
        if (endDateTime instanceof ChronoLocalDate == false) {
            throw new DateTimeException("Unable to calculate period between objects of two different types");
        }
        ChronoLocalDate<?> end = (ChronoLocalDate<?>) endDateTime;
        if (getChronology().equals(end.getChronology()) == false) {
            throw new DateTimeException("Unable to calculate period between two different chronologies");
        }
        if (unit instanceof ChronoUnit) {
            return LocalDate.from(this).periodUntil(end, unit);  // TODO: this is wrong
        }
        return unit.between(this, endDateTime);
    }

    @Override
    public Period periodUntil(ChronoLocalDate<?> endDate) {
        throw new UnsupportedOperationException();
    }

    //-----------------------------------------------------------------------
    @Override
    public long toEpochDay() {
        long year = (long) prolepticYear;
        long copticEpochDay = ((year - 1) * 365) + Jdk8Methods.floorDiv(year, 4) + (getDayOfYear() - 1);
        return copticEpochDay - EPOCH_DAY_DIFFERENCE;
    }

}
