# Advanced Interaction Events

A Minecraft library for registering and handling custom interaction events, including click actions and other player interactions. Useful for when working with Minecraft's Dialog framework and custom event handling.

## Features

1. Flexible event handler types using the `EventHandlerType` registry, *preventing event handler conflicts*.
2. Codec-based `EventData` serialisation for user specified event data, *instead of manual NBT parsing*.
3. Server-side event handler dispatch for custom click-action packets, *avoiding the need for hardcoded mixins*.

## Compatibility

This library is compatible with Minecraft 26.2, Fabric Loader 0.19.5 or newer, and Java 25 or newer.

## Usage

For demonstration on how to use this library, check my [Enhanced Lobby](https://github.com/samuelh2005/EnhancedLobby) mod.

## License

This project is license under the GNU Lesser General Public License v3.0 or later. See the [LICENSE](LICENSE) file for more details.
