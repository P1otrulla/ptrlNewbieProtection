package dev.piotrulla.newbieprotection;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public interface Newbie {

    /**
     * Get unique id of newbie
     *
     * @return unique id
     */
    UUID uniqueId();

    /**
     * Get issued at time
     *
     * @return issued at time
     */
    Instant issuedAt();

    /**
     * Set issued at time
     *
     * @param issuedAt issued at time
     */
    void setIssuedAt(Instant issuedAt);

    /**
     * Get player name
     *
     * @return player name
     */
    Duration protectionTime();

    /**
     * Set protection time
     *
     * @param protectionTime protection time
     */
    void setProtectionTime(Duration protectionTime);

}
