# Changelog

All notable changes to Cardboard Pet are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/).

## [0.4.3] - 2026-07-20

### Fixed
- Cardboard boxes now actually attack twice as often (every 10 ticks instead
  of 20) both on the ground and mid-flight. A first attempt overrode
  `MeleeAttackGoal#getAttackInterval()`, which turned out to have no effect:
  vanilla's `resetAttackCooldown()` hardcodes a 20-tick delay instead of
  consulting it, and the backing cooldown field is private. Fixed by
  overriding `checkAndPerformAttack(...)` directly with a self-managed
  cooldown instead.

## [0.4.2] - 2026-07-19

### Changed
- Relicensed from "All Rights Reserved" to MIT.

## [0.4.1] - 2026-07-19

### Changed
- Cardboard boxes chase harder now: the flight charge attack is faster
  (fast enough to catch up to a fleeing target) and no longer requires
  being grounded to trigger. `FOLLOW_RANGE` was raised from 32 to 500
  blocks, and losing line of sight no longer makes a box give up on a
  hostile-mob target — once it's after something, it keeps after it
  (bounded by what's actually loaded, in practice).

## [0.4.0] - 2026-07-19

### Added
- Cardboard boxes can now be equipped with armor. Shift + right-click with a
  helmet, chestplate, leggings, or boots routes it to the matching armor slot
  regardless of which hand it's held in (armor is not yet reflected visually
  on the model).
- Two parallel upgrade chains for the cardboard sword, each step crafted from
  the previous tier's sword plus material:
  - Stone chain: Stone → Iron → Gold → Diamond → Emerald → Netherite
  - Deepslate chain: Deepslate → Obsidian → Crying Obsidian → Sculk (echo
    shard) → End → Nether Star
  - Blade texture tint and attack damage/durability scale per tier.

### Fixed
- NeoForge (1.21.1): none of the mod's recipes were loading at all. Minecraft
  1.21.1 renamed the vanilla data folder from `recipes/` to the singular
  `recipe/`, and changed the recipe `result` object's key from `item` to
  `id`. The mod still used the old Forge 1.20.1 conventions on both counts,
  so recipes silently failed to register with no error on either the Forge
  or NeoForge side.

## [0.3.1] - 2026-07-18

### Changed
- Mod ID renamed from `takumimod` to `cardboard_pet` to match the repository
  name (the Java package/class names were left unchanged).

### Fixed
- A latent `NullPointerException` in `commonSetup()` that the mod ID rename
  exposed: on first-time config generation, `ModConfigEvent.Loading` could
  still be in flight when `FMLCommonSetupEvent` fired, leaving config fields
  null.

### Note
- **Not compatible with worlds from v0.3.0 or earlier.** Any cardboard box
  entities/items already placed in an existing world will fail to load under
  the new mod ID. A fresh world is recommended.

## [0.3.0] - 2026-07-18

### Added
- Cardboard Giant Wand: crafted from paper, a blaze rod, an amethyst shard,
  and a Cardboard Summoner. Summons a cardboard box at 2.5x normal size, with
  health and attack power scaled to match. Cooldown-based, not consumed on
  use.
- Cardboard Sword: crafted from 2 paper and a stick. Wooden-tier attack
  damage with a fast 3.6 attack speed. The blade is translucent with an
  emissive glow visible even in the dark.
- Cardboard boxes can now be equipped: shift + right-click as the owner to
  give or take back whatever's in your main hand or off-hand.

### Changed
- Attacks now land mid-flight during the box's charge attack, instead of
  waiting for it to land.
- Cardboard box hitbox adjusted to 1x1x1.
- Equipment the box was wearing is returned directly to its owner when it's
  defeated, instead of dropping on the ground.
- Owner-death Cardboard Summoner refund now reliably lands in the post-respawn
  inventory even when `keepInventory` is off.

## [0.2.0] - 2026-07-18

### Added
- Ported the mod to Minecraft 1.21.1 / NeoForge, kept behaviorally identical
  to the Forge 1.20.1 version, living alongside it in `neoforge-1.21.1/`.
- Cardboard Whistle: crafted from paper and string. Teleports every
  cardboard box you own to your side.
- Flight charge: cardboard boxes occasionally fly straight at distant
  targets instead of always approaching on foot.

### Changed
- Cardboard boxes no longer attack indiscriminately. They now only engage:
  - hostile mobs (zombies, skeletons, etc.), unconditionally
  - whoever their owner attacked, or was attacked by (defend/assist)
  - whoever hit the box itself (but never another cardboard box)

  Everyone else — other animals, unrelated players — is left alone.

## [0.1.0] - 2026-07-17

### Added
- Initial release. Adds the Cardboard Box entity: summon it with a Cardboard
  Summoner (crafted from paper, a Netherite Hoe, and string), right-click the
  ground to place it.
- Cardboard boxes hunt down anything that isn't their owner, never turn on
  their owner or each other, disappear when their owner dies, and don't
  despawn at range.
