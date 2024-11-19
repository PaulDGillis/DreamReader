import com.pgillis.dream.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.declarative.dsl.schema.FqName.Empty.packageName
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

class DesktopAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("dream.kotlin").get().get().pluginId)
            }

            extensions.configure<DesktopExtension> {
                application.nativeDistributions {
                    targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                    packageName = "com.pgillis.dream" // TODO circle back and make this a param, so its not tied to this project
                    packageVersion = "1.0.0"
                }
            }
        }
    }
}