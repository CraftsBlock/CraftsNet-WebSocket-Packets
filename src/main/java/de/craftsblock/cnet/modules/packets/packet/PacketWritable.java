package de.craftsblock.cnet.modules.packets.packet;

import de.craftsblock.craftsnet.utils.ByteBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a data structure that can be serialized into a {@link ByteBuffer}.
 * <p>
 * Implementing classes define how their internal data is written to the buffer for
 * network transmission. It is commonly used in combination with {@link Packet}
 * implementations to separate serialization from processing behavior.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see ByteBuffer
 * @since 1.0.0
 */
public interface PacketWritable {

    /**
     * Serializes the implementing object into the provided {@link ByteBuffer}.
     *
     * @param buffer The buffer to write the object's data into.
     */
    void write(@NotNull ByteBuffer buffer);

}
