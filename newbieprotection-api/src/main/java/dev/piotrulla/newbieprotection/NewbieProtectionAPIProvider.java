package dev.piotrulla.newbieprotection;

public class NewbieProtectionAPIProvider {

    private static NewbieProtectionAPI instance;

    private NewbieProtectionAPIProvider() {
    }

    public static void initialize(NewbieProtectionAPI instance) {
        NewbieProtectionAPIProvider.instance = instance;
    }

    public static NewbieProtectionAPI provide() {
        return instance;
    }

    public static void destroy() {
        instance = null;
    }
}
