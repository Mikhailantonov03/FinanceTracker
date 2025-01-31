package com.devautro.financetracker

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devautro.financetracker.core.presentation.navigation.NavigationScreen
import com.devautro.financetracker.feature_settings.presentation.SettingsViewModel
import com.devautro.financetracker.ui.theme.FinanceTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen().apply {
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }

        }
        super.onCreate(savedInstanceState)
        setContent {
            val vm = hiltViewModel<SettingsViewModel>()
            val state = vm.settingsState.collectAsStateWithLifecycle()
            splash.setKeepOnScreenCondition { state.value.isLoading }

            FinanceTrackerTheme(darkTheme = state.value.isDarkTheme) {
                NavigationScreen(
                    settingsViewModel = vm,
                    currencySign = state.value.selectedCurrency
                )
            }
        }
    }

}