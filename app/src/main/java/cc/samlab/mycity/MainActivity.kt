package cc.samlab.mycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cc.samlab.mycity.datasourse.dataSource
import cc.samlab.mycity.R
import cc.samlab.mycity.ui.MyCityCategoryRecommendationDetailsScreen
import cc.samlab.mycity.ui.MyCityHomeScreen
import cc.samlab.mycity.ui.MyCityRecommendationScreen
import cc.samlab.mycity.ui.MyCityViewModel
import cc.samlab.mycity.ui.theme.MyCityTheme
import cc.samlab.mycity.utils.MyCityContentType

enum class ABC(@StringRes val title: Int) {
    Shanghai(title = R.string.app_name),
    Recommandation(title = R.string.page_2),
    Detail(title = R.string.page_3)
}
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyCityTheme {
                val windowSize = calculateWindowSizeClass(this)
                Surface() {
                    MyCityApp(windowSize = windowSize.widthSizeClass)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCityTopAppBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(currentScreen) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Navigate Back"
                    )
                }
            }
        }
    )
}

@Composable
fun MyCityApp(windowSize: WindowWidthSizeClass) {
    val navController = rememberNavController()
    val viewModel: MyCityViewModel = viewModel()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = backStackEntry?.destination?.route ?: "MyCityHomeScreen"

    val contentType: MyCityContentType
    when(windowSize){
        WindowWidthSizeClass.Compact -> {
            contentType = MyCityContentType.single
        }
        WindowWidthSizeClass.Medium -> {
            contentType = MyCityContentType.double
        }
        WindowWidthSizeClass.Expanded ->{
            contentType = MyCityContentType.triple
        }
        else ->{
            contentType = MyCityContentType.single
        }
    }

    Scaffold(
        topBar = {
            MyCityTopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                modifier = Modifier
            )
        },
    ) { innerPadding ->

        val uiState by viewModel.uiState.collectAsState()

        if(uiState.selectedCategory == null){
            viewModel.updateSelectedCategory(dataSource.cityCatalog.get(0))
        }

        if(uiState.selectedRecommendation == null){
            viewModel.updateSelectedRecommendation(
                dataSource.cityCatalog.get(0)
                .recommendations.get(0)
            )
        }

        NavHost(
            navController = navController,
            startDestination = ABC.Shanghai.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ABC.Shanghai.name) {
                MyCityHomeScreen(
                    onCategoryClicked = {
                        viewModel.updateSelectedCategory(category = it)
                        if (contentType != MyCityContentType.triple) {
                            navController.navigate(ABC.Recommandation.name)
                        }
                    },

                    onRecommendationClicked = {
                        viewModel.updateSelectedRecommendation(recommendation = it)
                        if (contentType != MyCityContentType.triple) {
                            navController.navigate(ABC.Detail.name)
                        }
                    },
                    contentType = contentType,
                    myCityUiState = uiState
                )
            }
            composable(route = ABC.Recommandation.name) {
                MyCityRecommendationScreen(
                    myCityUiState = uiState,
                    onRecommendationClicked = {
                        viewModel.updateSelectedRecommendation(recommendation = it)
                        navController.navigate(ABC.Detail.name)
                    }
                )
            }
            composable(route = ABC.Detail.name) {
                uiState.selectedRecommendation?.let { it1 ->
                    MyCityCategoryRecommendationDetailsScreen(
                        it1,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
