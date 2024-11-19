package dev.piotrulla.newbieprotection;

import java.util.UUID;

public interface NewbieProtectionDataRepository {

    boolean isExists(UUID uniqueId);

    void save(Newbie newbie);

    void remove(UUID uniqueId);

    void remove(Newbie newbie);

    void update(Newbie newbie);

    Newbie load(UUID uniqueId);
}
