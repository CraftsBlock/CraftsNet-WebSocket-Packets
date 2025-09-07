package de.craftsblock.cnet.modules.packets.packet.codec;

import de.craftsblock.cnet.modules.packets.WebSocketPackets;
import de.craftsblock.cnet.modules.packets.packet.Packet;
import de.craftsblock.cnet.modules.packets.packet.WrappedPacket;
import de.craftsblock.cnet.modules.packets.protocol.PacketBundle;
import de.craftsblock.craftsnet.api.websocket.Frame;
import de.craftsblock.craftsnet.api.websocket.codec.WebSocketSafeTypeDecoder;
import de.craftsblock.craftsnet.utils.ByteBuffer;

/**
 * Decodes {@link Packet} instances from {@link Frame} or {@link ByteBuffer}
 * for WebSocket based communication.
 * <p>
 * This decoder reads the bundle identifier and packet ID from the buffer to
 * retrieve the appropriate {@link PacketBundle} and reconstruct the packet.
 * If the bundle is unknown, it wraps the buffer into a {@link WrappedPacket}.
 * Packet size is validated against {@link PacketEncoder#MAX_PACKET_SIZE}.
 * <p>
 * This implementation ensures safe and type consistent deserialization of
 * network packets within the {@link WebSocketPackets} addon system.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see Packet
 * @see WrappedPacket
 * @see PacketBundle
 * @see WebSocketSafeTypeDecoder
 * @since 1.0.0
 */
public final class PacketDecoder implements WebSocketSafeTypeDecoder<Packet> {

    /**
     * Decodes a {@link Packet} from a {@link Frame}.
     *
     * @param frame The WebSocket frame containing the packet data.
     * @return The decoded {@link Packet}.
     */
    @Override
    public Packet decode(Frame frame) {
        return decode(frame.getBuffer());
    }

    /**
     * Decodes a {@link Packet} from a {@link ByteBuffer}.
     * <p>
     * Reads the bundle identifier and packet ID from the buffer, checks for
     * oversized packets, and delegates to the appropriate {@link PacketBundle}
     * to reconstruct the packet. If no bundle is found, a {@link WrappedPacket}
     * is created.
     *
     * @param buffer The buffer containing the packet data.
     * @return The decoded {@link Packet}.
     * @throws IllegalStateException If the packet exceeds {@link PacketEncoder#MAX_PACKET_SIZE}.
     */
    public Packet decode(ByteBuffer buffer) {
        WebSocketPackets webSocketPackets = WebSocketPackets.getInstanceSafely();

        String identifier = buffer.readUTF();
        PacketBundle packetBundle = webSocketPackets.getPacketBundleRegistry().getBundle(identifier);
        if (packetBundle == null)
            return new WrappedPacket(buffer);

        int id = buffer.readVarInt();

        if (buffer.size() > PacketEncoder.MAX_PACKET_SIZE)
            throw new IllegalStateException("Packet %s#%s exceeded max size! (Got: %s, Max: %s)".formatted(
                    identifier, id, buffer.size(), PacketEncoder.MAX_PACKET_SIZE
            ));

        ByteBuffer packet = new ByteBuffer(buffer.readRemaining(), true);
        return packetBundle.createPacket(id, packet);
    }

}
