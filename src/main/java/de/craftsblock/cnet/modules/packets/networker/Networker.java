package de.craftsblock.cnet.modules.packets.networker;

import de.craftsblock.cnet.modules.packets.WebSocketPackets;
import de.craftsblock.cnet.modules.packets.packet.Packet;
import de.craftsblock.craftsnet.CraftsNet;
import de.craftsblock.craftsnet.api.websocket.ClosureCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * The {@code Networker} interface defines the contract for an entity
 * participating in WebSocket based communication within the
 * {@link WebSocketPackets} addon.
 * <p>
 * A {@code Networker} typically represents a single logical connection
 * (e.g. a {@link de.craftsblock.craftsnet.api.websocket.WebSocketClient WebSocketClient}
 * or {@link java.net.http.WebSocket WebSocket}) that can send
 * {@link Packet packets} and manage its connection lifecycle.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Networker {

    /**
     * Sends a {@link Packet} to this network connection.
     *
     * @param packet The packet to send, never {@code null}
     */
    void send(@NotNull Packet packet);

    /**
     * Disconnects the network connection immediately,
     * using a default closure code and no explicit reason.
     */
    void disconnect();

    /**
     * Disconnects the network connection with the given textual reason,
     * using the {@link ClosureCode#NORMAL} closure code by default.
     *
     * @param reason The reason for disconnecting, never {@code null}
     */
    default void disconnect(@NotNull String reason) {
        disconnect(ClosureCode.NORMAL, reason);
    }

    /**
     * Disconnects the network connection with the given closure code and reason.
     *
     * @param code   The closure code to use, never {@code null}
     * @param reason The reason for disconnecting, never {@code null}
     */
    default void disconnect(@NotNull ClosureCode code, @NotNull String reason) {
        disconnect(code.intValue(), reason);
    }

    /**
     * Disconnects the network connection with the specified closure code and reason.
     *
     * @param code   The numeric closure code to send, in range 1000–4999
     * @param reason The reason for disconnecting, never {@code null}
     */
    void disconnect(@Range(from = 1000, to = 4999) int code, @NotNull String reason);

    /**
     * Returns the unique identifier of this network connection.
     *
     * @return A unique connection ID
     */
    long getId();

    /**
     * Returns the {@link WebSocketPackets} addon environment
     * this {@code Networker} belongs to.
     *
     * @return The associated addon environment
     */
    WebSocketPackets getEnvironment();

    /**
     * Convenience method to return the global {@link CraftsNet} instance
     * from the enclosing addon environment.
     *
     * @return The {@link CraftsNet} instance
     */
    default CraftsNet getCraftsNet() {
        return getEnvironment().getCraftsNet();
    }

}
