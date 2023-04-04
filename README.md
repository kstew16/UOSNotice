# 📢 UOSNotice (진행중인 프로젝트)

## 목표

![uosnotice](/images/uosnotice.jpg)

**그 공지 못 봤는데? 와 마감됐어?**

네.. 제가 그랬습니다. 다들 저처럼 공지사항 놓치는 일 없기를 바랄게요.
개인화된 공지사항 로딩/키워드 알림 기능 제공을 목적으로 하는 애플리케이션입니다.

## 주요 특징

**JetPack Compose 만을 사용한 UI**로 구성되어 있습니다.
Retrofit2와 Kotlin Coroutines를 통해 MVVM 패턴을 기반으로 크롤링한 데이터를 사용자에게 비동기적으로 제공하고, 공지사항 검색, 키워드 알림 등을 제공할 예정입니다.

## 🖥️ 빌드 환경

이 프로젝트는 `Gradle` 빌드 시스템을 사용합니다.
이 프로젝트를 빌드하려면 `gradlew build` 커맨드를 사용하거나, Android Studio 에서 프로젝트를 열어주세요.

이 프로젝트는 `Gradle 7.0.4` 와 `JDK 11`를 사용한 환경에서 작업되었습니다.

- `minSdkVersion` : 29
- `targetSdkVersion` : 31

### 🏛️ 사용된 라이브러리

- #### UI

  - [Jetpack_Compose](https://developer.android.com/jetpack/compose)

- #### Android Architecture Components

  - [Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle)
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)

- #### 의존성 주입

  - [Hilt](https://dagger.dev/hilt)

- #### 비동기 프로그래밍

  - [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - 
  
- #### 네트워크

  - [Retrofit 2](https://square.github.io/retrofit)
  - [OkHttp 3](https://square.github.io/okhttp)
