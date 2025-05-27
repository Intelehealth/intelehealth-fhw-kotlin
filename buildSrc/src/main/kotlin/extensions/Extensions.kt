package extensions

import java.util.*

/**
 * Checks if a string represents a stable version number.
 *
 * This extension function on [String] determines whether the string is a
 * stable version number based on common versioning conventions. A version
 * is considered stable if it either contains a stable keyword (case-insensitive
 * "RELEASE", "FINAL", or "GA") or matches a regular expression for basic stable
 * version formats (e.g., "1.0.0", "v2.3", "1-0-0").
 *
 * @return `true` if the string represents a stable version, `false` otherwise.
 */
fun String.isStableVersion(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { uppercase(Locale.ROOT).contains(it) }
    return stableKeyword || Regex("^[0-9,.v-]+(-r)?$").matches(this)
}

/**
 * Checks if a version string indicates a non-stable release.
 *
 * This function determines whether a given version string represents a
 * non-stable software release (e.g., alpha, beta, release candidate) based on
 * common versioning conventions.  It considers a version to be stable if it
 * either contains a stable keyword (case-insensitive "RELEASE", "FINAL", or
 * "GA") or matches a regular expression for basic stable version formats (e.g.,
 * "1.0.0", "v2.3", "1-0-0").  Any version that does not meet these criteria is
 * considered non-stable.
 *
 * @param version The version string to check.
 * @return `true` if the version string indicates a non-stable release, `false`
 *   otherwise.
 */
fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any {
        version.uppercase(Locale.ROOT).contains(it)
    }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
