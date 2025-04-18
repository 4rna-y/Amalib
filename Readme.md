# Amalib
俺が楽するためのライブラリ。
俺以外も楽できるかも。

# Install
1. [Releaseページ](https://github.com/4rna-y/Amalib/releases) から``Amalib.jar``をダウンロード
2. プラグインプロジェクトのどこかに配置する
```
YourProject
├── lib
│   └── ! >> Amalib.jar << !
├── src
│   └── main
│       ├── kotlin
│       └── resources
├── build.gradle.kts
└── settings.gradle.kts
```
3. build.gradle.kts を編集する
```kt:build.gradle.kts
dependencies {
    implementation(files("lib/Amalib.jar"))
}
```

# [Wiki](https://github.com/4rna-y/Amalib/wiki)

