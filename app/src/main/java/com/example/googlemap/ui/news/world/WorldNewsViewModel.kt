package com.example.googlemap.ui.news.world

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.base.RxViewModel
import com.example.googlemap.data.model.News
import com.example.googlemap.data.resource.repository.InformationRepository
import com.example.googlemap.utils.PatternConst.REGEX_IMAGE_URL
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.regex.Pattern

class WorldNewsViewModel(repository: InformationRepository) : RxViewModel() {

    private val listNews = mutableListOf<News>()

    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>>
        get() = _news

    init {
        _isLoading.value = true
        val pattern = Pattern.compile(REGEX_IMAGE_URL)
        var image: String? = null
        repository.getWorldNewsVNExpress()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.channel?.item?.forEach { item ->
                    val matcher = pattern.matcher(item.description.toString())
                    if (matcher.find()) image = matcher.group(1)

                    if (item.title?.contains("Covid-19") == true ||
                        item.title?.contains("nCoV") == true) {
                        listNews.add(News(item.title, item.link, item.pubDate, image))
                    }
                }
            }, {
                _isLoading.value = false
                _error.value = it.message.toString()
            }, {
                _news.value = listNews
                _isLoading.value = false
            })
            .addTo(disposables)
    }
}
