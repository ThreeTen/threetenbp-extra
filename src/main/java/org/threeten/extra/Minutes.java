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

import static org.threeten.bp.temporal.ChronoUnit.MINUTES;

import java.io.Serializable;

import org.threeten.bp.temporal.TemporalUnit;

/**
 * An amount of time measured in minutes, such as '6 Minutes'.
 * <p>
 * This class stores an amount of time in terms of the minutes unit of time.
 *
 * <h3>Specification for implementors</h3>
 * This class is immutable and thread-safe.
 */
public final class Minutes extends AbstractSimpleAmount<Minutes> implements Serializable {

    /**
     * A constant for zero minutes.
     */
    public static final Minutes ZERO = new Minutes(0);
    /**
     * A serialization identifier for this class.
     */
    private static final long serialVersionUID = -8903767091325669093L;

    /**
     * The number of minutes.
     */
    private final int minutes;

    /**
     * Obtains an instance of {@code Minutes}.
     *
     * @param minutes  the number of minutes the instance will represent, may be negative
     * @return the {@code Minutes} instance, not null
     */
    public static Minutes of(int minutes) {
        if (minutes == 0) {
            return ZERO;
        }
        return new Minutes(minutes);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance using a specific number of minutes.
     *
     * @param minutes  the minutes to use
     */
    private Minutes(int minutes) {
        super();
        this.minutes = minutes;
    }

    /**
     * Resolves singletons.
     *
     * @return the singleton instance
     */
    private Object readResolve() {
        return Minutes.of(minutes);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of minutes in this amount.
     *
     * @return the number of minutes, may be negative
     */
    @Override
    public int getAmount() {
        return minutes;
    }

    /**
     * Returns a new instance of the subclass with a different number of minutes.
     *
     * @param amount  the number of minutes to set in the new instance, may be negative
     * @return a new period element, not null
     */
    @Override
    public Minutes withAmount(int amount) {
        return Minutes.of(amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the unit defining the amount of time.
     *
     * @return the minutes unit, not null
     */
    @Override
    public TemporalUnit getUnit() {
        return MINUTES;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with the specified amount of time added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param amount  the amount of time to add, may be negative
     * @return the new amount plus the specified amount of time, not null
     * @throws ArithmeticException if the result overflows an {@code int}
     */
    public Minutes plus(Minutes amount) {
        return plus(amount.getAmount());
    }

    /**
     * Returns a new instance with the specified amount of time subtracted.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param amount  the amount of time to add, may be negative
     * @return the new amount minus the specified amount of time, not null
     * @throws ArithmeticException if the result overflows an {@code int}
     */
    public Minutes minus(Minutes amount) {
        return minus(amount.getAmount());
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a string representation of the number of minutes.
     * This will be in the format 'PTnM' where n is the number of minutes.
     *
     * @return the number of minutes in ISO8601 string format
     */
    @Override
    public String toString() {
        return "PT" + minutes + "M";
    }

}
