---
title: Entity Overrides
lang: en-US
next:
    text: a
prev:
    text: o
---

# Entity Overrides

catharsis has a way to change entities!!!!

simply provide a file in `{your_name_space}:catharsis/entity_definitions/{entity_name}.json`

that loosk a bit liek this

```json
{
    "conditions": [
        {
            "type": "condition_type",
            ...
        }
    ],
    "replacement": "{your_name_space}:{entity_replacement_name}.json"
}
```

and another file in `{your_name_here}:catharsis/entities/{entity_replacement_name}.json`

that looks like this

```json
{
    "texture": "{your_name_space}:textures/{texture path here}.png",
    "model": "{your_name_space}:models/{model_path_here}.geo.json"
}
```

and put a `.png` fiel where the texture path leads to and a bedrock entity geometry (`.geo.json`) where that one points to. also if you name the bones right the model will use the main entity's animations!!!! the mod will log if a bone is named wrongly so dont worry about naming things right straight away.



## Condition Types

there are a lot of condition types

### `"type": "player"`
lets you access state about a player entity
keys:
`"skin"`: a reference to a skin url, if the entity matches this skin _exactly_ it is used
`"only_npc"`: the entity matches only if it isnt a real player (useful for npcs)

### `"type": "identity"`
lets you access state about regular entities
keys:
`"type"`: matches the entity's type (eg: `minecraft:zombie` for zombies)
`"uuid"`: matches the entity's uuid. skyblock mostly uses random uuids
`"name"`: matches the entity's name

### `"type": "attribute"`
allows access to entity attributes (such as max health)
keys:
`"attribute"`: which attribute to check
`"value"` or `"values"`: matches one or multiple values

### `"type": "island"`
matches if you are on an island
keys:
`"island"` or `"islands"`: matches one or multiple islands

### `"type": "equipment"`
matches the entity's equipment
keys:
`"slot"`: which slot to check
`"property"`: what BOOLEAN property to check (this can add more keys)

## Model quirks
Cannot handle per-face uv. will break. Everything else _should_ work fine
