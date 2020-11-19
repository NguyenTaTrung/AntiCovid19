package com.example.googlemap.data.model.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class Item(
    @field:Element(name = "title")
    var title: String? = null,
    @field:Element(name = "description")
    var description: String? = null,
    @field:Element(name = "pubDate")
    var pubDate: String? = null,
    @field:Element(name = "link")
    var link: String? = null
) {
    override fun toString(): String {
        return super.toString()
    }
}
