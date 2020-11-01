A Game similar to Tetris written in Kotlin
------------------------------------------

I created this codebase to get in touch with Kotlin and the Kotlin Multiplatform Plugin.
The Game has a very basic textual JavaFX UI with `TetrisJfx.kt` in `jvmMain` and a more pretty HTML/Canvas/JavaScript UI in `jsMain`.
The `commonMain` module contains the Games Core Code.

For tests i mainly use Junit5 and AssertJ, so they are to be found in `jvmTest`.
I use the `tdd.sh` script to run the tests continuously.

### Run the game in a browser
```
./gradlew jsBrowserDevelopmentRun
```