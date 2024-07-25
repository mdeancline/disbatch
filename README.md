# Disbatch
Disbatch is a versatile Minecraft server command library, meticulously designed to foster modularity and further streamline the handling of command arguments. With Disbatch, developers can effortlessly create and manage commands, empowering them to build robust and user-friendly commands. Its modular architecture facilitates the seamless integration of new commands and functionalities. Whether it's executing complex commands or managing intricate command structures, Disbatch offers a comprehensive toolkit that simplifies the development process involving commands.

# Installation
For the time being, Disbatch is only available for Spigot.

To add the libary to your project, add the following to your `pom.xml` file.
1. Add the following to your `<repositories>`:
```xml
<repository>
    <id>repsy</id>
    <name>mc-libs-repo</name>
    <url>https://repo.repsy.io/mvn/mdcline/mc-libs/</url>
</repository>
```
2. Add the following to your `<dependencies>`:
```xml
<dependency>
    <groupId>io.github.disbatch</groupId>
    <artifactId>disbatch-spigot</artifactId>
    <version>Insert version here</version> <!-- See the pom.xml in disbatch-spigot for the latest version -->
    <scope>compile</scope>
</dependency>
```

# Getting Started
Disbatch has its own `Command` interface for creating your own commands and catering to a certain `CommandSender` type if necessary. The same goes for all of the other abstractions and subclasses derived from it. If you wish to dynamically create a `Command`, a `CommandBuilder` can be utilized to define how you want the built `Command` to be executed and whether you want it to utilize tab completion. Below is an example of creating a simple command that will message a player.

Implementing the `Command` interface:
```java
import io.github.command.io.github.disbatch.Command;
import io.github.command.io.github.disbatch.CommandInput;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCommand implements Command<Player> {
    @Override
    public void execute(final Player sender, final @NotNull CommandInput input) {
        sender.sendMessage("Hello there, " + sender.getName());
    }
}
```
```java
final Command<Player> playerCmd = new PlayerCommand();
```

Utilizing a `CommandBuilder`:
```java
final Command<Player> playerCmd = new Command.Builder<Player>()
        .executor((sender, input) -> sender.sendMessage("Hello there, " + sender.getName()))
        .build();
```

# Registering Commands
For a command to be recognized by the server, it must be registered. Commands created with the Disbatch library can be registered without your project containing a `plugin.yml` file.

Using the previous player command example, below are examples of registering a command whether a `plugin.yml` file is present or not. Note that both examples assume you are running the code inside your plugin's `onEnable` method.

Without it:
```java
final CommandRegistrar registrar = CommandRegistrars.getCompatibleRegistrar(this);
registrar.register(playerCmd, "player");
```

With it:
```java
final CommandRegistrar registrar = CommandRegistrars.getCompatibleRegistrar(this);
registrar.registerFromFile(playerCmd, "player");
```

Either of these utility methods will register a command to the server so that it can be executed when `/player` is typed in the chat. However, this will not work if `player` is typed in the server's console, as the `CommandSender` generic type defined in both creation examples is aimed at a `Player`. If you wish to target every `CommandSender`, simply specify `CommandSender` as the generic type argument when implementing the interface or extending a related abstraction, or pass a generic wildcard when utilizing a `CommandBuilder`.
