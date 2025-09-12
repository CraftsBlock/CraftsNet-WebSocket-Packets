package de.craftsblock.cnet.modules.packets.common.networker.environment;

import de.craftsblock.cnet.modules.packets.common.WebSocketPackets;
import de.craftsblock.craftscore.event.ListenerRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A simple implementation of the {@link Environment} interface.
 * <p>
 * Provides access to a {@link WebSocketPackets} instance and an optional
 * {@link ListenerRegistry}. This environment is typically used when no
 * complex context or additional dependencies are required.
 * </p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.1.0
 */
public record SimpleEnvironment(@NotNull WebSocketPackets webSocketPackets,
                                @Nullable ListenerRegistry listenerRegistry)
        implements Environment {

    /**
     * Constructs a new {@link SimpleEnvironment} without a {@link ListenerRegistry}.
     *
     * @param webSocketPackets The {@link WebSocketPackets} instance for this environment, never {@code null}.
     */
    public SimpleEnvironment(@NotNull WebSocketPackets webSocketPackets) {
        this(webSocketPackets, null);
    }

    /**
     * Returns the {@link WebSocketPackets} instance associated with this environment.
     *
     * @return The WebSocketPackets instance, never {@code null}.
     */
    @Override
    public @NotNull WebSocketPackets getWebSocketPackets() {
        return webSocketPackets();
    }

    /**
     * Returns the optional {@link ListenerRegistry} associated with this environment.
     *
     * @return The listener registry, or {@code null} if none is provided.
     */
    @Override
    public @Nullable ListenerRegistry getListenerRegistry() {
        return listenerRegistry();
    }

}
