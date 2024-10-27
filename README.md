![ptrlNewbieProtection](https://github.com/user-attachments/assets/58f71aab-ecce-449c-9b76-949eacaf7ffc)
# 🛡️ ptrlNewbieProtection - New Player Protection for Minecraft Servers

**ptlNewbieProtection** is a lightweight plugin providing temporary protection for new players on Minecraft servers. It operates entirely in-memory, relying on server data without needing an external database.

## How It Works

- **New Players**: When a player joins for the first time, they automatically receive temporary protection to safely explore the server.
- **Returning Players**: If the player has been on the server before, no protection is applied.

This ensures new players can get started without immediate risk from other players.

## Key Features

- **In-Memory**: Efficiently runs without any database dependency.
- **Lightweight**: Minimal server impact.
- **Easy Integration**: Simple to install and use.

## API

For integration, use the following dependency in your project:

**Repository**: [repo.piotrulla.dev/releases](https://repo.piotrulla.dev/releases)

Maven
```xml
<dependency>
    <groupId>dev.piotrulla.newbieprotection</groupId>
    <artifactId>newbieprotection-api</artifactId>
    <version>1.0.0</version>
</dependency>
```

Gradle
```xml
compileOnly("dev.piotrulla.newbieprotection:newbieprotection-api:1.0.0")

