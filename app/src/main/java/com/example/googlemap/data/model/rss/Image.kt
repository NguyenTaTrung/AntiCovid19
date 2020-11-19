package com.example.googlemap.data.model.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "image", strict = false)
data class Image(
    @field:Element(name = "url")
    var url: String? = null,
    @field:Element(name = "title")
    var title: String? = null,
    @field:Element(name = "link")
    var link: String? = null
)
