package eu.livesport.mdevcamp26.di

import eu.livesport.mdevcamp26.shared.di.sharedModule
import org.koin.dsl.module

val appModule = module {
    includes(sharedModule)
}
