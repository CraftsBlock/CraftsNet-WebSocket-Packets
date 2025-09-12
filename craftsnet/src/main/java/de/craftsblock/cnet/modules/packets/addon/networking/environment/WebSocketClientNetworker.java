package de.craftsblock.cnet.modules.packets.addon.networking.environment;

import de.craftsblock.cnet.modules.packets.common.networker.Networker;
import de.craftsblock.cnet.modules.packets.common.networker.environment.Environment;
import de.craftsblock.cnet.modules.packets.common.packet.Packet;
import de.craftsblock.craftscore.utils.id.Snowflake;
import de.craftsblock.craftsnet.api.websocket.WebSocketClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public record WebSocketClientNetworker(long id, Environment environment, WebSocketClient client) implements Networker {

    public WebSocketClientNetworker(Environment environment, WebSocketClient client) {
        this(Snowflake.generate(), environment, client);
    }

    /**
     * Sends a {@link Packet} to the client through the underlying {@link WebSocketClient}.
     *
     * @param packet The packet to send.
     */
    @Override
    public void send(@NotNull Packet packet) {
        client().sendMessage(packet);
    }

    /**
     * Disconnects the client gracefully without a specific close code or reason.
     */
    @Override
    public void disconnect() {
        client().close();
    }

    /**
     * Disconnects the client using the given close code and reason.
     *
     * @param code   The WebSocket close code (between 1000 and 4999).
     * @param reason The textual reason for the disconnection.
     */
    @Override
    public void disconnect(@Range(from = 1000, to = 4999) int code, @NotNull String reason) {
        client.close(code, reason);
    }

    /**
     * Returns the unique identifier of this networker instance.
     *
     * @return The unique ID.
     */
    @Override
    public long getId() {
        return id();
    }

    @Override
    public Environment getEnvironment() {
        return environment();
    }

}
