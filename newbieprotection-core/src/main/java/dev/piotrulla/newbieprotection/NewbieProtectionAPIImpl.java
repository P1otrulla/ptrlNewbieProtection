package dev.piotrulla.newbieprotection;

public class NewbieProtectionAPIImpl implements NewbieProtectionAPI {

    private final NewbieProtectionNameTagService newbieProtectionNameTagService;
    private final NewbieProtectionService newbieProtectionService;

    public NewbieProtectionAPIImpl(NewbieProtectionNameTagService newbieProtectionNameTagService, NewbieProtectionService newbieProtectionService) {
        this.newbieProtectionNameTagService = newbieProtectionNameTagService;
        this.newbieProtectionService = newbieProtectionService;
    }

    @Override
    public NewbieProtectionService getNewbieProtectionService() {
        return this.newbieProtectionService;
    }

    @Override
    public NewbieProtectionNameTagService getNewbieProtectionNameTagService() {
        return this.newbieProtectionNameTagService;
    }
}
