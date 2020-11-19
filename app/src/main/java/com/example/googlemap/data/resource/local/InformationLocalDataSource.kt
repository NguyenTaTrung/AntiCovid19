package com.example.googlemap.data.resource.local

import android.content.Context
import com.example.googlemap.R
import com.example.googlemap.data.model.Symptom
import com.example.googlemap.data.resource.InformationDataSource

class InformationLocalDataSource(private val context: Context) : InformationDataSource.Local {
    override fun getSymptoms(): List<Symptom> = mutableListOf(
        Symptom(R.drawable.img_headache, context.getString(R.string.title_headche), context.getString(R.string.text_headache_content)),
        Symptom(R.drawable.img_cough, context.getString(R.string.title_cough), context.getString(R.string.text_cough_content)),
        Symptom(R.drawable.img_fever, context.getString(R.string.title_fever), context.getString(R.string.text_fever_content))
    )
}
