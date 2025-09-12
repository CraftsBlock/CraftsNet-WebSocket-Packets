package de.craftsblock.cnet.modules.packets.addon;

import de.craftsblock.cnet.modules.packets.addon.autoregister.PacketListenerAutoRegisterHandler;
import de.craftsblock.cnet.modules.packets.addon.codec.CraftsNetPacketEncoder;
import de.craftsblock.cnet.modules.packets.addon.networking.CraftsNetEnvironment;
import de.craftsblock.cnet.modules.packets.common.WebSocketPackets;
import de.craftsblock.cnet.modules.packets.common.networker.environment.Environment;
import de.craftsblock.cnet.modules.packets.common.packet.codec.PacketEncoder;
import de.craftsblock.craftsnet.addon.Addon;
import de.craftsblock.craftsnet.addon.meta.annotations.Meta;
import org.jetbrains.annotations.NotNull;

/**
 * Addon integrating the {@link WebSocketPackets} into CraftsNet.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0
 */
@Meta(name = "WebSocketPackets")
public class WebSocketPacketsAddon extends Addon {

    private final @NotNull Environment environment;
    private final @NotNull WebSocketPackets webSocketPackets;

    /**
     * Constructs a new {@link WebSocketPacketsAddon}.
     */
    public WebSocketPacketsAddon() {
        this.environment = new CraftsNetEnvironment(this);
        this.webSocketPackets = new WebSocketPackets();
    }

    /**
     * Called when the addon is loaded.
     * <p>
     * Initializes the {@link WebSocketPackets} system, registers automatic
     * packet listener handlers, and registers a custom {@link PacketEncoder}.
     * </p>
     */
    @Override
    public void onLoad() {
        this.webSocketPackets.onLoad();
        this.getAutoRegisterRegistry().register(
                new PacketListenerAutoRegisterHandler(this.getCraftsNet(), this.webSocketPackets)
        );
        this.getWebSocketEncoderRegistry().register(new CraftsNetPacketEncoder(
                new PacketEncoder(this.webSocketPackets)
        ));
    }

    /**
     * Called when the addon is disabled.
     * <p>
     * Shuts down the {@link WebSocketPackets} system to release resources.
     * </p>
     */
    @Override
    public void onDisable() {
        this.webSocketPackets.onDisable();
    }

    /**
     * Returns the {@link Environment} provided by this addon.
     *
     * @return The environment instance, never {@code null}.
     */
    public @NotNull Environment getEnvironment() {
        return environment;
    }

    /**
     * Returns the {@link WebSocketPackets} system managed by this addon.
     *
     * @return The WebSocketPackets instance, never {@code null}.
     */
    public @NotNull WebSocketPackets getWebSocketPackets() {
        return webSocketPackets;
    }

}
