---
title: Commands
lang: en-US
---

# Commands

### `/catharsis dev hand_id`

Prints the id used to modify the item into chat. Also adds a copy button.

### `/catharsis dev areas`

#### `/catharsis dev areas toggle <id>`

Toggles rendering of a debug view of the bounding box/octree associated with the area.

#### `/catharsis dev areas disable_all`

Disables all active debug renderers.

### `/catharsis dev area_selection`

#### `/catharsis dev area_selection <add|remove> <id>`

Adds/Removes the specified block to/from the allow list.

#### `/catharsis dev area_selection list`

Prints out the current allow list.

#### `/catharsis dev area_selection run {100}`

Dispatches the area selection with the current allow list, the range is the max distance from the origin.

#### `/catharsis dev area_selection clear`

Clears all entries from the allow list.

### `/catharsis dev give <type>`

#### `/catharsis dev give mannequin|armorstand <query>`

Gives a mannequin or armorstand with the armor set of the specified SkyBlock item.

#### `/catharsis dev give item id|name <query>`

Gives an item by its SkyBlock ID or name.

### `/catharsis dev find ids|names [flags] <query>`

A utility command to find items, by default uses a contains search that ignores the cases and limits it to 100 items.

Usage: `/catharsis dev find <ids|names> [-r|-c|-s|-e] [-l <amount>|-a] [-m] [-g] <query>`

#### Flags

Filtering flags (only one allowed)

> - `-c`/`--contains` uses contains (default).
> - `-r`/`--regex` uses regex search.
> - `-s`/`--starts_with` checks if the start is equal.
> - `-e`/`--ends_with` checks if the ending is equal.

Limiting flags (only one allowed)

> - `-l`/`--limit` `<amount>` Limits the items to find (default is 100).
> - `-a`/`--all` Takes all items that match the search.

Misc flags

> - `-m`/`--match_case` Enables case-sensitive matching.
> - `-g`/`--give` Automatically gives you all items that match the search.

### `/catharsis dev regex`

Opens the regex tester GUI.

TODO SOPHIE WRITE DOCS FOR THIS
