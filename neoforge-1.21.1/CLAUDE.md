# CLAUDE.md (neoforge-1.21.1/)

Minecraft 1.21.1 / NeoForge 版の "Cardboard Pet"。[ルートのForge 1.20.1版](../CLAUDE.md)と同じ機能を持つ移植版で、mod id/パッケージ名は `takumimod` / `com.takumi.takumimod` のまま共通。**このディレクトリは完全に独立したGradleプロジェクト**（独自の `gradlew`/`settings.gradle`/`build.gradle`）。ルートから `cd` して初めてコマンドを実行すること。

## ビルド・実行

```
cd neoforge-1.21.1
./gradlew runClient    # クライアント起動
./gradlew build        # build/libs/ にjarを生成
```

## 構造

ルート版とほぼ同じパッケージ構成（`entity/`, `entity/goal/`, `item/`, `event/`, `registry/`, `client/`）。差分は以下の「Forge→NeoForge / 1.20.1→1.21.1の主な差分」を参照。

- `TakumiMod.java` — メインクラス。コンストラクタが `(IEventBus modEventBus, ModContainer modContainer)` を受け取る
- `TakumiModClient.java` — クライアント専用エントリポイント（Forge版の`TakumiMod.ClientModEvents`に相当。NeoForgeでは`@Mod(dist=Dist.CLIENT)`の別トップレベルクラスにするのが定石）

## Forge→NeoForge / 1.20.1→1.21.1の主な差分（このモッドで実際に踏んだもの）

- パッケージ: `net.minecraftforge.*` → `net.neoforged.neoforge.*` / `net.neoforged.fml.*` / `net.neoforged.bus.api.*`
- `RegistryObject<T>` → `DeferredHolder<R, T>`（Item専用は`DeferredItem<T>`）。`DeferredRegister.create(...)` の代わりに Item は `DeferredRegister.createItems(modid)` が返す `DeferredRegister.Items` を使うと便利
- Modコンストラクタ引数: Forgeの `FMLJavaModLoadingContext` → NeoForgeは `(IEventBus modEventBus, ModContainer modContainer)` をFMLが自動注入
- `ForgeConfigSpec` → `ModConfigSpec`（APIはほぼ同じ）
- `Mod.EventBusSubscriber` → トップレベルの `net.neoforged.fml.common.EventBusSubscriber`。**`bus`パラメータは非推奨（削除予定）** — 指定せずイベント型から自動判定させる
- `Mob#defineSynchedData()` → `defineSynchedData(SynchedEntityData.Builder builder)`（ビルダー引数を取るように変更、1.20.5〜）
- `Mob#finalizeSpawn(...)` の引数が5個→4個に（末尾の `CompoundTag` が廃止）
- `new ResourceLocation(namespace, path)` は削除予定 → `ResourceLocation.fromNamespaceAndPath(namespace, path)` を使う
- `BuiltInRegistries.ITEM.getValue(ResourceLocation)` は存在しない → `.get(ResourceLocation)` を使う
- `pack.mcmeta` の `pack_format` は 1.20.1=15 に対し 1.21.1=34（データパックは48だが、この共有 `pack.mcmeta` ではリソースパック値を採用）
- `neoforge.mods.toml`（旧`mods.toml`相当）は `META-INF/neoforge.mods.toml`。`build.gradle`の`ProcessResources`で`${...}`展開する対象に`pack.mcmeta`も追加している（NeoForge公式MDKのデフォルトは`neoforge.mods.toml`のみ展開）

## 実装上の注意

- ゲームロジック（AI・アイテム効果・レシピ）はルートのForge版と常に同期させる。移植時は[ルートCLAUDE.md](../CLAUDE.md)の「実装上の注意」も参照
- ビルド設定に迷ったら公式MDK（`NeoForgeMDKs/MDK-1.21.1-NeoGradle`）を参照。`neo_version`はそのMDK基準（`21.1.235`）
