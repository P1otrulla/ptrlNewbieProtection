package dev.piotrulla.newbieprotection;

public class NewbieProtectionAPIImpl implements NewbieProtectionAPI {

    private final NewbieProtectionService newbieProtectionService;

    public NewbieProtectionAPIImpl(NewbieProtectionService newbieProtectionService) {
        this.newbieProtectionService = newbieProtectionService;
    }

    @Override
    public NewbieProtectionService getNewbieProtectionService() {
        return this.newbieProtectionService;
    }
}
