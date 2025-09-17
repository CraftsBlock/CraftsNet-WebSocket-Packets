package de.craftsblock.cnet.modules.packets.common.packet;

import de.craftsblock.craftsnet.utils.ByteBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Packet} that wraps an {@link EntityPacket.Entity} instance.
 * <p>
 * This interface provides default serialization logic by delegating
 * the {@link #write(ByteBuffer)} method to the wrapped entity.
 * Entities are uniquely identified and can be sent over the network as packets.
 *
 * @param <E> The type of {@link Entity} wrapped by this packet.
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see Packet
 * @see BufferWritable
 * @see EntityPacket.Entity
 * @since 1.0.0
 */
public interface EntityPacket<E extends EntityPacket.Entity> extends Packet {

    /**
     * Serializes the wrapped entity into the provided buffer.
     *
     * @param buffer The buffer to write the entity data into.
     */
    @Override
    default void write(@NotNull ByteBuffer buffer) {
        getEntity().write(buffer);
    }

    /**
     * Retrieves the entity instance wrapped by this packet.
     *
     * @return The entity instance.
     */
    E getEntity();

    /**
     * Represents an entity that can be serialized as part of an {@link EntityPacket}.
     * <p>
     * Entities must have a unique ID and implement {@link BufferWritable}
     * to allow serialization for network transmission.
     */
    interface Entity extends BufferWritable {

        /**
         * Returns the unique identifier of this entity.
         *
         * @return The entity ID.
         */
        long getId();

    }

}
