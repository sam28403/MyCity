package cc.samlab.mycity.ui

import android.graphics.fonts.FontFamily
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.samlab.mycity.model.MyCityUiState
import cc.samlab.mycity.model.Recommendation

@Composable
fun MyCityRecommendationScreen(
    myCityUiState: MyCityUiState,
    onRecommendationClicked: (recommendation: Recommendation) -> Unit,
) {
    Box(modifier = Modifier.padding(8.dp)) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            myCityUiState.selectedCategory?.let {
                items(it.recommendations) {
                    RecommendationList(
                        recommendation = it,
                        onRecommendationClicked = onRecommendationClicked
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendationList(
    recommendation: Recommendation,
    onRecommendationClicked: (recommendation: Recommendation) -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(size = 8.dp),
        modifier = Modifier.clickable {
            onRecommendationClicked(recommendation)
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = recommendation.imageResourceId),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = recommendation.name),
                fontSize = 24.sp
            )
        }
    }
}