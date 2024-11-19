package dev.piotrulla.newbieprotection;

public interface NewbieProtectionAPI {

    NewbieProtectionService getNewbieProtectionService();

    NewbieProtectionNameTagService getNewbieProtectionNameTagService();

    NewbieProtectionDataRepository getNewbieProtectionDataRepository();

}
