package com.example.googlemap.data.model.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "channel", strict = false)
data class Channel(
    @field:Element(name = "image")
    var image: Image? = null,
    @field:Element(name = "pubDate")
    var pubDate: String? = null,
    @field:Element(name = "generator")
    var generator: String? = null,
    @field:Element(name = "link")
    var link: String? = null,
    @field:ElementList(inline = true, required = false, name = "item")
    var item: List<Item>? = null
) {
    override fun toString(): String {
        return super.toString()
    }
}
