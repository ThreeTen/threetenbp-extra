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

import org.threeten.bp.chrono.ChronoLocalDate;
import org.threeten.bp.chrono.HijrahChronology;
import org.threeten.bp.chrono.JapaneseChronology;
import org.threeten.bp.chrono.MinguoChronology;
import org.threeten.bp.chrono.ThaiBuddhistChronology;
import org.threeten.bp.temporal.Temporal;
import org.threeten.bp.temporal.TemporalAdjuster;

/**
 * Adjusters that allow dates to be adjusted in terms of a calendar system.
 */
public final class ChronoAdjusters {

    /**
     * Restricted constructor.
     */
    private ChronoAdjusters() {
    }

    //-----------------------------------------------------------------------
    /**
     * Convenience wrapper allowing a date to be easily adjusted in the Minguo calendar system.
     * <p>
     * This allows the specified adjuster to be run in terms of a Minguo date.
     * This would be used as follows:
     * <pre>
     *  date = date.with(minguo(temporal -> temporal.plus(1, MONTHS)));
     * </pre>
     * or in JDK 1.7:
     * <pre>
     *  date = date.with(minguo(new TemporalAdjuster() {
     *    @Override
     *    public Temporal adjustInto(Temporal temporal) {
     *        return temporal.plus(1, MONTHS);
     *    }
     *  }));
     * </pre>
     * 
     * @param adjuster  the adjuster to run in the Minguo calendar system
     * @return the adjuster, not null
     */
    public static TemporalAdjuster minguo(final TemporalAdjuster adjuster) {
        return new TemporalAdjuster() {
            @Override
            public Temporal adjustInto(Temporal temporal) {
                ChronoLocalDate<?> baseDate = MinguoChronology.INSTANCE.date(temporal);
                ChronoLocalDate<?> adjustedDate = (ChronoLocalDate<?>) adjuster.adjustInto(baseDate);
                return temporal.with(adjustedDate);
            }
        };
    }

    /**
     * Convenience wrapper allowing a date to be easily adjusted in the Hijrah calendar system.
     * <p>
     * This allows the specified adjuster to be run in terms of a Hijrah date.
     * This would be used as follows:
     * <pre>
     *  date = date.with(hijrah(temporal -> temporal.plus(1, MONTHS)));
     * </pre>
     * or in JDK 1.7:
     * <pre>
     *  date = date.with(hijrah(new TemporalAdjuster() {
     *    @Override
     *    public Temporal adjustInto(Temporal temporal) {
     *        return temporal.plus(1, MONTHS);
     *    }
     *  }));
     * </pre>
     * 
     * @param adjuster  the adjuster to run in the Hijrah calendar system
     * @return the adjuster, not null
     */
    public static TemporalAdjuster hijrah(final TemporalAdjuster adjuster) {
        return new TemporalAdjuster() {
            @Override
            public Temporal adjustInto(Temporal temporal) {
                ChronoLocalDate<?> baseDate = HijrahChronology.INSTANCE.date(temporal);
                ChronoLocalDate<?> adjustedDate = (ChronoLocalDate<?>) adjuster.adjustInto(baseDate);
                return temporal.with(adjustedDate);
            }
        };
    }

    /**
     * Convenience wrapper allowing a date to be easily adjusted in the Japanese calendar system.
     * <p>
     * This allows the specified adjuster to be run in terms of a Japanese date.
     * This would be used as follows:
     * <pre>
     *  date = date.with(japanese(temporal -> temporal.plus(1, MONTHS)));
     * </pre>
     * or in JDK 1.7:
     * <pre>
     *  date = date.with(japanese(new TemporalAdjuster() {
     *    @Override
     *    public Temporal adjustInto(Temporal temporal) {
     *        return temporal.plus(1, MONTHS);
     *    }
     *  }));
     * </pre>
     * 
     * @param adjuster  the adjuster to run in the Japanese calendar system
     * @return the adjuster, not null
     */
    public static TemporalAdjuster japanese(final TemporalAdjuster adjuster) {
        return new TemporalAdjuster() {
            @Override
            public Temporal adjustInto(Temporal temporal) {
                ChronoLocalDate<?> baseDate = JapaneseChronology.INSTANCE.date(temporal);
                ChronoLocalDate<?> adjustedDate = (ChronoLocalDate<?>) adjuster.adjustInto(baseDate);
                return temporal.with(adjustedDate);
            }
        };
    }

    /**
     * Convenience wrapper allowing a date to be easily adjusted in the ThaiBuddhist calendar system.
     * <p>
     * This allows the specified adjuster to be run in terms of a ThaiBuddhist date.
     * This would be used as follows:
     * <pre>
     *  date = date.with(thaiBuddhist(temporal -> temporal.plus(1, MONTHS)));
     * </pre>
     * or in JDK 1.7:
     * <pre>
     *  date = date.with(thaiBuddhist(new TemporalAdjuster() {
     *    @Override
     *    public Temporal adjustInto(Temporal temporal) {
     *        return temporal.plus(1, MONTHS);
     *    }
     *  }));
     * </pre>
     * 
     * @param adjuster  the adjuster to run in the ThaiBuddhist calendar system
     * @return the adjuster, not null
     */
    public static TemporalAdjuster thaiBuddhist(final TemporalAdjuster adjuster) {
        return new TemporalAdjuster() {
            @Override
            public Temporal adjustInto(Temporal temporal) {
                ChronoLocalDate<?> baseDate = ThaiBuddhistChronology.INSTANCE.date(temporal);
                ChronoLocalDate<?> adjustedDate = (ChronoLocalDate<?>) adjuster.adjustInto(baseDate);
                return temporal.with(adjustedDate);
            }
        };
    }

}
