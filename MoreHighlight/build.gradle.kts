version = "1.4.4"
description = "Adds more syntax highlighting options"

aliucord.changelog.set(
    """
    New {added}
    ======================
    * Added option to use star bullets instead of default bullets (hyphens become ☆ while asterisks become ★)
    * Added option to disable asterisk bullet point recognition
    * Now supports disabling either or both bullet point types (asterisk and hyphen)
    
    Previous Updates (v1.4.3) {updated}
    ======================
    * Added headers, subtext and bulletpoints -serinova (credit to AAurus for their version with their regex, god send)
    * Added slider in settings to change the headers font size
    * Added option to disable hyphen bulletpoint recognition
    
    Fixed (v1.4.2) {fixed marginTop}
    ======================
    * Bullets and subtext no longer prevent other markdown from working
    """.trimIndent()
)