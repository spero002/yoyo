# Android UFile SDK 使用说明文档

## UFile SDK 说明

    sdk以library形式开源提供 支持gradle编译 ant编译请调整目录结构

    需提供用户公钥 (按需提供私钥) 以及bucket : bucket name 、proxySuffix : 域名后缀


## UFile SDK 目录介绍

  #### app  该目录为 ufile sdk demo

  #### ufilesdk 该目录为 ufile sdk library工程


## UFile SDK使用说明

  #### gradle 项目示例

   1. 将ufilesdk 拷贝到项目根目录

   2. 在项目setting.gradle中 添加

    include ':ufilesdk'

   - 例如 setting.gradle

    include ':app', ':ufilesdk'

   3. 在项目build.gradle中 添加

    compile project(':ufilesdk')

   - 例如 app/build.gradle

    dependencies {
        compile fileTree(include: ['*.jar'], dir: 'libs')
        testCompile 'junit:junit:4.12'
        compile 'com.android.support:appcompat-v7:23.1.1'
        compile 'com.android.support:design:23.1.1'
        compile project(':ufilesdk')
    }

   4. SDK使用

   - 详见 UFileSDK 中注释 及 app目录中 MainActivity 中示例

