package com.example.googlemap.data.model.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class Rss(
    @field:Element(name = "channel")
    var channel: Channel? = null
) {
    override fun toString(): String {
        return super.toString()
    }
}
