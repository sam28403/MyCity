package cc.samlab.mycity.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.samlab.mycity.datasourse.dataSource
import cc.samlab.mycity.model.Category
import cc.samlab.mycity.model.MyCityUiState
import cc.samlab.mycity.model.Recommendation
import cc.samlab.mycity.utils.MyCityContentType



@Composable
fun MyCityHomeScreen(
    onCategoryClicked: (category: Category) -> Unit,
    onRecommendationClicked: (recommendation: Recommendation) -> Unit,
    contentType: MyCityContentType,
    myCityUiState: MyCityUiState
) {
    val cityCategories = dataSource.cityCatalog
    Row(modifier = Modifier.padding(8.dp)) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(cityCategories) {
                HomeScreenList(
                    category = it,
                    onCategoryClicked = onCategoryClicked
                )
            }
        }

        if(contentType == MyCityContentType.triple){
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                myCityUiState.selectedCategory?.let {
                    items(it.recommendations) {
                        RecommendationList(
                            recommendation = it,
                            onRecommendationClicked = onRecommendationClicked
                        )
                    }
                }
            }
            myCityUiState.selectedRecommendation?.let {
                MyCityCategoryRecommendationDetailsScreen(
                    recommendation = it,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun HomeScreenList(
    category: Category,
    onCategoryClicked: (category: Category) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(size = 8.dp),
        modifier = Modifier.clickable {
            onCategoryClicked(category)
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = category.imageResourceId),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    .clip(MaterialTheme.shapes.small),
                //contentScale = ContentScale.Crop,
            )
            Text(
                text = stringResource(id = category.name),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}