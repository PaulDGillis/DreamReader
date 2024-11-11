import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pgillis.dream.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "DreamReader",
    ) {
        App()
    }
}