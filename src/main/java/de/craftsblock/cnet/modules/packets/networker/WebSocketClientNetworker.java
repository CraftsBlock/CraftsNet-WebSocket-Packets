package de.craftsblock.cnet.modules.packets.networker;

import de.craftsblock.cnet.modules.packets.WebSocketPackets;
import de.craftsblock.cnet.modules.packets.packet.Packet;
import de.craftsblock.craftscore.utils.id.Snowflake;
import de.craftsblock.craftsnet.api.websocket.WebSocketClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * A {@link Networker} implementation for {@link WebSocketClient} connections.
 * <p>
 * Each {@code WebSocketClientNetworker} is assigned a unique ID (generated using {@link Snowflake})
 * and acts as a bridge between the {@link WebSocketPackets} environment and an active
 * {@link WebSocketClient}. It provides methods to send packets and to manage the
 * connection lifecycle, including disconnecting with or without custom codes and reasons.
 *
 * @param id               The unique identifier of this networker instance.
 * @param webSocketPackets The {@link WebSocketPackets} environment this networker belongs to.
 * @param client           The underlying {@link WebSocketClient} connection.
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see Networker
 * @see WebSocketClient
 * @see Packet
 * @since 1.0.0
 */
public record WebSocketClientNetworker(long id, WebSocketPackets webSocketPackets, WebSocketClient client) implements Networker {

    /**
     * Creates a new {@link WebSocketClientNetworker} with a generated unique ID.
     *
     * @param webSocketPackets The {@link WebSocketPackets} environment this networker belongs to.
     * @param client           The underlying {@link WebSocketClient}.
     */
    public WebSocketClientNetworker(WebSocketPackets webSocketPackets, WebSocketClient client) {
        this(Snowflake.generate(), webSocketPackets, client);
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

    /**
     * Returns the {@link WebSocketPackets} environment this networker belongs to.
     *
     * @return The environment instance.
     */
    @Override
    public WebSocketPackets getEnvironment() {
        return webSocketPackets();
    }

}
