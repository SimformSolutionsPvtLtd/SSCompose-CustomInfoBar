
# SSComposeInfoHost Config Customization

## You can customise the SSComposeInfoBar Behaviour by configuring the SSComposeInfoHost.

1.  **Such as the direction from which the InfoBar will be shown.**
    ```kotlin
        var direction by remember {
            mutableStateOf((SSComposeInfoBarDirection.Top))
        }
  
        SSComposeInfoHost(
            modifier = Modifier
                .fillMaxSize(),
            composeHostState = composeInfoHostState,
            direction = direction
        )
    ```

2.  **Whether we want to show a default info bar when there is no internet connection.**
    ```kotlin
        SSComposeInfoHost(
            modifier = Modifier
                .fillMaxSize(),
            composeHostState = composeInfoHostState,
            enableNetworkMonitoring = true,
        )
    ```
3.  **To show a custom ComposeInfoBar we can pass our own Composable in the parameter.**
    ```kotlin
        SSComposeInfoHost(
            modifier = Modifier
                .fillMaxSize(),
            composeHostState = composeInfoHostState,
            composeInfoBar = { content ->
                SSComposeInfoBar(
                    title = content.title,
                    description = content.description
                )
            }
        )
    ```

4.  **If you want to have a functionality where the info bar gets dismissed when user starts scrolling some content downwards and it should be visible only if user scrolls slightly upwards.**
    ```kotlin
        val lazyListState = rememberLazyListState()
    
        SSComposeInfoHost(
            modifier = Modifier
                .fillMaxSize(),
            composeHostState = composeInfoHostState,
            contentScrollState = lazyListState
        )
    ```

5. **If you want a callback when the info bar is dismissed successfully then we can set an onDismissListener to ComposeInfoHostState**
    ```kotlin
        val composeInfoHostState by remember {
            mutableStateOf(SSComposeInfoHostState())
        }
   
        composeInfoHostState.setOnInfoBarDismiss {
            Toast.makeText(
                context,
                context.getString(R.string.info_bar_dismissed_successfully),
                Toast.LENGTH_SHORT
            ).show()
        }
    
        SSComposeInfoHost(
            modifier = Modifier
                .fillMaxSize(),
            composeHostState = composeInfoHostState
        )
    ```
