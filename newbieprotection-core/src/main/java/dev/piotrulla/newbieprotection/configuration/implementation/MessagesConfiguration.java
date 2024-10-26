package dev.piotrulla.newbieprotection.configuration.implementation;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

public class MessagesConfiguration extends OkaeriConfig {

    public Notice newbieOnJoinMessage = Notice.chat(
            "&eYou are newbie, you have &c3 minutes &eto get protection from other players"
    );

    public Notice protectionEnd = Notice.chat(
            "&eYour protection has ended, stay safe!"
    );

    public Notice reminderTask = Notice.actionbar(
            "&eYou have &c{TIME} &eto get protection from other players"
    );

    public Notice cantAttackProtected = Notice.chat(
            "&cYou can't attack protected player!", "&eWait &c{TIME} &euntil his protection ends"
    );

    public Notice cantAttackWhenProtected = Notice.chat(
            "&cYou can't attack when you are protected! &eUse &c/newbieprotection remove &eto cancel protection"
    );

    public Commands commands = new Commands();

    public static class Commands extends OkaeriConfig {

        public Notice noPermission = Notice.chat("&cYou dont have permissions! &7({PERMISSIONS})");

        public Notice invalidUsageOne = Notice.chat("&6Correct usage: &e{USAGE}");

        public Notice invalidUsageMany = Notice.chat("&7Correct usages:");

        public Notice invalidUsageManyItem = Notice.chat("&e{USAGE}");
    }


    public Command command = new Command();

    public static class Command extends OkaeriConfig{

        public Notice infoCommand = Notice.chat(
                "&eYou are protected!",
                "&eRemaining protection time: &c{TIME}"
        );

        public Notice notProtected = Notice.chat(
                "&eYou no longer have protection!"
        );

        public Notice protectionRemoved = Notice.chat(
                "&cProtection removed, stay safe!"
        );

        public Admin admin = new Admin();

        public static class Admin extends OkaeriConfig {

            public Notice protectionAdded = Notice.chat("&c{PLAYER} &ereceived protection on &c{TIME}&e!");

            public Notice protectionRemoved = Notice.chat("&c{PLAYER} &elost protection!");

            public Notice protectionInfo = Notice.chat(
                    "&e{PLAYER} is protected!",
                    "&eRemaining protection time: &c{TIME}"
            );

            public Notice notProtected = Notice.chat(
                    "&c{PLAYER} &eis not protected!"
            );
        }
    }
}
