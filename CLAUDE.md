# CLAUDE.md

Minecraft MOD "Cardboard Pet"（GitHub リポジトリ名: `cardboard-pet`）。内部 mod id / パッケージ名は `takumimod`（`com.takumi.takumimod`）で両バージョン共通。日本語コンテキストのプロジェクトで、リソース（アイテム名など）は日本語・英語両対応。

## バージョン構成

このリポジトリは2つの独立したGradleプロジェクトを含む:

- **ルート** — Minecraft 1.20.1 / Forge（ForgeGradle）。以下の説明は主にこちら
- **`neoforge-1.21.1/`** — Minecraft 1.21.1 / NeoForge（NeoGradle）への移植版。独自の `gradlew`/`settings.gradle`/`build.gradle` を持つ完全に独立したビルド。詳細は[neoforge-1.21.1/CLAUDE.md](neoforge-1.21.1/CLAUDE.md)

**両バージョンでゲームロジック（段ボールのAI・アイテムの効果など）は同一に保つこと。** 片方に機能追加・バグ修正をしたら、もう片方にも同じ変更を反映する（API差分は各バージョンのCLAUDE.mdを参照）。

**Forge版はMinecraft 1.20.1のみをターゲットとする。** 1.21系はNeoForge版でカバーしているため、Forgeを1.21.1に追従させる作業は不要（スコープ外）。

### 今後の計画: 統合版（Bedrock Edition）対応

Java Edition（Forge / NeoForge）に加えて、**Minecraft 統合版（Bedrock Edition）** 向けのリリースも計画中（未着手）。統合版はJavaのMOD機構が使えず、技術スタックが根本的に異なる点に注意:

- Behavior Pack（JSON定義: エンティティ、アイテム、レシピ）+ Resource Pack（`.geo.json`形式のモデル、独自形式のテクスチャ/アニメーション）の「アドオン」構成になる
- カスタムAIゴール（`DefendOwnerTargetGoal`等）のようなJavaコードは書けないため、Bedrockのコンポーネントシステムや Script API（`@minecraft/server`）で同等ロジックを再実装する必要がある
- 実装する際は `neoforge-1.21.1/` と同様、リポジトリ直下に新しいトップレベルディレクトリ（例: `bedrock/`）を切って独立させる想定
- ゲームロジック（段ボールの攻撃対象ルール、装備、召喚器/呼び笛の挙動）はJava版と可能な限り同じ仕様に合わせること

## ビルド・実行（ルート = Forge 1.20.1）

```
./gradlew runClient    # クライアント起動（初回はMCソースのDL/デコンパイルで時間がかかる）
./gradlew build        # build/libs/ にjarを生成
```

## 構造

- `src/main/java/com/takumi/takumimod/`
  - `TakumiMod.java` — メインクラス。DeferredRegisterの登録、クリエイティブタブへのアイテム追加
  - `Config.java` — Forge ConfigSpec（MDKのサンプルのまま、未使用寄り）
  - `entity/CardboardBoxEntity.java` — 「段ボール」Mob本体。オーナーUUIDを同期データ+NBTで永続化。`getOwnerEntity()`でオーナーのPlayerを解決
  - `entity/goal/FlyAtTargetGoal.java` — 段ボールが対象へ空中突撃するAIゴール（`LeapAtTargetGoal`の代替）。射程内なら飛行中でも`doHurtTarget`で攻撃を命中させる（着地を待たない）
  - `entity/goal/DefendOwnerTargetGoal.java` — オーナーが攻撃された相手をターゲットにする（`getLastHurtByMob`ベース）
  - `entity/goal/AssistOwnerTargetGoal.java` — オーナーが攻撃した相手をターゲットにする（`getLastHurtMob`ベース）
  - `item/CardboardSummonerItem.java` — 段ボール召喚器（右クリックで召喚、オーナー登録）
  - `item/CardboardWhistleItem.java` — 段ボール呼び笛（使用で自分の段ボールを全召集しテレポート）
  - `item/CardboardSwordItem.java` — `SwordItem`のサブクラス（`Tiers.WOOD`ベース、攻撃力は木の剣相当だが攻撃速度は3.6=デフォルト基礎値4.0にmodifier `-0.4`を加算、木の剣の1.6より高速）。刃の縁だけ発光するエミッシブ描画のために`initializeClient`をオーバーライドしてカスタムレンダラーを登録（後述）。カスタムロジックが無いアイテムは無理にクラスを作らずvanilla実装を直接使う方針だが、これはクライアント描画フックのためだけにクラスが必要な例外
  - `event/ModEvents.java` — オーナー死亡時に紐づく段ボールをdiscardし、代わりに段ボール召喚器をオーナーの次回リスポーン時に付与する処理
  - `registry/ModItems.java`, `registry/ModEntities.java` — DeferredRegister定義。段ボールの当たり判定は`sized(1.0F, 1.0F)`（1x1x1）
  - `client/` — レンダラー・モデル（クライアント専用）
- `src/main/resources/`
  - `data/takumimod/recipes/` — クラフトレシピ
  - `assets/takumimod/lang/{en_us,ja_jp}.json` — ローカライズ。アイテム/エンティティ追加時は両方更新する
  - `assets/takumimod/models/`, `textures/` — アイテム/エンティティのモデル・テクスチャ

## 実装上の注意

- 全段ボールを走査する処理（呼び笛、オーナー死亡時の消滅）は `ServerLevel#getEntities().getAll()` をフィルタする方式。以前はAABB無限範囲クエリを使っていたが、素直な全走査に置き換え済み
- 段ボールのターゲット選択（`registerGoals`内 `targetSelector`）は「無差別に全生物を攻撃」ではなく以下の優先順位:
  1. `HurtByTargetGoal` — 自分を殴った相手（他の段ボールは除外）
  1. `DefendOwnerTargetGoal` — オーナーを攻撃した相手
  2. `AssistOwnerTargetGoal` — オーナーが攻撃した相手
  3. `NearestAttackableTargetGoal<Mob>`（`instanceof Enemy`） — 敵対Mobは無条件
  - オーナー・同族(`CardboardBoxEntity`)・`ArmorStand`は`canAttack`でオーバーライドして常に除外
- 新しいアイテム/エンティティを追加したら、`en_us.json` と `ja_jp.json` の両方にローカライズキーを追加すること
- `README.md` は日本語で、ユーザー向けの機能一覧・レシピを記載。実装を変更したら追従させる
- **装備システム**: `CardboardBoxEntity#mobInteract` でオーナーのシフト右クリックのみ受け付ける。手が空でなければそのアイテム1個を対応する`EquipmentSlot`（メインハンド/オフハンド、`hand`引数から決定）に装着し既存装備は player のインベントリへ（入らなければドロップ）。手が空なら現在の装備を外して手に渡す
  - 見た目の反映には `CardboardBoxModel implements ArmedModel`（`translateToHand`で右腕/左腕にアイテム位置を合わせる）+ `CardboardBoxRenderer` に `ItemInHandLayer` を追加。防具スロット（頭/胴/脚/足）は機能的には設定できるが、モデルに頭部パーツが無いため防具レイヤーの見た目には対応していない
- **飛行中の攻撃**: `MeleeAttackGoal`と`FlyAtTargetGoal`はどちらも`Flag.MOVE`を宣言しているため同時実行できない（GoalSelectorの仕様上、同じフラグを持つゴールは優先度が高い方だけが動く）。そのため飛行中に殴らせたい場合はMeleeAttackGoal任せにせず、`FlyAtTargetGoal`自身の`tick()`内で射程判定・クールダウン管理・`doHurtTarget`呼び出しまで完結させている（`ATTACK_INTERVAL_TICKS`で連打を防止）
- **`FlyAtTargetGoal.canUse()`の接地判定は`onGround()`のみで十分**: 一度、「垂直速度がほぼ0であること」も条件に追加してみたが、接地中のMobでも重力の残差で`getDeltaMovement().y`は`0`ちょうどにはならず`-0.08`前後になることが多く、厳しめの閾値（`< 0.01`）では接地判定がほぼ常にfalseになり突撃自体が発生しなくなる回帰を起こした。`onGround()`単体で判定すること
- **死亡時のアイテム返却**: `CardboardBoxEntity#dropEquipment`をオーバーライドし、地面ドロップ（vanillaのデフォルト）ではなくオーナーへ直接返す。オーナーが読み込まれていなければ段ボールの位置に`spawnAtLocation`、オーナーのインベントリが満杯なら`owner.drop`で足元に。同様に`ModEvents#onLivingDeath`でも、オーナー死亡で段ボールをdiscardする際に段ボール召喚器の返却を予約する（呼び笛や巨大化の杖のように消費されないアイテムは対象外）
- **オーナー死亡時の召喚器返却がkeepInventoryに影響されない理由**: `LivingDeathEvent`の時点で直接インベントリに入れると、`Player#dropEquipment`（`keepInventory`が無効な場合にインベントリを全ドロップする処理）が`LivingDeathEvent`より後に走るため、せっかく入れたアイテムも巻き込まれて地面にばら撒かれてしまう。そこで死亡時は`Player#getPersistentData()`内の`Player.PERSISTED_NBT_TAG`（`"PlayerPersisted"`）配下に返却予定数をNBTで記録するだけにとどめ（このタグはリスポーン時に`ServerPlayer#restoreFrom`で新しいPlayerインスタンスへコピーされるため死を跨いで生存する）、`PlayerEvent.PlayerRespawnEvent`（インベントリが確定した後に発火）で実際にアイテムを付与している。`getPersistentData()`のルート直下に書き込むだけでは復元時にコピーされないので注意
- **巨大化（Forge版）のサイズ変更は`onSyncedDataUpdated`のフックが必須**: `SIZE_SCALE`をサーバー側で`entityData.set()`しただけでは、クライアント側のエンティティは新しい値を受信するだけで当たり判定（`dimensions`）を再計算しない。見た目（レンダラーの`scale()`）はエンティティのsynced dataを直接読むため正しく巨大化して見えるが、実際のクリック判定・攻撃判定に使われるクライアント側バウンディングボックスは古いまま、という表面化しにくい不整合が起きる（攻撃が当たらない、モデルの端に当たり判定が無い、等）。`CardboardBoxEntity#onSyncedDataUpdated`をオーバーライドし、`SIZE_SCALE`が更新されたら`refreshDimensions()`を呼んで解消している。NeoForge版はバニラの`Attributes.SCALE`を使っており、この同期後の再計算がエンジン側の標準機能として組み込まれているためこの問題は起きない
- **段ボールの剣のエミッシブ（部分発光）描画**: 平面アイテムの一部だけを暗闇でも光らせる仕組みはvanillaの`minecraft:item/generated`モデルだけでは実現できない（レイヤーを増やしても通常の陰影が乗る）。`CardboardSwordItemRenderer`（`BlockEntityWithoutLevelRenderer`のサブクラス）を実装し、`IClientItemExtensions#getCustomRenderer()`経由でアイテムに紐付けている
  - **アイテムモデルの`parent`は`minecraft:builtin/entity`が必須**: 盾・バナー・チェストなど`BlockEntityWithoutLevelRenderer`を使うvanillaアイテムと同じ。`minecraft:item/handheld`（通常の平面アイテム）のままだと、ベイクされたモデルの`isCustomRenderer()`が常に`false`になり、`ItemRenderer#render()`がカスタムレンダラーの分岐に一切入らず`renderByItem`が呼ばれない（＝レンダラーを実装しても何も起きない）。実際にこれで最初「発光しない」（＝カスタム描画自体が丸ごと無視されていた）というバグを踏んだ。テクスチャは`layer0`ではなく`particle`キーで指定する（破壊パーティクル用、実際の描画には使わない）
  - **`builtin/entity`にすると`item/handheld`が持っていた手持ち時の回転/位置/スケール（`display`ブロック）を失う**ため、`cardboard_sword.json`自身に`thirdperson_righthand`/`firstperson_righthand`等を明示的に書く必要がある（vanillaの`item/shield.json`が同じ構成で参考になる）。値は`item/handheld.json`（三人称/一人称の手持ち回転）+`item/generated.json`（`ground`/`head`/`fixed`）から転記した。これを忘れると持ち位置・向きがおかしくなる（実際に踏んだ2つ目のバグ）
  - ベーステクスチャ（`cardboard_sword.png`）は`RenderType.entityCutout`で通常通り環境光の影響を受けて描画。発光マスク（`cardboard_sword_glow.png`、刃の縁だけ不透明で他は透明）は`RenderType.entityTranslucentEmissive`（マグマキューブの亀裂やウォーデンの発光部と同じ仕組み）で描画し、`light`引数を無視して`LightTexture.FULL_BRIGHT`を渡すことで常時フルブライトにしている
  - `ItemRenderer#render()`はカスタムレンダラーの有無に関わらず`handleCameraTransforms`（GUI/手持ち/地面などの表示コンテキストに応じた位置・回転・スケール）と`-0.5,-0.5,-0.5`の原点補正を先に適用してから`renderByItem`を呼ぶため、BEWLR側でvanillaのアイテム表示変換を再実装する必要はない
  - **クアッドのZ座標は0ではなく0.5**: vanillaの`ItemModelGenerator`（平面アイテムの自動モデル生成）は前後面を16分率で`z=[7.5, 8.5]`（＝0-1空間で0.5付近の薄い板）に配置している。Z=0で描いてしまうと`-0.5,-0.5,-0.5`原点補正後にZが`-0.5`ずれ、持ち位置が不自然にズレる（3つ目のバグ）。`BASE_Z = 0.5F`、発光レイヤーはZファイティング回避のためわずかにずらして`GLOW_Z = 0.499F`としている。X/Yは`(0,0)-(1,1)`のクアッド
  - **発光レイヤーは片面だけ描画する**: ベース（`entityCutout`）は`RenderType`側でカリング有効なので表裏両面を描く必要があるが、発光（`entityTranslucentEmissive`）は`CompositeState`で`NO_CULL`が設定済みで元々両面表示。両面分描くと同一平面に半透明面が2枚重なり、ブレンドが二重に乗って発光の見た目がおかしくなる（4つ目のバグ）。ベースは`bothSides=true`、発光は`bothSides=false`で呼び分ける
  - **`cardboard_sword_glow.png`のデザインは試行錯誤中**: 元の刃自体が16x16の中で2px幅の細い斜め線でしかない。「範囲を広く」という要望に対し、刃の輪郭の外側ににじみ出るハロー（1px圏・2px圏のdilate）を試したが、最終的には「後光ではなく刃全体を光らせる」方針に戻した（＝`blade_cells`+`edge_cells`の全ピクセルを塗る、輪郭の外にはにじませない）。ハロー実装のコード自体は残していないので、再度「範囲を広く」と言われたら本セクション上の`quadFace`/`renderFlatQuad`は変更不要、`cardboard_sword_glow.png`側のピクセル塗り分けだけで調整する
  - **発光レイヤーのalpha値を下げても実際には半透明に見えなかった**: `rendertype_entity_translucent_emissive`のシェーダー/ブレンド関数（`SRC_ALPHA, ONE_MINUS_SRC_ALPHA`）自体は標準的でテクスチャの`color.a`をそのまま使っているように見えるが、alpha≈160にしても体感で透けて見えなかった（原因未特定、レンダーパスのflush順序かGLブレンド周りの何か）
  - **最終的な設計: 半透明にするのは発光レイヤーではなくベース（刃）側**。ユーザーの要望は「発光を半透明に」ではなく「刃自体を半透明にして奥の発光が透けて見えるように」だった。`cardboard_sword.png`のブレード部分（`blade_cells`/`edge_cells`、柄・鍔は不透明のまま）のalphaを下げ、`RenderType.entityCutout`から`RenderType.entityTranslucent`に変更。描画順を「発光（`entityTranslucentEmissive`、不透明・フルブライト）を先に奥へ→半透明のベースを後に手前へ」に入れ替え、`GLOW_Z = 0.501F`（奥）/`BASE_Z = 0.499F`（手前）とした。`entityTranslucent`も`entityTranslucentEmissive`と同じく`NO_CULL`（両面表示）なので、どちらも片面描画のみでよい
  - UVは`v=0`がPNGの最上段（テクスチャ座標系の慣習）なので、クアッド座標の`y=1`（上側）に`v=0`を対応させる。逆にすると絵が上下反転する
  - `VertexConsumer`の頂点追加APIはバージョン差分あり: Forge 1.20.1は`.vertex(matrix,x,y,z).color().uv().overlayCoords().uv2(packedLight).normal().endVertex()`という旧チェーンAPI、NeoForge 1.21.1は`.addVertex(matrix,x,y,z).setColor().setUv().setOverlay(packedOverlay).setUv2(light,light2).setNormal()`という新API（`setUv2`は2引数のみでパック済みint版が無い点に注意、`light & 65535, light >> 16 & 65535`で分解する）
  - 登録方法もバージョンで異なる: Forge 1.20.1は`Item#initializeClient(Consumer<IClientItemExtensions>)`をアイテムクラスでオーバーライド。NeoForge 1.21.1では同メソッドは非推奨（削除予定）のため、`TakumiModClient`で`RegisterClientExtensionsEvent`（モードバスイベント）を購読し`event.registerItem(extensions, item)`で登録する方式に統一した
