package dev.piotrulla.newbieprotection.util;

import com.eternalcode.commons.time.DurationParser;
import com.eternalcode.commons.time.TemporalAmountParser;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DurationUtil {

    private static final TemporalAmountParser<Duration> WITHOUT_MILLIS_FORMAT = new DurationParser()
            .withUnit("s", ChronoUnit.SECONDS)
            .withUnit("m", ChronoUnit.MINUTES)
            .withUnit("h", ChronoUnit.HOURS)
            .withUnit("d", ChronoUnit.DAYS)
            .roundOff(ChronoUnit.MILLIS);

    private static final TemporalAmountParser<Duration> STANDARD_FORMAT = new DurationParser()
            .withUnit("ms", ChronoUnit.MILLIS)
            .withUnit("s", ChronoUnit.SECONDS)
            .withUnit("m", ChronoUnit.MINUTES)
            .withUnit("h", ChronoUnit.HOURS)
            .withUnit("d", ChronoUnit.DAYS);


    public static final Duration ONE_SECOND = Duration.ofSeconds(1);


    public static String format(Duration duration, boolean useMillis) {
        if (!useMillis) {
            if (duration.toMillis() < ONE_SECOND.toMillis()) {
                return "0s";
            }

            return WITHOUT_MILLIS_FORMAT.format(duration);
        }

        return STANDARD_FORMAT.format(duration);
    }

    public static String format(Duration duration) {
        return format(duration, false);
    }
}

