package de.craftsblock.cnet.modules.packets.networker.middleware;

import de.craftsblock.cnet.modules.packets.WebSocketPackets;
import de.craftsblock.cnet.modules.packets.networker.WebSocketClientNetworker;
import de.craftsblock.craftsnet.api.middlewares.MiddlewareCallbackInfo;
import de.craftsblock.craftsnet.api.middlewares.WebsocketMiddleware;
import de.craftsblock.craftsnet.api.websocket.Frame;
import de.craftsblock.craftsnet.api.websocket.SocketExchange;
import de.craftsblock.craftsnet.api.websocket.WebSocketClient;

/**
 * A middleware that automatically sets up and tears down
 * {@link WebSocketClientNetworker} instances for {@link WebSocketClient}s.
 * <p>
 * On connection, a {@link WebSocketClientNetworker} is created and stored in
 * the client's session under the key {@link #SESSION_NETWORKER_KEY}.
 * On disconnection, the entry is removed.
 * <p>
 * This allows simple retrieval of the associated networker instance
 * from any {@link WebSocketClient}.
 *
 * @author Philipp Maywald
 * @author CraftsBlock
 * @version 1.0.0
 * @since 1.0.0
 * @see WebsocketMiddleware
 * @see WebSocketClientNetworker
 */
public class SimpleNetworkerSetupMiddleware implements WebsocketMiddleware {

    /**
     * The session attribute key used to store the {@link WebSocketClientNetworker}.
     */
    public static final String SESSION_NETWORKER_KEY = "networker";

    /**
     * Called when a new WebSocket connection is established.
     * <p>
     * Creates a {@link WebSocketClientNetworker} and stores it in the
     * session of the connecting client.
     *
     * @param callbackInfo The middleware callback context.
     * @param exchange     The WebSocket exchange containing connection details.
     */
    @Override
    public void handleConnect(MiddlewareCallbackInfo callbackInfo, SocketExchange exchange) {
        WebSocketPackets packets = exchange.server().getCraftsNet().getAddonManager().getAddon(WebSocketPackets.class);
        WebSocketClient client = exchange.client();

        client.getSession().put("networker", new WebSocketClientNetworker(packets, client));
    }

    /**
     * Called when a WebSocket connection is closed.
     * <p>
     * Removes the {@link WebSocketClientNetworker} from the client session.
     *
     * @param callbackInfo The middleware callback context.
     * @param exchange     The WebSocket exchange containing connection details.
     */
    @Override
    public void handleDisconnect(MiddlewareCallbackInfo callbackInfo, SocketExchange exchange) {
        exchange.client().getSession().remove("networker");
    }

    /**
     * Retrieves the {@link WebSocketClientNetworker} associated with a given
     * {@link WebSocketClient}, or {@code null} if none exists.
     *
     * @param client The WebSocket client.
     * @return The associated {@link WebSocketClientNetworker}, or {@code null} if not set.
     */
    public static WebSocketClientNetworker getNetworker(WebSocketClient client) {
        return client.getSession().getAsType(SESSION_NETWORKER_KEY, WebSocketClientNetworker.class);
    }

}
