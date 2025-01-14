package co.nimblehq.sample.compose.model

import android.os.Parcelable
import co.nimblehq.sample.compose.domain.model.Model
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiModel(
    val id: String
) : Parcelable

fun Model.toUiModel() = UiModel(id.toString())
