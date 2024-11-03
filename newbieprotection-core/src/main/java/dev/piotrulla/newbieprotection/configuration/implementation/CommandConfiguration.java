package dev.piotrulla.newbieprotection.configuration.implementation;

import dev.piotrulla.newbieprotection.configuration.implementation.command.Command;
import dev.piotrulla.newbieprotection.configuration.implementation.command.SubCommand;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandConfiguration extends OkaeriConfig {

    @Comment({
            "# This file allows you to configure commands.",
            "# You can change command name, aliases and permissions.",
            "# You can edit the commands as follows this template:",
            "# commands:",
            "#   <command_name>:",
            "#     name: \"<new_command_name>\"",
            "#     aliases:",
            "#       - \"<new_command_aliases>\"",
            "#     permissions:",
            "#       - \"<new_command_permission>\"",
            "#     subCommands:",
            "#       <default_sub_command_name>:",
            "#         name: <new_sub_command_name>",
            "#         disabled: false/true",
            "#         aliases:",
            "#           - \"<new_sub_command_aliases>\"",
            "#         permissions:",
            "#           - \"<new_sub_command_permission>\"",
    })

    public Map<String, Command> commands = Map.of(
            "newbieprotection", new Command(
                    "newbieprotection",
                    List.of("np"),
                    new ArrayList<>(),
                    Map.of("remove", new SubCommand("remove", true, List.of("off"), new ArrayList<>())),
                    true
            ),

            "newbieprotectionadmin", new Command(
                    "newbieprotectionadmin",
                    List.of("npa"),
                    Collections.singletonList("ptrlNewbieProtection.admin"),
                    Map.of(
                            "add", new SubCommand("add", true, List.of("on"), Collections.singletonList("ptrlNewbieProtection.on")),
                            "remove", new SubCommand("remove", true, List.of("off"), Collections.singletonList("ptrlNewbieProtection.off")),
                            "info", new SubCommand("info", true, List.of("inf"), Collections.singletonList("ptrlNewbieProtection.info"))
                    ),
                    true
            )
    );
}
