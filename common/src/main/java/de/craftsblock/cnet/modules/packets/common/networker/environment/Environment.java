package de.craftsblock.cnet.modules.packets.common.networker.environment;

import de.craftsblock.cnet.modules.packets.common.WebSocketPackets;
import de.craftsblock.cnet.modules.packets.common.packet.listener.PacketListenerRegistry;
import de.craftsblock.cnet.modules.packets.common.protocol.PacketBundleRegistry;
import de.craftsblock.craftscore.event.ListenerRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the environment or context in which a
 * {@link de.craftsblock.cnet.modules.packets.common.networker.Networker Networker}
 * operates.
 * <p>
 * Provides access to central components of the WebSocket packet system, such as
 * packet registries and listener registries. Implementations allow networkers
 * to interact with packets and event listeners without direct dependency on
 * specific packet system instances.
 * </p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.1.0
 */
public interface Environment {

    /**
     * Returns the main {@link WebSocketPackets} instance associated with this environment.
     *
     * @return The WebSocketPackets instance, never {@code null}.
     */
    @NotNull WebSocketPackets getWebSocketPackets();

    /**
     * Returns the {@link PacketBundleRegistry} from the {@link WebSocketPackets} instance.
     *
     * @return The packet bundle registry, never {@code null}.
     */
    default @NotNull PacketBundleRegistry getPacketBundleRegistry() {
        return getWebSocketPackets().getPacketBundleRegistry();
    }

    /**
     * Returns the {@link PacketListenerRegistry} from the {@link WebSocketPackets} instance.
     *
     * @return The packet listener registry, never {@code null}.
     */
    default @NotNull PacketListenerRegistry getPacketListenerRegistry() {
        return getWebSocketPackets().getPacketListenerRegistry();
    }

    /**
     * Returns the optional {@link ListenerRegistry} associated with this environment.
     * <p>
     * Can be {@code null} if no general listener registry is present.
     * </p>
     *
     * @return The listener registry, or {@code null} if not available.
     */
    @Nullable ListenerRegistry getListenerRegistry();

    /**
     * Checks whether a {@link ListenerRegistry} is present in this environment.
     *
     * @return {@code true} if a listener registry is available, {@code false} otherwise.
     */
    default boolean hasListenerRegistry() {
        return getListenerRegistry() != null;
    }

}
