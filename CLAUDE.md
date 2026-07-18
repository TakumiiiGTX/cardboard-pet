# CLAUDE.md

Minecraft MOD "Cardboard Pet"（GitHub リポジトリ名: `cardboard-pet`）。内部 mod id / パッケージ名は `takumimod`（`com.takumi.takumimod`）で両バージョン共通。日本語コンテキストのプロジェクトで、リソース（アイテム名など）は日本語・英語両対応。

## バージョン構成

このリポジトリは2つの独立したGradleプロジェクトを含む:

- **ルート** — Minecraft 1.20.1 / Forge（ForgeGradle）。以下の説明は主にこちら
- **`neoforge-1.21.1/`** — Minecraft 1.21.1 / NeoForge（NeoGradle）への移植版。独自の `gradlew`/`settings.gradle`/`build.gradle` を持つ完全に独立したビルド。詳細は[neoforge-1.21.1/CLAUDE.md](neoforge-1.21.1/CLAUDE.md)

**両バージョンでゲームロジック（段ボールのAI・アイテムの効果など）は同一に保つこと。** 片方に機能追加・バグ修正をしたら、もう片方にも同じ変更を反映する（API差分は各バージョンのCLAUDE.mdを参照）。

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
  - `entity/goal/FlyAtTargetGoal.java` — 段ボールが対象へ空中突撃するAIゴール（`LeapAtTargetGoal`の代替）
  - `entity/goal/DefendOwnerTargetGoal.java` — オーナーが攻撃された相手をターゲットにする（`getLastHurtByMob`ベース）
  - `entity/goal/AssistOwnerTargetGoal.java` — オーナーが攻撃した相手をターゲットにする（`getLastHurtMob`ベース）
  - `item/CardboardSummonerItem.java` — 段ボール召喚器（右クリックで召喚、オーナー登録）
  - `item/CardboardWhistleItem.java` — 段ボール呼び笛（使用で自分の段ボールを全召集しテレポート）
  - `event/ModEvents.java` — オーナー死亡時に紐づく段ボールをdiscardする処理
  - `registry/ModItems.java`, `registry/ModEntities.java` — DeferredRegister定義
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
