# CLAUDE.md

Minecraft Forge 1.20.1 MOD "Cardboard Pet"（GitHub リポジトリ名: `cardboard-pet`）。内部 mod id / パッケージ名は `takumimod`（`com.takumi.takumimod`）のまま。日本語コンテキストのプロジェクトで、リソース（アイテム名など）は日本語・英語両対応。

## ビルド・実行

```
./gradlew runClient    # クライアント起動（初回はMCソースのDL/デコンパイルで時間がかかる）
./gradlew build        # build/libs/ にjarを生成
```

## 構造

- `src/main/java/com/takumi/takumimod/`
  - `TakumiMod.java` — メインクラス。DeferredRegisterの登録、クリエイティブタブへのアイテム追加
  - `Config.java` — Forge ConfigSpec（MDKのサンプルのまま、未使用寄り）
  - `entity/CardboardBoxEntity.java` — 「段ボール」Mob本体。オーナーUUIDを同期データ+NBTで永続化
  - `entity/goal/FlyAtTargetGoal.java` — 段ボールが対象へ空中突撃するAIゴール（`LeapAtTargetGoal`の代替）
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
- 新しいアイテム/エンティティを追加したら、`en_us.json` と `ja_jp.json` の両方にローカライズキーを追加すること
- `README.md` は日本語で、ユーザー向けの機能一覧・レシピを記載。実装を変更したら追従させる
