package de.craftsblock.cnet.modules.packets.packet.listener;

/**
 * A marker interface for classes that handle {@code Packet} events.
 * <p>
 * Implementations of this interface define the logic for reacting to
 * specific packets. By default, if a packet type is not handled,
 * calling the default method {@link #throwNotSpecified()} will result
 * in an {@link UnsupportedOperationException}.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0
 */
public interface PacketListener {

    /**
     * Throws an {@link UnsupportedOperationException} to indicate that
     * packet handling has not been defined.
     *
     * @throws UnsupportedOperationException Always thrown when invoked.
     */
    default void throwNotSpecified() {
        throw new UnsupportedOperationException("Packet handling not specified!");
    }

}
