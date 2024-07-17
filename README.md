![Banner]

# :speech_balloon: SSCompose-CustomInfoBar :speech_balloon:

<div align="center">

[![Platform-badge]][Android]
[![Jetpack Compose-badge]][Android]
[![API-badge]][Android]
[![Kotlin Version](https://img.shields.io/badge/Kotlin-v1.9.23-blue.svg)](https://kotlinlang.org)
[![Release-badge]][Release]

</div>

<!-- Description -->
Welcome to our SSCompose-CustomInfoBar Library! :tada:
Tired of generic snackbars? This Jetpack Compose library offers a powerful and customizable way to
display informative messages within your app.

## :zap: Features

- **Content**: Add :speech_balloon: text, icons, or even custom composables to your info bar for
  ultimate flexibility.
- **Position**: Display the info bar strategically at the :point_up: top or :point_down: bottom of
  your
  screen.
- **Duration**: Control how long the info bar stays visible, from indefinite to short :zap: or
  long :
  hourglass: durations.
- **Styles**: Predefined styles like :warning: error, :information_source: info, and warning are
  available, or craft your own style with custom background,
  text color, and icons.
- **Swipe to dismiss**: Users can dismiss the info bar with a simple swipe, providing an interactive
  touch.
- **Look & Feel**: :paintbrush: Customize the info bar's shape, color, and elevation to seamlessly
  match
  your app's design.
- **Theming**: Integrate with Jetpack Compose's theming system for a consistent and beautiful look
  across your app.
- **Animations**: :tada: Liven up your info bars with various built-in entrance and exit animations.
- **Offline Notifications**: :signal_strength: A constructor flag which allows to automatically
  displays
  an info bar giving "you are offline" notification.
- **Action button**: :+1: Optional action button to show on InfoBar.
- **On Dismiss Callback**: Get notified about bar dismissal.
- **Queueing System**: :repeat: Display multiple info bars sequentially.

**NOTE:**
- Scroll to show and hide info bar feature work only with LazyListState.
- If the duration of infoBar is infinite then the queue feature won't work.
- Currently there is no limit to how many info bars can be added to queue for displaying.


## :framed_picture: Preview
|Full Preview |
|---|
|<video src="https://github.com/SimformSolutionsPvtLtd/SSCompose-CustomInfoBar/assets/125441434/303282b1-91c2-4094-b030-923403be1e85"  />|

| Default Demo                                   | Error Themed                                 | Warning Themed                                 |
|------------------------------------------------|----------------------------------------------|------------------------------------------------|
| <img src="/gifs/default.gif" height="500px" /> | <img src="/gifs/error.gif" height="500px" /> | <img src="/gifs/warning.gif" height="500px" /> |

| Success Themed                                   | Annotated Strings                                  | Gradient Background                               |
|--------------------------------------------------|----------------------------------------------------|---------------------------------------------------|
| <img src="/gifs/success.gif" height="500px" /> | <img src="/gifs/annotated.gif" height="500px" /> | <img src="/gifs/gradient.gif" height="500px" /> |

| SVG Background                               | PNG Background                               | Action Button                                   |
|----------------------------------------------|----------------------------------------------|-------------------------------------------------|
| <img src="/gifs/svg.gif" height="500px" /> | <img src="/gifs/png.gif" height="500px" /> | <img src="/gifs/action.gif" height="500px" /> |

# :books: How it works:

1. Add the dependency in your app's build.gradle file

    ```kotlin
    dependencies {
        implementation("com.github.SimformSolutionsPvtLtd:SSComposeInfoBar:1.0")
    }
    ```

2. Create ComposeInfoHost with default parameters to host ComposeInfoBar (NOTE: Here we are not
   passing an custom ComposeInfoBar so it will use a default implementation. To show custom
   ComposeInfoBar check out [SSComposeInfoHost-Customisation-Guide])

    ```kotlin
    val composeInfoHostState by remember {
        mutableStateOf(SSComposeInfoHostState())
    }

    SSComposeInfoHost(
        modifier = Modifier
            .fillMaxSize(),
        composeHostState = composeInfoHostState
    ) {
        MainComposable()
    }
    ```

3. Show composeInfoBar anywhere in your project

    ```kotlin
    val coroutineScope = rememberCoroutineScope()

    var duration by remember {
        mutableStateOf(SSComposeInfoDuration.Short)
    }        

    coroutineScope.launch { 
        composeInfoHostState.show(
            infoBarData = SSComposeInfoBarData(title, description),
            duration = duration
        )
    }
    ```

4. For full customisation of SSComposeInfoBar checkout [SSComposeInfoBar-Customisation-Guide]

## :heart: Find this samples useful?

Support it by joining [stargazers] :star: for this repository.

## :handshake: How to Contribute?

Whether you're helping us fix bugs, improve the docs, or a feature request, we'd love to have you! :
muscle: \
Check out our __[Contributing Guide]__ for ideas on contributing.

## :lady_beetle: Bugs and Feedback

For bugs, feature requests, and discussion use [GitHub Issues].

## :rocket: Other Mobile Libraries

Check out our other libraries [Awesome-Mobile-Libraries].

## :balance_scale: License

Distributed under the MIT license. See [LICENSE] for details.

<!-- Reference links -->

[Banner]:                   https://github.com/user-attachments/assets/231ef0fa-4ac0-4bf4-9060-63c3b15f0512

[Android]:                  https://www.android.com/

[Android App Architecture]:  https://developer.android.com/topic/architecture

[Release]:                  https://github.com/SimformSolutionsPvtLtd/SSCompose-CustomInfoBar/releases/latest

[stargazers]:               https://github.com/SimformSolutionsPvtLtd/SSCompose-CustomInfoBar/stargazers

[Contributing Guide]:       CONTRIBUTING.md

[Github Issues]:            https://github.com/SimformSolutionsPvtLtd/SSCompose-CustomInfoBar/issues

[Awesome-Mobile-Libraries]: https://github.com/SimformSolutionsPvtLtd/Awesome-Mobile-Libraries

[license]:                  LICENSE

[SSComposeInfoBar-Customisation-Guide]:        SSComposeInfoBarConfigurationGuide.md

[SSComposeInfoHost-Customisation-Guide]:        ComposeInfoHostConfigurationGuide.md

<!-- Badges -->

[Platform-badge]:               https://img.shields.io/badge/Platform-Android-green.svg?logo=Android

[Jetpack Compose-badge]:        https://img.shields.io/badge/Jetpack_Compose-v1.5.11-1c274a.svg?logo=jetpackcompose&logoColor=3ddc84

[API-badge]:                    https://img.shields.io/badge/API-21+-51b055

[Release-badge]:                https://img.shields.io/github/v/release/SimformSolutionsPvtLtd/SSCompose-CustomInfoBar

[License Badge-badge]:          https://img.shields.io/github/license/SimformSolutionsPvtLtd/SSCompose-CustomInfoBar
