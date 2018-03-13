package BotFrame.Listeners;

import BotFrame.Parser;
import Levels.Level;
import Utility.Model.CommandBox;
import Utility.Model.Server;
import Utility.Servers;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        Server server = Servers.activeServers.get(event.getGuild().getIdLong());
        if (server.drop) {
            return;
        }

        if (server.getFunction("level").isEnabled()) {
            Level.handle(event);
        }

        if (event.getMessage().getContentRaw().startsWith(server.getSetting("prefix"))) {
            CommandBox command = Parser.parse(server, event);
            if (server.getCommandStatus(command.getInvoke().toLowerCase())) {
                server.getCommand(command.getInvoke().toLowerCase())
                        .log(server.getCommand(command.getInvoke().toLowerCase()).execute(command), command);
            }
        }
    }
}
