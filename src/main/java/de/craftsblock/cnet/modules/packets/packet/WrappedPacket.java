package de.craftsblock.cnet.modules.packets.packet;

import de.craftsblock.cnet.modules.packets.networker.Networker;
import de.craftsblock.cnet.modules.packets.protocol.PacketBundle;
import de.craftsblock.craftsnet.utils.ByteBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a packet that could not be matched to a known {@link PacketBundle}
 * or is otherwise wrapped for transmission purposes.
 * <p>
 * The {@link WrappedPacket} stores the raw bundle identifier, packet ID, and
 * packet data bytes. It provides serialization through {@link #write(ByteBuffer)}
 * but cannot be processed via {@link #handle(Networker)} since the underlying
 * packet type is unknown.
 * <p>
 * This is typically used when decoding packets from external or unknown sources
 * to ensure no data is lost, even if the specific packet type cannot be instantiated.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see Packet
 * @see ByteBuffer
 * @since 1.0.0
 */
public record WrappedPacket(String bundle, int id, byte[] data) implements Packet {

    /**
     * Constructs a {@link WrappedPacket} by reading from a {@link ByteBuffer}.
     *
     * @param buffer The buffer containing the serialized wrapped packet data.
     */
    public WrappedPacket(ByteBuffer buffer) {
        // Needs to read the hole packet as the decoder does not read anything
        this(buffer.readUTF(), buffer.readVarInt(), buffer.readRemaining());
    }

    /**
     * Writes the raw packet data into the provided buffer.
     * <p>
     * Note that the bundle and ID are not written here as they are handled by the encoder.
     *
     * @param buffer The buffer to write the raw packet data into.
     */
    @Override
    public void write(@NotNull ByteBuffer buffer) {
        // No need to write the bundle and id as the encoder does that
        buffer.write(data);
    }

    /**
     * Throws {@link UnsupportedOperationException} as wrapped packets cannot be handled.
     *
     * @param networker The networker that received the packet.
     * @throws UnsupportedOperationException Always thrown because wrapped packets cannot be processed.
     */
    @Override
    public void handle(Networker networker) {
        throw new UnsupportedOperationException("Can not handle wrapped / unknown packet!");
    }

}
