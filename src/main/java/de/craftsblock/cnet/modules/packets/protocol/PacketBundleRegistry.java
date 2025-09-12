package de.craftsblock.cnet.modules.packets.protocol;

import de.craftsblock.cnet.modules.packets.packet.Packet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A registry for managing {@link PacketBundle} instances.
 * <p>
 * This class provides methods to register, unregister, retrieve, and query
 * packet bundles by their identifier or associated {@link Packet} type.
 * Identifiers are required to be unique and must match the regex {@code [a-z0-9-_]}.
 * <p>
 * Once registered, a {@link PacketBundle} is accessible via its identifier
 * or through its contained packet classes. The registry itself is thread-safe
 * through synchronization on all public operations.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @see PacketBundle
 * @since 1.0.0
 */
public final class PacketBundleRegistry {

    private final HashMap<String, PacketBundle> bundles = new HashMap<>();

    /**
     * Registers a new {@link PacketBundle} in this registry.
     * <p>
     * The identifier of the bundle must be unique and match the regex {@code [a-z0-9-_]}.
     *
     * @param bundle The packet bundle to register.
     * @throws IllegalStateException    If the identifier is already in use.
     * @throws IllegalArgumentException If the identifier does not match the required pattern.
     */
    public synchronized void register(@NotNull PacketBundle bundle) {
        String identifier = bundle.identifier();
        if (this.isRegistered(identifier))
            throw new IllegalStateException("The identifier %s is already in use!".formatted(identifier));

        if (!identifier.matches("[a-z0-9-_]+"))
            throw new IllegalArgumentException("An packet bundle identifier must match [a-z0-9-_]!");

        bundles.put(identifier, bundle);
    }

    /**
     * Creates a new {@link PacketBundleBuilder} that automatically registers
     * the built bundle into this registry once {@link PacketBundleBuilder#build()} is called.
     *
     * @param identifier The identifier of the new bundle.
     * @param version    The version of the new bundle.
     * @return A {@link PacketBundleBuilder} that registers its result into this registry.
     */
    public synchronized PacketBundleBuilder create(@NotNull String identifier,
                                                   @Range(from = 0, to = Integer.MAX_VALUE) int version) {
        return new PacketBundleBuilder(identifier, version) {

            @Override
            public synchronized @NotNull PacketBundle build() {
                PacketBundle bundle = super.build();
                register(bundle);
                return bundle;
            }

        };
    }

    /**
     * Unregisters a {@link PacketBundle} from this registry by instance reference.
     *
     * @param bundle The bundle to unregister.
     * @return The unregistered bundle, or {@code null} if not found.
     */
    public synchronized PacketBundle unregister(@NotNull PacketBundle bundle) {
        return this.unregister(bundle.identifier());
    }

    /**
     * Unregisters a {@link PacketBundle} from this registry by its identifier.
     *
     * @param identifier The identifier of the bundle to unregister.
     * @return The unregistered bundle, or {@code null} if not found.
     */
    public synchronized PacketBundle unregister(@NotNull String identifier) {
        return bundles.remove(identifier);
    }

    /**
     * Checks whether a given bundle is registered in this registry.
     *
     * @param bundle The bundle to check, or {@code null}.
     * @return {@code true} if the bundle is registered, {@code false} otherwise.
     */
    @Contract("null -> false")
    public synchronized boolean isRegistered(PacketBundle bundle) {
        if (bundle == null) return false;
        return this.isRegistered(bundle.identifier());
    }

    /**
     * Checks whether a bundle with the given identifier is registered.
     *
     * @param identifier The identifier to check, or {@code null}.
     * @return {@code true} if a bundle is registered under the identifier, {@code false} otherwise.
     */
    @Contract("null -> false")
    public synchronized boolean isRegistered(String identifier) {
        if (identifier == null) return false;
        return bundles.containsKey(identifier);
    }

    /**
     * Retrieves a {@link PacketBundle} by its identifier.
     *
     * @param identifier The identifier of the bundle, or {@code null}.
     * @return The matching bundle, or {@code null} if not found.
     */
    @Contract("null -> null")
    public synchronized PacketBundle getBundle(String identifier) {
        if (identifier == null) return null;
        return this.bundles.get(identifier.toLowerCase().trim());
    }

    /**
     * Retrieves the {@link PacketBundle} associated with a given packet instance.
     *
     * @param packet The packet instance, or {@code null}.
     * @return The bundle containing the packet type, or {@code null} if not found.
     */
    @Contract("null -> null")
    public synchronized PacketBundle getBundle(Packet packet) {
        if (packet == null) return null;
        return this.getBundle(packet.getClass());
    }

    /**
     * Retrieves the {@link PacketBundle} associated with a given packet class.
     *
     * @param packet The packet class, or {@code null}.
     * @return The bundle containing the packet class, or {@code null} if not found.
     */
    @Contract("null -> null")
    public synchronized PacketBundle getBundle(Class<? extends Packet> packet) {
        if (packet == null) return null;
        return bundles.values().stream()
                .filter(bundle -> bundle.getId(packet) >= 0)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns an unmodifiable view of all registered packet bundles in this registry.
     *
     * @return An unmodifiable map of identifiers to their {@link PacketBundle} instances.
     */
    public @NotNull @Unmodifiable Map<String, PacketBundle> getBundles() {
        return Collections.unmodifiableMap(this.bundles);
    }

}
