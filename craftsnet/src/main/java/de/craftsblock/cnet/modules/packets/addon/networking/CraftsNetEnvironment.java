package de.craftsblock.cnet.modules.packets.addon.networking;

import de.craftsblock.cnet.modules.packets.addon.WebSocketPacketsAddon;
import de.craftsblock.cnet.modules.packets.common.WebSocketPackets;
import de.craftsblock.cnet.modules.packets.common.networker.environment.Environment;
import de.craftsblock.craftscore.event.ListenerRegistry;
import de.craftsblock.craftsnet.CraftsNet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Environment implementation backed by a {@link WebSocketPacketsAddon}.
 * <p>
 * Provides access to the WebSocket packet system, the optional {@link ListenerRegistry},
 * and the underlying {@link CraftsNet} instance. Ensures that the environment
 * is fully qualified and initialized with a valid {@link CraftsNet} instance.
 * </p>
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0
 */
public record CraftsNetEnvironment(WebSocketPacketsAddon addon) implements Environment {

    /**
     * Constructs a new {@link CraftsNetEnvironment}.
     * <p>
     * Validates that the provided addon has a fully initialized {@link CraftsNet} instance.
     * </p>
     *
     * @param addon The WebSocketPacketsAddon providing the environment context.
     * @throws IllegalStateException if the addon does not contain a valid CraftsNet instance.
     */
    public CraftsNetEnvironment {
        if (addon().getCraftsNet() == null)
            throw new IllegalStateException("Not a fully qualified craftsnet environment!");
    }

    /**
     * Returns the {@link WebSocketPackets} instance managed by the addon.
     *
     * @return The WebSocketPackets instance, never {@code null}.
     */
    @Override
    public @NotNull WebSocketPackets getWebSocketPackets() {
        return addon().getWebSocketPackets();
    }

    /**
     * Returns the optional {@link ListenerRegistry} from the addon.
     *
     * @return The listener registry, or {@code null} if none is provided.
     */
    @Override
    public @Nullable ListenerRegistry getListenerRegistry() {
        return addon().getListenerRegistry();
    }

    /**
     * Returns the underlying {@link CraftsNet} instance associated with the addon.
     *
     * @return The CraftsNet instance, never {@code null}.
     */
    public CraftsNet getCraftsNet() {
        return addon().getCraftsNet();
    }

}
