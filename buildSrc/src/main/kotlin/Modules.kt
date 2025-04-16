/**
 * Object holding module names used in the project.
 *
 * This object centralizes the management of module names, providing a single
 * source of truth for referencing modules within the project.  It uses
 * `const val` declarations to define the names of various modules, such as
 * `app`, `data`, `fcm`, and so on.
 *
 * Using this `Modules` object promotes consistency in module naming across
 * the project and simplifies referring to modules in various contexts, such as
 * dependency declarations or build configurations.
 *
 * Example usage in a `build.gradle.kts` file:
 **/
object Modules {
    const val app = ":app"
    const val data = ":data"
    const val fcm = ":fcm"
    const val common = ":common"
    const val resource = ":resource"
    const val config = ":config"
    const val core = ":core"
    const val offline = ":data:offline"
    const val network = ":data:network"
    const val provider = ":data:provider"
    const val installer = ":features:installer"
    const val mediator = ":features:ondemand:mediator"
}

/**
 * Object holding names of dynamic feature modules.
 *
 * This object centralizes the management of dynamic feature module names within
 * the project. Dynamic feature modules are optional modules that can be
 * downloaded and installed on demand, providing additional functionality
 * without increasing the initial app download size.  The object uses `const
 * val` declarations to define the names of these modules, such as `chat` and
 * `video`.
 *
 * Using this `DynamicFeature` object promotes consistency in naming dynamic
 * feature modules and simplifies referencing them in contexts such as
 * feature installation logic or build configurations.
 *
 * Example usage (within the app module's `build.gradle.kts` file):
 **/
object DynamicFeature {
    const val chat = ":features:ondemand:chat"
    const val video = ":features:ondemand:video"
}
