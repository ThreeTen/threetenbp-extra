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
package org.threeten.extra;

import static org.threeten.bp.temporal.ChronoField.DAY_OF_MONTH;
import static org.threeten.bp.temporal.ChronoUnit.DAYS;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.threeten.bp.DateTimeException;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.MonthDay;
import org.threeten.bp.YearMonth;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.ValueRange;

/**
 * A representation of a day-of-month in the ISO-8601 calendar system.
 * <p>
 * {@code DayOfMonth} allows the day-of-month to be represented in a type-safe way.
 * The value can range from 1 to 31, as there is no month or year to validate against.
 *
 * <h3>Specification for implementors</h3>
 * This class is immutable and thread-safe.
 */
public final class DayOfMonth
        implements Comparable<DayOfMonth>, TemporalAccessor, TemporalAdjuster, Serializable {

    /**
     * A serialization identifier for this instance.
     */
    private static final long serialVersionUID = -8840172642009917873L;
    /**
     * Cache of singleton instances.
     */
    private static final AtomicReferenceArray<DayOfMonth> CACHE = new AtomicReferenceArray<DayOfMonth>(31);

    /**
     * The day-of-month being represented, from 1 to 31.
     */
    private final int dayOfMonth;

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of {@code DayOfMonth}.
     * <p>
     * A day-of-month object represents one of the 31 days of the month, from 1 to 31.
     *
     * @param dayOfMonth  the day-of-month to represent, from 1 to 31
     * @return the day-of-month, not null
     * @throws DateTimeException if the day-of-month is invalid
     */
    public static DayOfMonth of(int dayOfMonth) {
        try {
            DayOfMonth result = CACHE.get(--dayOfMonth);
            if (result == null) {
                DayOfMonth temp = new DayOfMonth(dayOfMonth + 1);
                CACHE.compareAndSet(dayOfMonth, null, temp);
                result = CACHE.get(dayOfMonth);
            }
            return result;
        } catch (IndexOutOfBoundsException ex) {
            throw new DateTimeException("Invalid value for DayOfMonth: " + ++dayOfMonth);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of {@code DayOfMonth} from a temporal object.
     * <p>
     * A {@code TemporalAccessor} represents some form of date and time information.
     * This factory converts the arbitrary temporal object to an instance of {@code DayOfWeek}.
     * <p>
     * The conversion extracts the {@link ChronoField#DAY_OF_MONTH DAY_OF_MONTH} field.
     * <p>
     * This method matches the signature of the functional interface {@link TemporalQuery}
     * allowing it to be used as a query via method reference, {@code DayOfMonth::from}.
     *
     * @param temporal  the temporal object to convert, not null
     * @return the day-of-month, not null
     * @throws DateTimeException if unable to convert to a {@code DayOfMonth}
     */
    public static DayOfMonth from(TemporalAccessor temporal) {
        if (temporal instanceof DayOfMonth) {
            return (DayOfMonth) temporal;
        }
        return of(temporal.get(DAY_OF_MONTH));
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance with the specified day-of-month.
     *
     * @param dayOfMonth  the day-of-month to represent
     */
    private DayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    /**
     * Resolve the singleton.
     *
     * @return the singleton, not null
     */
    private Object readResolve() {
        return of(dayOfMonth);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the field that defines how the day-of-month field operates.
     * <p>
     * The field provides access to the minimum and maximum values, and a
     * generic way to access values within a date-time.
     *
     * @return the day-of-month field, not null
     */
    public TemporalField getField() {
        return DAY_OF_MONTH;
    }

    /**
     * Gets the day-of-month value.
     *
     * @return the day-of-month, from 1 to 31
     */
    public int getValue() {
        return dayOfMonth;
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the specified field is supported.
     * <p>
     * This checks if this day-of-month can be queried for the specified field.
     * If false, then calling the {@link #range(TemporalField) range} and
     * {@link #get(TemporalField) get} methods will throw an exception.
     * <p>
     * If the field is {@link ChronoField#DAY_OF_MONTH DAY_OF_MONTH} then
     * this method returns true.
     * All other {@code ChronoField} instances will return false.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
     * passing {@code this} as the argument.
     * Whether the field is supported is determined by the field.
     *
     * @param field  the field to check, null returns false
     * @return true if the field is supported on this day-of-month, false if not
     */
    @Override
    public boolean isSupported(TemporalField field) {
        if (field instanceof ChronoField) {
            return field == DAY_OF_MONTH;
        }
        return field != null && field.isSupportedBy(this);
    }

    /**
     * Gets the range of valid values for the specified field.
     * <p>
     * The range object expresses the minimum and maximum valid values for a field.
     * This day-of-month is used to enhance the accuracy of the returned range.
     * If it is not possible to return the range, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is {@link ChronoField#DAY_OF_MONTH DAY_OF_MONTH} then the
     * range of the day-of-month, from 1 to 31, will be returned.
     * All other {@code ChronoField} instances will throw a {@code DateTimeException}.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.rangeRefinedBy(TemporalAccessor)}
     * passing {@code this} as the argument.
     * Whether the range can be obtained is determined by the field.
     *
     * @param field  the field to query the range for, not null
     * @return the range of valid values for the field, not null
     * @throws DateTimeException if the range for the field cannot be obtained
     */
    @Override
    public ValueRange range(TemporalField field) {
        if (field == DAY_OF_MONTH) {
            return field.range();
        } else if (field instanceof ChronoField) {
            throw new DateTimeException("Unsupported field: " + field.getName());
        }
        return field.rangeRefinedBy(this);
    }

    /**
     * Gets the value of the specified field from this day-of-month as an {@code int}.
     * <p>
     * This queries this day-of-month for the value for the specified field.
     * The returned value will always be within the valid range of values for the field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is {@link ChronoField#DAY_OF_MONTH DAY_OF_MONTH} then the
     * value of the day-of-month, from 1 to 31, will be returned.
     * All other {@code ChronoField} instances will throw a {@code DateTimeException}.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
     * passing {@code this} as the argument. Whether the value can be obtained,
     * and what the value represents, is determined by the field.
     *
     * @param field  the field to get, not null
     * @return the value for the field, within the valid range of values
     * @throws DateTimeException if a value for the field cannot be obtained
     * @throws DateTimeException if the range of valid values for the field exceeds an {@code int}
     * @throws DateTimeException if the value is outside the range of valid values for the field
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public int get(TemporalField field) {
        if (field == DAY_OF_MONTH) {
            return getValue();
        }
        return range(field).checkValidIntValue(getLong(field), field);
    }

    /**
     * Gets the value of the specified field from this day-of-month as a {@code long}.
     * <p>
     * This queries this day-of-month for the value for the specified field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is {@link ChronoField#DAY_OF_MONTH DAY_OF_MONTH} then the
     * value of the day-of-month, from 1 to 31, will be returned.
     * All other {@code ChronoField} instances will throw a {@code DateTimeException}.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
     * passing {@code this} as the argument. Whether the value can be obtained,
     * and what the value represents, is determined by the field.
     *
     * @param field  the field to get, not null
     * @return the value for the field
     * @throws DateTimeException if a value for the field cannot be obtained
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public long getLong(TemporalField field) {
        if (field == DAY_OF_MONTH) {
            return getValue();
        } else if (field instanceof ChronoField) {
            throw new DateTimeException("Unsupported field: " + field.getName());
        }
        return field.getFrom(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Queries this day-of-month using the specified query.
     * <p>
     * This queries this day-of-month using the specified query strategy object.
     * The {@code TemporalQuery} object defines the logic to be used to
     * obtain the result. Read the documentation of the query to understand
     * what the result of this method will be.
     * <p>
     * The result of this method is obtained by invoking the
     * {@link TemporalQuery#queryFrom(TemporalAccessor)} method on the
     * specified query passing {@code this} as the argument.
     *
     * @param <R> the type of the result
     * @param query  the query to invoke, not null
     * @return the query result, null may be returned (defined by the query)
     * @throws DateTimeException if unable to query (defined by the query)
     * @throws ArithmeticException if numeric overflow occurs (defined by the query)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <R> R query(TemporalQuery<R> query) {
        if (query == TemporalQueries.precision()) {
            return (R) DAYS;
        } else if (query == TemporalQueries.zoneId() || query == TemporalQueries.chronology()) {
            return null;
        }
        return query.queryFrom(this);
    }

    /**
     * Adjusts the specified temporal object to have this day-of-month.
     * <p>
     * This returns a temporal object of the same observable type as the input
     * with the day-of-month changed to be the same as this.
     * <p>
     * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
     * passing {@link ChronoField#DAY_OF_MONTH} as the field.
     * <p>
     * In most cases, it is clearer to reverse the calling pattern by using
     * {@link Temporal#with(TemporalAdjuster)}:
     * <pre>
     *   // these two lines are equivalent, but the second approach is recommended
     *   temporal = thisDayOfWeek.adjustInto(temporal);
     *   temporal = temporal.with(thisDayOfWeek);
     * </pre>
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param temporal  the target object to be adjusted, not null
     * @return the adjusted object, not null
     * @throws DateTimeException if unable to make the adjustment
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(DAY_OF_MONTH, getValue());
    }

    //-----------------------------------------------------------------------
    /**
     * Combines this day with a month to create a {@code MonthDay}.
     * <p>
     * This returns a {@code MonthDay} formed from this day and the specified month.
     *
     * @param month  the month to use, from 1 to 12
     * @return the date formed from this day and the specified month, not null
     * @throws DateTimeException if the month is invalid
     */
    public MonthDay atMonth(int month) {
        return MonthDay.of(month, dayOfMonth);
    }

    /**
     * Combines this day with a month to create a {@code MonthDay}.
     * <p>
     * This returns a {@code MonthDay} formed from this day and the specified month.
     *
     * @param month  the month to use, not null
     * @return the date formed from this day and the specified month, not null
     */
    public MonthDay atMonth(Month month) {
        return MonthDay.of(month, dayOfMonth);
    }

    /**
     * Combines this day with a year-month to create a {@code LocalDate}.
     * <p>
     * This returns a {@code LocalDate} formed from this day-of-month and the specified year-month.
     * <p>
     * If the day-of-month value if not valid for the year-month, it is adjusted
     * to the last valid day-of-month.
     *
     * @param yearMonth  the year-month to use, not null
     * @return the date formed from this day and the specified year-month, not null
     */
    public LocalDate atYearMonth(YearMonth yearMonth) {
        int day = Math.min(dayOfMonth, yearMonth.lengthOfMonth());
        return yearMonth.atDay(day);
    }

    //-----------------------------------------------------------------------
    /**
     * Compares this day-of-month instance to another.
     *
     * @param otherDayOfMonth  the other day-of-month instance, not null
     * @return the comparator value, negative if less, positive if greater
     */
    @Override
    public int compareTo(DayOfMonth otherDayOfMonth) {
        return Integer.compare(dayOfMonth, otherDayOfMonth.dayOfMonth);
    }

    //-----------------------------------------------------------------------
    /**
     * Is this instance equal to that specified, evaluating the day-of-month.
     *
     * @param otherDayOfMonth  the other day-of-month instance, null returns false
     * @return true if the day-of-month is the same
     */
    @Override
    public boolean equals(Object otherDayOfMonth) {
        return this == otherDayOfMonth;
    }

    /**
     * A hash code for the day-of-month object.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return dayOfMonth;
    }

    /**
     * A string describing the day-of-month object.
     *
     * @return a string describing this object
     */
    @Override
    public String toString() {
        return "DayOfMonth=" + getValue();
    }

}
