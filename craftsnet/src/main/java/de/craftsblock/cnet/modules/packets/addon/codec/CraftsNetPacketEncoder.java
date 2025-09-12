package de.craftsblock.cnet.modules.packets.addon.codec;

import de.craftsblock.cnet.modules.packets.common.packet.Packet;
import de.craftsblock.cnet.modules.packets.common.packet.codec.PacketEncoder;
import de.craftsblock.craftsnet.api.websocket.codec.WebSocketSafeTypeEncoder;
import de.craftsblock.craftsnet.utils.ByteBuffer;

/**
 * A {@link WebSocketSafeTypeEncoder.TypeToByteBufferEncoder} implementation for {@link Packet} instances.
 * <p>
 * Wraps a {@link PacketEncoder} to convert packets into {@link ByteBuffer} instances
 * suitable for safe transmission over WebSocket connections.
 *
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.1.0
 */
public record CraftsNetPacketEncoder(PacketEncoder encoder) implements WebSocketSafeTypeEncoder.TypeToByteBufferEncoder<Packet> {

    /**
     * Encodes a {@link Packet} into a {@link ByteBuffer} using the underlying {@link PacketEncoder}.
     *
     * @param packet The packet to encode.
     * @return A ByteBuffer containing the encoded packet data.
     */
    @Override
    public ByteBuffer encode(Packet packet) {
        return encoder.encode(packet);
    }

}
