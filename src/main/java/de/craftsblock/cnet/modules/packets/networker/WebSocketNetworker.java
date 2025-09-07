package de.craftsblock.cnet.modules.packets.networker;

import de.craftsblock.cnet.modules.packets.WebSocketPackets;
import de.craftsblock.cnet.modules.packets.packet.Packet;
import de.craftsblock.cnet.modules.packets.packet.codec.PacketEncoder;
import de.craftsblock.craftscore.utils.id.Snowflake;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.net.http.WebSocket;
import java.nio.ByteBuffer;

/**
 * A {@link Networker} implementation for Java's {@link WebSocket} client API.
 * <p>
 * Each {@code WebSocketNetworker} is assigned a unique ID (generated using {@link Snowflake})
 * and provides functionality for sending packets and managing the WebSocket connection lifecycle.
 * <p>
 * Packets are encoded using {@link PacketEncoder} before being transmitted as binary messages.
 *
 * @param id               The unique identifier of this networker instance.
 * @param webSocketPackets The {@link WebSocketPackets} environment this networker belongs to.
 * @param webSocket        The underlying Java {@link WebSocket} connection.
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see Networker
 * @see Packet
 * @see WebSocket
 * @since 1.0.0
 */
public record WebSocketNetworker(long id, WebSocketPackets webSocketPackets, WebSocket webSocket) implements Networker {

    /**
     * Creates a new {@link WebSocketNetworker} with a generated unique ID.
     *
     * @param webSocketPackets The {@link WebSocketPackets} environment this networker belongs to.
     * @param webSocket        The underlying Java {@link WebSocket}.
     */
    public WebSocketNetworker(WebSocketPackets webSocketPackets, WebSocket webSocket) {
        this(Snowflake.generate(), webSocketPackets, webSocket);
    }

    /**
     * Sends a {@link Packet} to the client through the underlying {@link WebSocket}.
     * <p>
     * The packet is first encoded using {@link PacketEncoder} and then transmitted as a binary frame.
     *
     * @param packet The packet to send.
     */
    @Override
    public void send(@NotNull Packet packet) {
        PacketEncoder packetEncoder = new PacketEncoder(webSocketPackets);
        ByteBuffer message = ByteBuffer.wrap(packetEncoder.encode(packet).getSource());
        webSocket().sendBinary(message, true);
    }

    /**
     * Disconnects the client gracefully using close code {@code 1000} (normal closure)
     * and an empty reason.
     */
    @Override
    public void disconnect() {
        webSocket().sendClose(1000, "");
    }

    /**
     * Disconnects the client using the given close code and reason.
     *
     * @param code   The WebSocket close code (between 1000 and 4999).
     * @param reason The textual reason for the disconnection.
     */
    @Override
    public void disconnect(@Range(from = 1000, to = 4999) int code, @NotNull String reason) {
        webSocket().sendClose(code, reason);
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
