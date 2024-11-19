package dev.piotrulla.newbieprotection;

public class NewbieProtectionAPIImpl implements NewbieProtectionAPI {

    private final NewbieProtectionNameTagService newbieProtectionNameTagService;
    private final NewbieProtectionDataRepository newbieProtectionDataRepository;
    private final NewbieProtectionService newbieProtectionService;


    public NewbieProtectionAPIImpl(
            NewbieProtectionNameTagService newbieProtectionNameTagService,
            NewbieProtectionDataRepository newbieProtectionDataRepository,
            NewbieProtectionService newbieProtectionService
    ) {
        this.newbieProtectionNameTagService = newbieProtectionNameTagService;
        this.newbieProtectionDataRepository = newbieProtectionDataRepository;
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

    @Override
    public NewbieProtectionDataRepository getNewbieProtectionDataRepository() {
        return this.newbieProtectionDataRepository;
    }
}
