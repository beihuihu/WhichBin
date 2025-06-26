# WhichBin 是什么垃圾
*智能识别，精准分类——让垃圾分类更简单*

## 📱 应用概述
WhichBin 是一款基于 Android 平台的智能垃圾分类助手，采用 Kotlin 开发，支持上海市和成都市的分类标准（其他地区尽情期待）。通过图像识别、文字搜索和互动学习功能，帮助用户快速准确分类垃圾。

## ✨ 核心功能
### 1. 智能识别
- ​**图像识别**​：本地 ncnn 模型 + 云端接口双识别
- ​**实时识别**​：摄像头实时分类
- ​**文字搜索**​：模糊匹配垃圾类别

### 2. 城市分类标准
- 自动切换上海/成都分类规则
- 本地数据库动态更新

### 3. 百科知识
- 图文详解四类垃圾定义
- 投放要求与常见误区

### 4. 互动学习
- 随机抽取5道题目
- 卡牌式答题界面（翻转动画+滑动效果）
- 积分奖励机制

## 🛠️ 技术架构
### 核心技术
```kotlin
dependencies {
    implementation 'com.tencent.youtu:ncnn-android:1.0.0'  // 本地模型
    implementation 'com.squareup.retrofit2:retrofit:2.9.0' // 网络请求
}
### 项目架构
app/src/main/
├── assets/ # 静态资源
│ ├── best.bin # ncnn模型权重文件
│ ├── best.param # ncnn模型结构文件
│ └── synset.txt # 分类标签文件
│
├── java/com/example/whichbin/
│ ├── classification/   # 图像分类核心模块
│ ├── database/         # 本地数据库操作
│ ├── http/             # 网络请求封装
│ ├── Login/            # 登录注册模块
│ └── ui/               # 界面相关
│ ├── ActivityCollector # 活动管理器
│ ├── BaseActivity      # 基础Activity
│ ├── CircleImageView   # 自定义圆形图片控件
│ ├── MainActivity      # 主界面
│ ├── UserData.kt       # 用户数据模型
│ └── WelcomeActivity   # 欢迎页
│
├── jni/ # 本地代码（C++）
│ └── ncnn-20220216-android/ # ncnn框架文件（已.gitignore忽略）
│ ├── CMakeLists.txt         # CMake构建脚本
│ ├── mobileNet_jni.cpp      # JNI接口实现
│ └── ts.id.h                # 头文件
│
├── res/                      # 资源目录
│   ├── drawable/             # 通用图片资源
│   ├── drawable-v24/         # API 24+专用资源
│   ├── layout/               # XML布局文件
│   ├── menu/                 # 菜单定义文件
│   ├── mipmap-*/             # 多密度图标资源（hdpi~xxxhdpi）
│   ├── navigation/           # Navigation组件路由定义
│   ├── values/               # 通用资源配置
│   │   ├── colors.xml        # 颜色定义
│   │   ├── strings.xml       # 字符串定义
│   │   └── styles.xml        # 主题样式
│   ├── values-night/         # 深色模式资源
│   └── xml/                  # 其他XML配置
│
├── AndroidManifest.xml       # 应用清单文件
├── build.gradle              # 模块级构建脚本
└── proguard-rules.pro        # 混淆规则文件
opencv/
├── build.gradle
└── 其他文件                  #已.gitignore忽略

## 所需配置
Android Studio Electric Eel 2021.1+
Android SDK 33+
NDK 25+（ncnn 支持）

##调用的开源包
easypermissions-3.0.0
retrofit-2.6.0
ncnn-20220216-android
opencv-4.3.0-android-sdk
UCrops-2.2.10
...
