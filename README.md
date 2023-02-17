# Compose QR Code
__A simple, flexible QR Code Renderer For Jetpack Compose__

<img src="./docs/images/purple_and_gold.png" width="150px" height="150px" />
<img src="./docs/images/lightning.png" width="150px" height="150px" />
<img src="./docs/images/light_smile_square.png" width="150px" height="150px" />

## Usage

Here's a plain ol' boring QR Code:

```kotlin
@Composable
fun BoringPreview() {
    QrCodeView(
        data = "https://github.com/lightsparkdev/compose-qr-code",
        modifier = Modifier.size(300.dp)
    )
}
```

It'll look like this:

<img src="./docs/images/boring.png" width="150px" height="150px" />

Meh... Let's spice it up a bit with a smiley face:

```kotlin
@Composable
fun SmileyPreview() {
    QrCodeView(
        data = "https://github.com/lightsparkdev/compose-qr-code",
        modifier = Modifier.size(300.dp),
        overlayContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Smile(
                    modifier = Modifier.fillMaxSize(),
                    backgroundColor = Color.Yellow,
                    smileColor = Color.Black
                )
            }
        })
}
```

<img src="./docs/images/light_smile_square.png" width="150px" height="150px" />

TODO: Add a documentation and examples.
