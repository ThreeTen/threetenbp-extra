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

import static org.threeten.bp.temporal.ChronoField.AMPM_OF_DAY;
import static org.threeten.bp.temporal.ChronoField.HOUR_OF_DAY;
import static org.threeten.bp.temporal.ChronoUnit.HALF_DAYS;

import java.util.Calendar;
import java.util.Locale;

import org.threeten.bp.DateTimeException;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalAdjuster;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.ValueRange;

/**
 * A half-day before or after midday, with the values 'AM' and 'PM'.
 * <p>
 * {@code AmPm} is an enum representing the half-day concepts of AM and PM.
 * AM is defined as from 00:00 to 11:59, while PM is defined from 12:00 to 23:59.
 * <p>
 * All date-time fields have an {@code int} value.
 * The {@code int} value follows {@link Calendar}, assigning 0 to AM and 1 to PM.
 * It is recommended that applications use the enum rather than the {@code int} value
 * to ensure code clarity.
 * <p>
 * <b>Do not use {@code ordinal()} to obtain the numeric representation of {@code AmPm}.
 * Use {@code getValue()} instead.</b>
 * <p>
 * This enum represents a common concept that is found in many calendar systems.
 * As such, this enum may be used by any calendar system that has the AM/PM
 * concept defined exactly equivalent to the ISO calendar system.
 *
 * <h3>Specification for implementors</h3>
 * This is an immutable and thread-safe enum.
 */
public enum AmPm implements TemporalAccessor, TemporalAdjuster {

    /**
     * The singleton instance for the morning, AM - ante meridiem.
     * This has the numeric value of {@code 0}.
     */
    AM,
    /**
     * The singleton instance for the afternoon, PM - post meridiem.
     * This has the numeric value of {@code 1}.
     */
    PM;

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of {@code AmPm} from an {@code int} value.
     * <p>
     * {@code AmPm} is an enum representing before and after midday.
     * This factory allows the enum to be obtained from the {@code int} value.
     * The {@code int} value follows {@link Calendar}, assigning 0 to AM and 1 to PM.
     *
     * @param amPmValue  the AM/PM value to represent, from 0 (AM) to 1 (PM)
     * @return the AM/PM, not null
     * @throws DateTimeException if the value is invalid
     */
    public static AmPm of(int amPmValue) {
        switch (amPmValue) {
            case 0: return AM;
            case 1: return PM;
            default: throw new DateTimeException("Invalid value for AM/PM: " + amPmValue);
        }
    }

    /**
     * Obtains an instance of {@code AmPm} from an hour-of-day.
     * <p>
     * {@code AmPm} is an enum representing before and after midday.
     * This factory allows the enum to be obtained from the hour-of-day value, from 0 to 23.
     *
     * @param hourOfDay  the hour-of-day to extract from, from 0 to 23
     * @return the AM/PM, not null
     * @throws DateTimeException if the hour-of-day is invalid
     */
    public static AmPm ofHour(int hourOfDay) {
        HOUR_OF_DAY.checkValidValue(hourOfDay);
        return hourOfDay < 12 ? AM : PM;
    }

    //-----------------------------------------------------------------------
    /**
     * Obtains an instance of {@code AmPm} from a temporal object.
     * <p>
     * A {@code TemporalAccessor} represents some form of date and time information.
     * This factory converts the arbitrary temporal object to an instance of {@code DayOfWeek}.
     * <p>
     * The conversion extracts the {@link ChronoField#AMPM_OF_DAY AMPM_OF_DAY} field.
     * <p>
     * This method matches the signature of the functional interface {@link TemporalQuery}
     * allowing it to be used as a query via method reference, {@code AmPm::from}.
     *
     * @param temporal  the temporal object to convert, not null
     * @return the AM/PM, not null
     * @throws DateTimeException if unable to convert to a {@code AmPm}
     */
    public static AmPm from(TemporalAccessor temporal) {
        if (temporal instanceof AmPm) {
            return (AmPm) temporal;
        }
        return of(temporal.get(AMPM_OF_DAY));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the AM/PM {@code int} value.
     * <p>
     * The values are numbered following {@link Calendar}, assigning 0 to AM and 1 to PM.
     *
     * @return the AM/PM value, from 0 (AM) to 1 (PM)
     */
    public int getValue() {
        return ordinal();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the textual representation, such as 'AM' or 'PM'.
     * <p>
     * This returns the textual name used to identify the AM/PM.
     * The parameters control the length of the returned text and the locale.
     * <p>
     * If no textual mapping is found then the {@link #getValue() numeric value} is returned.
     *
     * @param style  the length of the text required, not null
     * @param locale  the locale to use, not null
     * @return the text value of the AM/PM, not null
     */
    public String getDisplayName(TextStyle style, Locale locale) {
        return new DateTimeFormatterBuilder().appendText(AMPM_OF_DAY, style).toFormatter(locale).format(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the specified field is supported.
     * <p>
     * This checks if this AM/PM can be queried for the specified field.
     * If false, then calling the {@link #range(TemporalField) range} and
     * {@link #get(TemporalField) get} methods will throw an exception.
     * <p>
     * If the field is {@link ChronoField#AMPM_OF_DAY AMPM_OF_DAY} then
     * this method returns true.
     * All other {@code ChronoField} instances will return false.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
     * passing {@code this} as the argument.
     * Whether the field is supported is determined by the field.
     *
     * @param field  the field to check, null returns false
     * @return true if the field is supported on this AM/PM, false if not
     */
    @Override
    public boolean isSupported(TemporalField field) {
        if (field instanceof ChronoField) {
            return field == AMPM_OF_DAY;
        }
        return field != null && field.isSupportedBy(this);
    }

    /**
     * Gets the range of valid values for the specified field.
     * <p>
     * The range object expresses the minimum and maximum valid values for a field.
     * This AM/PM is used to enhance the accuracy of the returned range.
     * If it is not possible to return the range, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is {@link ChronoField#AMPM_OF_DAY AMPM_OF_DAY} then the
     * range of the AM/PM, from 0 to 1, will be returned.
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
        if (field == AMPM_OF_DAY) {
            return field.range();
        } else if (field instanceof ChronoField) {
            throw new DateTimeException("Unsupported field: " + field.getName());
        }
        return field.rangeRefinedBy(this);
    }

    /**
     * Gets the value of the specified field from this AM/PM as an {@code int}.
     * <p>
     * This queries this AM/PM for the value for the specified field.
     * The returned value will always be within the valid range of values for the field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is {@link ChronoField#AMPM_OF_DAY AMPM_OF_DAY} then the
     * value of the AM/PM, from 0 to 1, will be returned.
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
        if (field == AMPM_OF_DAY) {
            return getValue();
        }
        return range(field).checkValidIntValue(getLong(field), field);
    }

    /**
     * Gets the value of the specified field from this AM/PM as a {@code long}.
     * <p>
     * This queries this AM/PM for the value for the specified field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is {@link ChronoField#AMPM_OF_DAY AMPM_OF_DAY} then the
     * value of the AM/PM, from 0 to 1, will be returned.
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
        if (field == AMPM_OF_DAY) {
            return getValue();
        } else if (field instanceof ChronoField) {
            throw new DateTimeException("Unsupported field: " + field.getName());
        }
        return field.getFrom(this);
    }

    //-----------------------------------------------------------------------
    /**
     * Queries this AM/PM using the specified query.
     * <p>
     * This queries this AM/PM using the specified query strategy object.
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
            return (R) HALF_DAYS;
        } else if (query == TemporalQueries.zoneId() || query == TemporalQueries.chronology()) {
            return null;
        }
        return query.queryFrom(this);
    }

    /**
     * Adjusts the specified temporal object to have this AM/PM.
     * <p>
     * This returns a temporal object of the same observable type as the input
     * with the AM/PM changed to be the same as this.
     * <p>
     * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
     * passing {@link ChronoField#AMPM_OF_DAY} as the field.
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
        return temporal.with(AMPM_OF_DAY, getValue());
    }

}
