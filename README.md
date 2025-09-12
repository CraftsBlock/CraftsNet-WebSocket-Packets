# CraftsNet-WebSocket-Packets
### Simple packet system for websockets

![Latest Release on Maven](https://repo.craftsblock.de/api/badge/latest/releases/de/craftsblock/craftsnet/modules/websocketpackets?color=40c14a&name=WebSocketPackets&prefix=v)
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
      <groupId>de.craftsblock.craftsnet.modules.websocketpackets</groupId>
      <artifactId>common</artifactId>
      <version>VERSION</version>
  </dependency>
    
  <!-- If you are using craftsnet you'll also need this dependency -->
  <dependency>
      <groupId>de.craftsblock.craftsnet.modules.websocketpackets</groupId>
      <artifactId>craftsnet</artifactId>
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
  implementation "de.craftsblock.craftsnet.modules.websocketpackets:common:VERSION"
  
  // If you are using craftsnet you'll also need this dependency
  implementation "de.craftsblock.craftsnet.modules.websocketpackets:craftsnet:VERSION"
}
```

## Quick Start

1. Apply your web socket endpoint with `@ApplyDecoder`.

```java
import de.craftsblock.cnet.modules.packets.addon.codec.CraftsNetPacketDecoder;
import de.craftsblock.cnet.modules.packets.addon.networking.middleware.SimpleNetworkerSetupMiddleware;
import de.craftsblock.cnet.modules.packets.common.networker.Networker;
import de.craftsblock.cnet.modules.packets.common.packet.Packet;
import de.craftsblock.craftsnet.api.middlewares.annotation.ApplyMiddleware;
import de.craftsblock.craftsnet.api.websocket.SocketExchange;
import de.craftsblock.craftsnet.api.websocket.SocketHandler;
import de.craftsblock.craftsnet.api.websocket.WebSocketClient;
import de.craftsblock.craftsnet.api.websocket.annotations.ApplyDecoder;
import de.craftsblock.craftsnet.api.websocket.annotations.Socket;

// For this quick start you'll need the websocket packets craftsnet dependency too. 
public class MyWebsocket implements SocketHandler {

    @Socket("/my/packets")
    @ApplyDecoder(CraftsNetPacketDecoder.class)
    @ApplyMiddleware(SimpleNetworkerSetupMiddleware.class)
    public void handle(SocketExchange exchange, Packet packet) {
        WebSocketClient client = exchange.client();
        Networker networker = SimpleNetworkerSetupMiddleware.getNetworker(client);
        packet.handle(networker);
    }

}
```

2. Start creating your own custom packets.

## Open Source Licenses
We are using some third party open source libraries. Below you find a list of all third party open source libraries used:

| Name                                                   | Description                                                      | Licecnse                                                                           |
|--------------------------------------------------------|------------------------------------------------------------------|------------------------------------------------------------------------------------|
| [CraftsCore](https://github.com/CrAfTsArMy/CraftsCore) | https://repo.craftsblock.de/#/releases/de/craftsblock/craftscore | [Apache License 2.0](https://github.com/CrAfTsArMy/CraftsCore/blob/master/LICENSE) |
| [CraftsNet](https://github.com/CraftsBlock/CraftsNet)  | https://repo.craftsblock.de/#/releases/de/craftsblock/craftsnet  | [Apache License 2.0](https://github.com/CrAfTsArMy/CraftsCore/blob/master/LICENSE) |

## Support and contribution
If you have any questions or have found a bug, please feel free to let us know in our [issue tracker](https://github.com/CraftsBlock/CraftsNet-WebSocketPackets/issues). We appreciate any help and welcome your contributions to improve the project.

