
# SSComposeInfoBar Customization

### You can create your own custom InfoBar using SSComposeInfoBar composable which accepts following parameters.

| Parameter Name   | Parameter Type            | Description                                                                    | Default Value                                      |
|------------------|---------------------------|--------------------------------------------------------------------------------|----------------------------------------------------|
| title            | TextType                  | The title of the InfoBar (Wrapper around String and AnnotatedString.           | N/A                                                |
| titleStyle       | TextStyle                 | Text style of the InfoBar title.                                               | SSComposeInfoBarDefaults.defaultTitleStyle         |
| description      | TextType?                 | The description of the InfoBar.                                                | null                                               |
| descriptionStyle | TextStyle                 | Text style of the InfoBar description.                                         | SSComposeInfoBarDefaults.defaultDescriptionStyle   |
| customBackground | SSCustomBackground        | Background of the InfoBar (Wrapper around Support PNG, SVG and Color).         | SSComposeInfoBarDefaults.defaultSSCustomBackground |
| icon             | ImageVector               | Icon that will be displayed along with title and description.                  | Icons.Default.Info                                 |
| shape            | Shape                     | Shape of the InfoBar.                                                          | SSComposeInfoBarDefaults.shape                     |
| elevations       | SSComposeInfoBarElevation | Elevation of the InfoBar (Wrapper around Tonal elevation and Shadow elevation. | SSComposeInfoBarDefaults.elevations                |
| contentColors    | SSComposeInfoBarColors    | Content colors for InfoBar content.                                            | SSComposeInfoBarDefaults.colors                    |
| contentPadding   | PaddingValues             | PaddingValues that will be applied to InfoBar.                                 | SSComposeInfoBarDefaults.contentPadding            |
| height           | Dp                        | Height of the InfoBar.                                                         | SSComposeInfoBarDefaults.defaultHeight             |
| actionText       | String                    | Title of the action button if action is showed.                                | SSComposeInfoBarDefaults.defaultActionTitle        |
| onActionClicked  | (() -> Unit)?             | Callback for when user clicks the action button on InfoBar.                    | null                                               |
| onCloseClicked   | () -> Unit                | Callback for when user clicks the close icon on InfoBar.                       | Empty Lambda                                       |
| isInfinite       | Boolean                   | Flag that decides whether to show a close icon on InfoBar.                     | false                                              |
