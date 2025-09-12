package de.craftsblock.cnet.modules.packets.common.packet;

import de.craftsblock.cnet.modules.packets.common.networker.Networker;
import de.craftsblock.cnet.modules.packets.common.networker.environment.Environment;
import de.craftsblock.craftscore.event.Event;
import de.craftsblock.craftscore.event.ListenerRegistry;
import de.craftsblock.craftsnet.utils.ByteBuffer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Represents a {@link Packet} that wraps a {@link Event} which must also be
 * {@link PacketWritable}.
 * <p>
 * This interface allows events to be sent over the network as packets. It provides
 * default implementations for {@link #write(ByteBuffer)} and {@link #handle(Networker)},
 * delegating serialization to the underlying event and firing the event through the
 * networker's listener registry when handled.
 * <p>
 * Implementing classes must provide the event instance via {@link #getEvent()}.
 *
 * @param <E> The type of {@link Event} that is also {@link PacketWritable}.
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see Packet
 * @see PacketWritable
 * @see Event
 * @since 1.0.0
 */
public interface EventPacket<E extends Event & PacketWritable> extends Packet {

    /**
     * Serializes the wrapped event into the provided buffer.
     *
     * @param buffer The buffer to write the event data into.
     */
    @Override
    default void write(@NotNull ByteBuffer buffer) {
        getEvent().write(buffer);
    }

    /**
     * Handles the event packet by firing the underlying event through the networker's
     * listener registry. Any reflection related exceptions during invocation are
     * wrapped in a {@link RuntimeException}.
     *
     * @param networker The networker responsible for handling the packet.
     * @throws RuntimeException If the event could not be fired due to reflection errors.
     */
    @Override
    default void handle(Networker networker) {
        Event event = getEvent();

        try {
            Environment environment = networker.getEnvironment();
            if (!environment.hasListenerRegistry())
                throw new UnsupportedOperationException("Received event in environment without listener registry!");

            environment.getListenerRegistry().call(event);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Could not fire event %s from network!"
                    .formatted(event.getClass().getName()), e);
        }
    }

    /**
     * Retrieves the wrapped event instance.
     *
     * @return The event instance that is sent as a packet.
     */
    E getEvent();

}
