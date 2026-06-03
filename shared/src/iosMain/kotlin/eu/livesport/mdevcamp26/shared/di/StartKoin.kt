package eu.livesport.mdevcamp26.shared.di

import org.koin.core.context.startKoin

fun startKoin() {
    startKoin {
        modules(sharedModule)
    }
}
