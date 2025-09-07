# CraftsNet-WebSocket-Packets
### Simple packet system for websockets

![Latest Release on Maven](https://repo.craftsblock.de/api/badge/latest/releases/de/craftsblock/craftsnet?color=40c14a&name=CraftsNet&prefix=v)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/CraftsBlock/CraftsNet-WebSocketPackets)
![GitHub](https://img.shields.io/github/license/CraftsBlock/CraftsNet-WebSocketPackets)
![GitHub all releases](https://img.shields.io/github/downloads/CraftsBlock/CraftsNet-WebSocketPackets/total)
![GitHub issues](https://img.shields.io/github/issues-raw/CraftsBlock/CraftsNet-WebSocketPackets)

---

## Installation

### Maven
```xml
<repositories>
  ...
  <repository>
    <id>craftsblock-releases</id>
    <name>CraftsBlock Repositories</name>
    <url>https://repo.craftsblock.de/releases</url>
  </repository>
</repositories>
```
```xml
<dependencies>
  ...
  <dependency>
      <groupId>de.craftsblock.craftsnet.modules</groupId>
      <artifactId>websocketpackets</artifactId>
      <version>VERSION</version>
  </dependency>
</dependencies>
```

### Gradle
```gradle
repositories {
  ...
  maven { url "https://repo.craftsblock.de/releases" }
  mavenCentral()
}
```
```gradle
dependencies {
  ...
  implementation "de.craftsblock.craftsnet.modules:websocketpackets:VERSION"
}
```

## Quick Start

1. Create a middleware to create for connecting web socket clients a networker.
```java
import de.craftsblock.cnet.modules.packets.WebSocketPackets;
import de.craftsblock.cnet.modules.packets.networker.WebSocketClientNetworker;
import de.craftsblock.craftsnet.api.middlewares.MiddlewareCallbackInfo;
import de.craftsblock.craftsnet.api.middlewares.WebsocketMiddleware;
import de.craftsblock.craftsnet.api.websocket.SocketExchange;
import de.craftsblock.craftsnet.api.websocket.WebSocketClient;

public class NetworkerSetupMiddleware implements WebsocketMiddleware {
    
    @Override
    public void handleConnect(MiddlewareCallbackInfo callbackInfo, SocketExchange exchange) {
        WebSocketPackets packets = exchange.server().getCraftsNet().getAddonManager().getAddon(WebSocketPackets.class);
        WebSocketClient client = exchange.client();

        client.getSession().put("networker", new WebSocketClientNetworker(packets, client));
    }

    @Override
    public void handleDisconnect(MiddlewareCallbackInfo callbackInfo, SocketExchange exchange) {
        exchange.client().getSession().remove("networker");
    }
    
}
```

2. Apply your web socket endpoint with `@ApplyDecoder`.

```java
import de.craftsblock.cnet.modules.packets.packet.Packet;
import de.craftsblock.cnet.modules.packets.packet.codec.PacketDecoder;
import de.craftsblock.craftsnet.api.middlewares.annotation.ApplyMiddleware;
import de.craftsblock.craftsnet.api.websocket.SocketExchange;
import de.craftsblock.craftsnet.api.websocket.SocketHandler;
import de.craftsblock.craftsnet.api.websocket.annotations.ApplyDecoder;
import de.craftsblock.craftsnet.api.websocket.annotations.Socket;
import de.craftsblock.craftsnet.autoregister.meta.AutoRegister;

@AutoRegister
public class MyWebsocket implements SocketHandler {

    @Socket("")
    @ApplyDecoder(PacketDecoder.class)
    @ApplyMiddleware(NetworkerSetupMiddleware.class)
    public void handle(SocketExchange exchange, Packet packet) {
        Networker networker = exchange.client().getSession().getAsType("networker", Networker.class);
        message.handle(networker);
    }

}
```

3. Start creating custom packets.

## Open Source Licenses
We are using some third party open source libraries. Below you find a list of all third party open source libraries used:

| Name                                                   | Description                                                      | Licecnse                                                                           |
|--------------------------------------------------------|------------------------------------------------------------------|------------------------------------------------------------------------------------|
| [CraftsCore](https://github.com/CrAfTsArMy/CraftsCore) | https://repo.craftsblock.de/#/releases/de/craftsblock/craftscore | [Apache License 2.0](https://github.com/CrAfTsArMy/CraftsCore/blob/master/LICENSE) |
| [CraftsNet](https://github.com/CraftsBlock/CraftsNet)  | https://repo.craftsblock.de/#/releases/de/craftsblock/craftsnet  | [Apache License 2.0](https://github.com/CrAfTsArMy/CraftsCore/blob/master/LICENSE) |

## Support and contribution
If you have any questions or have found a bug, please feel free to let us know in our [issue tracker](https://github.com/CraftsBlock/CraftsNet-WebSocketPackets/issues). We appreciate any help and welcome your contributions to improve the project.

