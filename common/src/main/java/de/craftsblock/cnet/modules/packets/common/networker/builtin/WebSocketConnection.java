package de.craftsblock.cnet.modules.packets.common.networker.builtin;

import de.craftsblock.cnet.modules.packets.common.networker.Networker;

import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a {@link WebSocket} connection along with an associated {@link Networker}
 * and accumulator buffer for packet assembly.
 *
 * @param webSocket   The underlying WebSocket.
 * @param networker   The networker wrapper managing this WebSocket.
 * @param accumulator An atomic reference holding the accumulator buffer.
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.1.0
 */
record WebSocketConnection(WebSocket webSocket, Networker networker, AtomicReference<ByteBuffer> accumulator) {

    /**
     * Clears and resets the accumulator buffer.
     */
    synchronized void clearAccumulator() {
        accumulator.set(ByteBuffer.allocate(32));
    }

    /**
     * Ensures the accumulator buffer has enough capacity for additional data
     * and returns it.
     *
     * @param additionalCapacity The number of bytes to accommodate.
     * @return The buffer ready for writing additional data.
     */
    synchronized ByteBuffer ensureCapacityAndGetAccumulator(int additionalCapacity) {
        ByteBuffer accumulator = accumulator().get();
        if (accumulator.remaining() >= additionalCapacity)
            return accumulator;

        ByteBuffer expandedBuffer = ByteBuffer.allocate(accumulator.capacity() + additionalCapacity);
        accumulator.flip();
        expandedBuffer.put(accumulator);

        return expandedBuffer;
    }

}
