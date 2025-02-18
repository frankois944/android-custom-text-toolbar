# demo-test-tech


This sample replace the default contextual menu of a selected text on Compose Android/CMP by a custom one.

This sample uses Compose multiplatform, but it can also work with Compose for Android / iOS.

I have replaced the contextual menu when selecting some text to add more text manipulation than the default one.

The dialog for adding a link could be avoided (and replaced), but it will complicate the code.

Also, there are some edge cases when manipulating the link that need to be debugged.

Waiting for update of the Text Editor and Compose for iOS

References :

- [Entry point of the code](composeApp/src/commonMain/kotlin/fr/francoisdabonot/texttoolbareditordemo/App.kt)
- [Text editor engine](https://github.com/MohamedRejeb/compose-rich-editor)

Some screens

| ![simple](./screens/screen1.png)       | ![with selection](./screens/screen2.png) |
|----------------------------------------|------------------------------------------|
| ![add new link](./screens/screen3.png) |                                          |

