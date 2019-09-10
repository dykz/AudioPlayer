# AudioPlayer

### 介绍
音频播放工具
--带简易播放界面

#### 版本 [![](https://www.jitpack.io/v/dykz/AudioPlayer.svg)](https://www.jitpack.io/#dykz/AudioPlayer) 

### 使用方式
#### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}

#### Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.dykz:AudioPlayer:1.0.0'
	}

## 目前测试发现 对amr,mp3,wav 音频支持较好，flac支持较差
