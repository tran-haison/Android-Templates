package co.nimblehq.sample.compose.ui.screens.third

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import co.nimblehq.sample.compose.R
import co.nimblehq.sample.compose.model.UiModel
import co.nimblehq.sample.compose.ui.AppDestination
import co.nimblehq.sample.compose.ui.screens.AppBar
import co.nimblehq.sample.compose.ui.theme.ComposeTheme

@Composable
fun ThirdScreen(
    viewModel: ThirdViewModel = hiltViewModel(),
    navigator: (destination: AppDestination) -> Unit,
    model: UiModel?
) {
    ThirdScreenContent(data = model)
}

@Composable
fun ThirdScreenContent(data: UiModel?) {
    Scaffold(
        topBar = {
            AppBar(title = R.string.third_title_bar)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = stringResource(R.string.third_data_title, data.toString()),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
fun ThirdScreenPreview() {
    ComposeTheme {
        ThirdScreenContent(data = UiModel("1"))
    }
}
