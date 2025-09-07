package de.craftsblock.cnet.modules.packets;

import de.craftsblock.cnet.modules.packets.packet.codec.PacketEncoder;
import de.craftsblock.cnet.modules.packets.packet.listener.PacketListenerRegistry;
import de.craftsblock.cnet.modules.packets.protocol.PacketBundleRegistry;
import de.craftsblock.craftsnet.addon.Addon;
import de.craftsblock.craftsnet.addon.meta.annotations.Meta;
import org.jetbrains.annotations.NotNull;

/**
 * {@code WebSocketPackets} is a CraftsNet addon that integrates a
 * WebSocket based packet handling system into the framework.
 * <p>
 * This addon registers a custom {@link PacketEncoder} into the
 * WebSocket encoder registry of CraftsNet, enabling the encoding
 * of {@link de.craftsblock.cnet.modules.packets.packet.Packet Packet}
 * based messages.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see Addon
 * @see PacketEncoder
 * @see PacketListenerRegistry
 * @see PacketBundleRegistry
 * @since 1.0.0
 */
@Meta(name = "WebSocketPackets")
public class WebSocketPackets extends Addon {

    private static WebSocketPackets instance;

    private PacketListenerRegistry packetListenerRegistry;
    private PacketBundleRegistry packetBundleRegistry;

    /**
     * Called when the addon is loaded by the CraftsNet framework.
     * <p>
     * Initializes the singleton instance, registers the {@link PacketEncoder}
     * and initializes the packet listener and bundle registries.
     */
    @Override
    public void onLoad() {
        if (instance != null)
            throw new IllegalStateException("The websocket packet addon can not be loaded twice by the same class loader!");

        instance = this;

        getWebSocketEncoderRegistry().register(new PacketEncoder(this));

        this.packetListenerRegistry = new PacketListenerRegistry();
        this.packetBundleRegistry = new PacketBundleRegistry();
    }

    /**
     * Called when the addon is disabled.
     * <p>
     * Clears the singleton instance to allow reloading.
     */
    @Override
    public void onDisable() {
        instance = null;
    }

    /**
     * Returns the {@link PacketListenerRegistry} associated with this addon.
     *
     * @return The packet listener registry instance
     */
    public PacketListenerRegistry getPacketListenerRegistry() {
        return packetListenerRegistry;
    }

    /**
     * Returns the {@link PacketBundleRegistry} associated with this addon.
     *
     * @return The packet bundle registry instance
     */
    public PacketBundleRegistry getPacketBundleRegistry() {
        return packetBundleRegistry;
    }

    /**
     * Returns the singleton instance of this addon.
     *
     * @return The singleton {@link WebSocketPackets} instance, or {@code null} if not loaded.
     */
    public static WebSocketPackets getInstance() {
        return instance;
    }

    /**
     * Returns the singleton instance of this addon, throwing an exception if not loaded.
     *
     * @return The singleton {@link WebSocketPackets} instance.
     * @throws IllegalStateException If the addon has not been loaded.
     */
    public static @NotNull WebSocketPackets getInstanceSafely() {
        loadedOrThrow();
        return instance;
    }

    /**
     * Ensures the addon has been loaded, throwing an exception if it has not.
     *
     * @throws IllegalStateException If the addon is not loaded.
     */
    public static void loadedOrThrow() {
        WebSocketPackets microservices = WebSocketPackets.getInstance();
        if (microservices != null) return;

        throw new IllegalStateException("The %s addon must be loaded!".formatted(WebSocketPackets.class.getSimpleName()));
    }

}
