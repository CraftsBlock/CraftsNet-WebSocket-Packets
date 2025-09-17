package de.craftsblock.cnet.modules.packets.common.networker.builtin;

import de.craftsblock.cnet.modules.packets.common.networker.Networker;
import de.craftsblock.cnet.modules.packets.common.networker.environment.Environment;
import de.craftsblock.cnet.modules.packets.common.packet.Packet;
import de.craftsblock.cnet.modules.packets.common.packet.codec.PacketDecoder;

import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Internal listener for handling WebSocket events.
 * <p>
 * This listener decodes incoming binary messages into {@link Packet} instances
 * and dispatches them to their handlers. It also manages connection state
 * and ping/pong responses.
 * </p>
 *
 * @param environment The environment providing packet system context.
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.1.0
 */
@SuppressWarnings("unused")
public record SimpleWebSocketListener(Environment environment) implements WebSocket.Listener {

    private static final PacketDecoder PACKET_DECODER = new PacketDecoder();
    private static final ConcurrentHashMap<WebSocket, WebSocketConnection> connections = new ConcurrentHashMap<>(1);

    /**
     * Called when a WebSocket connection is opened.
     * <p>
     * Creates a new {@link WebSocketConnection} for the WebSocket and stores it
     * in the static connections map.
     * </p>
     *
     * @param webSocket The WebSocket that was opened.
     */
    @Override
    public void onOpen(WebSocket webSocket) {
        WebSocketConnection connection = new WebSocketConnection(
                webSocket,
                new WebSocketNetworker(environment, webSocket),
                new AtomicReference<>()
        );
        connection.clearAccumulator();

        connections.put(webSocket, connection);
        WebSocket.Listener.super.onOpen(webSocket);
    }

    /**
     * Called when a WebSocket is closed.
     *
     * @param webSocket  The WebSocket that was closed.
     * @param statusCode The close status code.
     * @param reason     The reason for closure.
     * @return Completion stage for further processing.
     */
    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        this.onClose0(webSocket);
        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
    }

    /**
     * Called when a WebSocket encounters an error.
     *
     * @param webSocket The WebSocket with an error.
     * @param error     The throwable error.
     */
    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        WebSocket.Listener.super.onError(webSocket, error);
        WebSocketConnection connection = this.onClose0(webSocket);

        if (connection == null) throw new RuntimeException(error);
        throw new RuntimeException("Error in connection %s".formatted(connection.networker().getId()), error);
    }

    /**
     * Removes a connection from the connections map.
     *
     * @param webSocket The WebSocket whose connection should be removed.
     * @return The removed {@link WebSocketConnection}, or null if none existed.
     */
    WebSocketConnection onClose0(WebSocket webSocket) {
        return connections.remove(webSocket);
    }

    /**
     * Handles incoming binary WebSocket messages.
     * <p>
     * Accumulates partial messages, decodes complete messages into packets,
     * and dispatches them using {@link Packet#handle(Networker)}.
     * </p>
     *
     * @param webSocket The WebSocket receiving the binary message.
     * @param message   The message buffer.
     * @param last      Whether this is the last fragment.
     * @return Completion stage for further processing.
     */
    @Override
    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer message, boolean last) {
        WebSocketConnection connection = connections.get(webSocket);
        if (connection == null)
            throw new IllegalStateException("No ");

        byte[] data = new byte[message.remaining()];
        message.get(data);

        Networker networker = connection.networker();
        ByteBuffer accumulator = connection.ensureCapacityAndGetAccumulator(data.length);
        accumulator.put(data);

        if (!last) return WebSocket.Listener.super.onBinary(webSocket, message, false);

        accumulator.flip();
        byte[] packetData = new byte[accumulator.remaining()];
        accumulator.get(packetData);

        try {
            Packet packet = PACKET_DECODER.decode(new de.craftsblock.craftsnet.utils.ByteBuffer(packetData));
            packet.handle(networker);

            return WebSocket.Listener.super.onBinary(webSocket, message, true);
        } finally {
            connection.clearAccumulator();
        }
    }

    /**
     * Handles incoming ping frames by responding with a pong.
     *
     * @param webSocket The WebSocket receiving the ping.
     * @param message   The ping payload.
     * @return Completion stage for further processing.
     */
    @Override
    public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
        webSocket.sendPong(message).join();
        return WebSocket.Listener.super.onPing(webSocket, message);
    }

    /**
     * Retrieves the {@link Networker} associated with a WebSocket.
     *
     * @param webSocket The WebSocket.
     * @return The networker managing this WebSocket.
     */
    public static Networker getNetworkerFor(WebSocket webSocket) {
        return connections.get(webSocket).networker();
    }

}