# リバーシゲーム 仕様書

## プロジェクト概要
Javaで実装されたリバーシ(オセロ)ゲーム。以下の特徴を持つ:
- プレイヤー対コンピュータ、プレイヤー対プレイヤー、コンピュータ対コンピュータの対戦が可能
- 複数のAI戦略を実装
- GUIとコンソール出力を備える

## 実行方法

### 1. ビルド
```bash
./build.sh
```
- classesディレクトリにクラスファイルが生成されます

### 2. 各モードの実行

#### プレイヤー対コンピュータ (PlayerVSComputer)
```bash
java -cp classes jp.hashiwa.reversi.play.PlayerVSComputer [AIクラス名]
```
例:
```bash
java -cp classes jp.hashiwa.reversi.play.PlayerVSComputer
java -cp classes jp.hashiwa.reversi.play.PlayerVSComputer AlphaBetaPlayer
```

#### コンピュータ対コンピュータ (ComputerVSComputer)
```bash
java -cp classes jp.hashiwa.reversi.play.ComputerVSComputer [AIクラス名1] [AIクラス名2]
```
例:
```bash
java -cp classes jp.hashiwa.reversi.play.ComputerVSComputer
java -cp classes jp.hashiwa.reversi.play.ComputerVSComputer MinMaxPlayer RandomPlayer
```

#### AI対戦統計取得 (Competitions)
```bash
java -cp classes jp.hashiwa.reversi.play.Competitions [ゲーム数]
```
例:
```bash
java -cp classes jp.hashiwa.reversi.play.Competitions
java -cp classes jp.hashiwa.reversi.play.Competitions 20
```

#### テスト実行 (Test)
```bash
java -cp classes jp.hashiwa.reversi.play.Test
```

## 主要クラス説明

### コアクラス
- `RFrame`: メインウィンドウ(GUI)
- `RBoard`: 8x8のゲームボード
- `RCell`: セル(駒)の状態管理
- `RManager`: ゲーム進行管理
- `GameState`: ゲーム状態管理

### AIプレイヤー
- `RandomPlayer`: ランダム配置
- `MinMaxPlayer`: MinMaxアルゴリズム
- `AlphaBetaPlayer`: Alpha-Beta法
- `BiasedMMPlayer`: 重み付けMinMax
- `EdgePreferringPlayer`: 辺優先戦略

## システムプロパティ

実行時に設定可能:
```bash
-Djp.hashiwa.reversi.minmax.depth=3  # MinMaxの探索深さ
-Djp.hashiwa.reversi.alphabeta.depth=5  # AlphaBetaの探索深さ
-Djp.hashiwa.reversi.debug=true  # デバッグモード
```

例:
```bash
java -cp classes -Djp.hashiwa.reversi.minmax.depth=5 jp.hashiwa.reversi.play.PlayerVSComputer MinMaxPlayer
